package io.github.olakouns.unit.core.qrcode;

import io.github.olakouns.exception.PiSpiException;
import io.github.olakouns.qrcode.MerchantChannel;
import io.github.olakouns.qrcode.PiSpiQrCodeBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
@DisplayName("PiSpiQrCodeBuilder Tests")
class PiSpiQrCodeBuilderTest {

    private static final String ALIAS = "111c3e1b-4312-49ec-b75e-4c8c74c10fd7";

    @Test
    @DisplayName("Should build the exact payload documented by BCEAO for a static merchant QR")
    void buildsFixture1ExactlyAsDocumented() {
        String payload = new PiSpiQrCodeBuilder()
                .alias(ALIAS)
                .countryCode("CI")
                .merchantChannel(MerchantChannel.STATIC_MERCHANT)
                .referenceLabel("X2AB528754YY2022")
                .build();

        assertThat(payload).isEqualTo(
                "00020136560012int.bceao.pi0136111c3e1b-4312-49ec-b75e-4c8c74c10fd7" +
                        "5204000053039525802CI5901X6001X62270516X2AB528754YY202211030006304255B");
    }

    @Test
    @DisplayName("Should build the exact payload documented by BCEAO for a dynamic merchant QR with an amount")
    void buildsFixture2ExactlyAsDocumented() {
        String payload = new PiSpiQrCodeBuilder()
                .alias(ALIAS)
                .countryCode("CI")
                .merchantChannel(MerchantChannel.DYNAMIC_MERCHANT)
                .amount(1000)
                .referenceLabel("F2YG7643D5HH2023")
                .build();
        assertThat(payload).isEqualTo(
                "00020136560012int.bceao.pi0136111c3e1b-4312-49ec-b75e-4c8c74c10fd7" +
                        "52040000530395254041000" +
                        "5802CI5901X6001X62270516F2YG7643D5HH20231103400" +
                        "63042E79");
    }

    @Test
    @DisplayName("Should build the exact payload documented by BCEAO for a transfer QR")
    void buildsFixture3ExactlyAsDocumented() {
        String payload = new PiSpiQrCodeBuilder()
                .alias("341c3e1b-4312-49gc-b75e-4i8c74c60hJ2")
                .countryCode("TG")
                .merchantChannel(MerchantChannel.STATIC_INDIVIDUAL)
                .build();

        assertThat(payload).isEqualTo(
                "00020136560012int.bceao.pi0136341c3e1b-4312-49gc-b75e-4i8c74c60hJ2" +
                        "5204000053039525802TG5901X6001X6207110373163044448");
    }

    @Test
    @DisplayName("Should reject a missing alias")
    void missingAliasThrowsPiSpiException() {
        assertThatThrownBy(() -> new PiSpiQrCodeBuilder()
                .countryCode("CI")
                .merchantChannel(MerchantChannel.STATIC_MERCHANT)
                .build())
                .isInstanceOf(PiSpiException.class);
    }

    @Test
    @DisplayName("Should reject a country code outside the UEMOA zone")
    void countryCodeOutsideUemoaThrowsPiSpiException() {
        assertThatThrownBy(() -> new PiSpiQrCodeBuilder()
                .alias(ALIAS)
                .countryCode("FR")
                .merchantChannel(MerchantChannel.STATIC_MERCHANT)
                .build())
                .isInstanceOf(PiSpiException.class);
    }

    @Test
    @DisplayName("Should reject a missing merchant channel")
    void missingMerchantChannelThrowsPiSpiException() {
        assertThatThrownBy(() -> new PiSpiQrCodeBuilder()
                .alias(ALIAS)
                .countryCode("CI")
                .build())
                .isInstanceOf(PiSpiException.class);
    }

    @Test
    @DisplayName("Should reject an amount longer than 13 digits")
    void amountOver13DigitsThrowsPiSpiException() {
        assertThatThrownBy(() -> new PiSpiQrCodeBuilder()
                .alias(ALIAS)
                .countryCode("CI")
                .merchantChannel(MerchantChannel.STATIC_MERCHANT)
                .amount(10_000_000_000_000L) // 14 digits
                .build())
                .isInstanceOf(PiSpiException.class);
    }

    @Test
    @DisplayName("Should reject a negative amount")
    void negativeAmountThrowsPiSpiException() {
        assertThatThrownBy(() -> new PiSpiQrCodeBuilder()
                .alias(ALIAS)
                .countryCode("CI")
                .merchantChannel(MerchantChannel.STATIC_MERCHANT)
                .amount(-1)
                .build())
                .isInstanceOf(PiSpiException.class);
    }

    @Test
    @DisplayName("Should reject a reference label longer than 25 characters")
    void referenceLabelOver25CharsThrowsPiSpiException() {
        String tooLong = String.join("", Collections.nCopies(26, "A"));

        assertThatThrownBy(() -> new PiSpiQrCodeBuilder()
                .alias(ALIAS)
                .countryCode("CI")
                .merchantChannel(MerchantChannel.STATIC_MERCHANT)
                .referenceLabel(tooLong)
                .build())
                .isInstanceOf(PiSpiException.class);
    }

    @Test
    @DisplayName("Should accept a merchant channel code not yet documented, for forward compatibility")
    void unknownMerchantChannelCodeIsAcceptedNotRejected() {
        String payload = new PiSpiQrCodeBuilder()
                .alias(ALIAS)
                .countryCode("CI")
                .merchantChannel(MerchantChannel.of("999"))
                .build();

        assertThat(payload).contains("999");
    }
}
