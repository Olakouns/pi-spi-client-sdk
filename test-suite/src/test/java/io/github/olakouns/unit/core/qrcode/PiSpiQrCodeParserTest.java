package io.github.olakouns.unit.core.qrcode;

import io.github.olakouns.exception.PiSpiException;
import io.github.olakouns.qrcode.Crc16;
import io.github.olakouns.qrcode.EmvTlv;
import io.github.olakouns.qrcode.MerchantChannel;
import io.github.olakouns.qrcode.PiSpiQrCodeParser;
import io.github.olakouns.qrcode.PiSpiQrPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
@DisplayName("PiSpiQrCodeParser Tests")
class PiSpiQrCodeParserTest {

    private static final String FIXTURE_1 =
            "00020136560012int.bceao.pi0136111c3e1b-4312-49ec-b75e-4c8c74c10fd7" +
                    "5204000053039525802CI5901X6001X62270516X2AB528754YY202211030006304255B";

    @Test
    @DisplayName("Should extract all fields from the documented static-merchant fixture")
    void parsesFixture1Fields() {
        PiSpiQrPayload data = PiSpiQrCodeParser.parse(FIXTURE_1);

        assertThat(data.alias()).isEqualTo("111c3e1b-4312-49ec-b75e-4c8c74c10fd7");
        assertThat(data.countryCode()).isEqualTo("CI");
        assertThat(data.merchantChannel()).isEqualTo(MerchantChannel.STATIC_MERCHANT);
        assertThat(data.referenceLabel()).isEqualTo("X2AB528754YY2022");
        assertThat(data.amount()).isNull();
        assertThat(data.rawPayload()).isEqualTo(FIXTURE_1);
    }

    @Test
    @DisplayName("Should reject a payload whose GUI does not match int.bceao.pi, even when its CRC is otherwise valid")
    void rejectsPayloadWithWrongGuiEvenIfCrcIsValidForIt() {
        String withBadGui = FIXTURE_1.replace("int.bceao.pi", "int.evil.piX"); // same length (12), stale CRC
        String bodyWithoutCrcValue = withBadGui.substring(0, withBadGui.length() - 4);
        String validCrcForTamperedContent = bodyWithoutCrcValue + Crc16.compute(bodyWithoutCrcValue);

        assertThatThrownBy(() -> PiSpiQrCodeParser.parse(validCrcForTamperedContent))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("GUI");
    }

    @Test
    @DisplayName("Should reject a payload with a tampered CRC")
    void rejectsPayloadWithBadCrc() {
        char lastChar = FIXTURE_1.charAt(FIXTURE_1.length() - 1);
        char flipped = lastChar == 'A' ? 'B' : 'A';
        String tampered = FIXTURE_1.substring(0, FIXTURE_1.length() - 1) + flipped;

        assertThatThrownBy(() -> PiSpiQrCodeParser.parse(tampered))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("CRC");
    }

    @Test
    @DisplayName("Should check the GUI before the CRC, reporting the GUI failure when both are wrong")
    void guiIsCheckedBeforeCrc() {
        // GUI tampered but CRC left stale (computed for the original content) -- both checks
        // would fail; the error must name GUI, proving that check runs (and fails) first.
        String bothWrong = FIXTURE_1.replace("int.bceao.pi", "int.evil.piX");

        assertThatThrownBy(() -> PiSpiQrCodeParser.parse(bothWrong))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("GUI");
    }

    @Test
    @DisplayName("Should accept and round-trip a merchant channel code not yet documented")
    void parsesUnknownMerchantChannelWithoutThrowing() {
        String withUnknownChannel = FIXTURE_1.replace("1103000", "1103999"); // same length, stale CRC
        String bodyWithoutCrcValue = withUnknownChannel.substring(0, withUnknownChannel.length() - 4);
        String validPayload = bodyWithoutCrcValue + Crc16.compute(bodyWithoutCrcValue);

        PiSpiQrPayload data = PiSpiQrCodeParser.parse(validPayload);

        assertThat(data.merchantChannel().code()).isEqualTo("999");
    }

    @Test
    @DisplayName("Should reject a payload missing the Merchant Account Information (tag 36)")
    void rejectsPayloadMissingMerchantAccountInformation() {
        String body = EmvTlv.encode("00", "01")
                + EmvTlv.encode("52", "0000")
                + EmvTlv.encode("53", "952")
                + EmvTlv.encode("58", "CI")
                + EmvTlv.encode("59", "X")
                + EmvTlv.encode("60", "X")
                + EmvTlv.encode("62", EmvTlv.encode("11", "000"));

        assertThatThrownBy(() -> PiSpiQrCodeParser.parse(appendCrc(body)))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("36");
    }

    @Test
    @DisplayName("Should reject a payload whose Merchant Account Information is missing the alias (tag 36-01)")
    void rejectsPayloadMissingAlias() {
        String merchantAccountInformationWithoutAlias = EmvTlv.encode("36", EmvTlv.encode("00", "int.bceao.pi"));
        String body = EmvTlv.encode("00", "01")
                + merchantAccountInformationWithoutAlias
                + EmvTlv.encode("52", "0000")
                + EmvTlv.encode("53", "952")
                + EmvTlv.encode("58", "CI")
                + EmvTlv.encode("59", "X")
                + EmvTlv.encode("60", "X")
                + EmvTlv.encode("62", EmvTlv.encode("11", "000"));

        assertThatThrownBy(() -> PiSpiQrCodeParser.parse(appendCrc(body)))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("alias");
    }

    @Test
    @DisplayName("Should reject a payload missing the country code (tag 58)")
    void rejectsPayloadMissingCountryCode() {
        String body = EmvTlv.encode("00", "01")
                + validMerchantAccountInformation()
                + EmvTlv.encode("52", "0000")
                + EmvTlv.encode("53", "952")
                + EmvTlv.encode("59", "X")
                + EmvTlv.encode("60", "X")
                + EmvTlv.encode("62", EmvTlv.encode("11", "000"));

        assertThatThrownBy(() -> PiSpiQrCodeParser.parse(appendCrc(body)))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("country code");
    }

    @Test
    @DisplayName("Should reject a payload missing the merchant channel (tag 62-11)")
    void rejectsPayloadMissingMerchantChannel() {
        String body = EmvTlv.encode("00", "01")
                + validMerchantAccountInformation()
                + EmvTlv.encode("52", "0000")
                + EmvTlv.encode("53", "952")
                + EmvTlv.encode("58", "CI")
                + EmvTlv.encode("59", "X")
                + EmvTlv.encode("60", "X"); // tag 62 entirely absent

        assertThatThrownBy(() -> PiSpiQrCodeParser.parse(appendCrc(body)))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("merchant channel");
    }

    @Test
    @DisplayName("Should reject a payload with a non-numeric transaction amount (tag 54)")
    void rejectsPayloadWithNonNumericAmount() {
        String body = EmvTlv.encode("00", "01")
                + validMerchantAccountInformation()
                + EmvTlv.encode("52", "0000")
                + EmvTlv.encode("53", "952")
                + EmvTlv.encode("54", "ABCD")
                + EmvTlv.encode("58", "CI")
                + EmvTlv.encode("59", "X")
                + EmvTlv.encode("60", "X")
                + EmvTlv.encode("62", EmvTlv.encode("11", "000"));

        assertThatThrownBy(() -> PiSpiQrCodeParser.parse(appendCrc(body)))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("amount");
    }

    private static String validMerchantAccountInformation() {
        return EmvTlv.encode("36",
                EmvTlv.encode("00", "int.bceao.pi") + EmvTlv.encode("01", "111c3e1b-4312-49ec-b75e-4c8c74c10fd7"));
    }

    private static String appendCrc(String bodyBeforeCrcTag) {
        String withCrcTagAndLength = bodyBeforeCrcTag + "6304";
        return withCrcTagAndLength + Crc16.compute(withCrcTagAndLength);
    }
}
