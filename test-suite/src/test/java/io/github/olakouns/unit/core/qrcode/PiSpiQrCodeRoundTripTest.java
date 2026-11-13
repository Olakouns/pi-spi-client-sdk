package io.github.olakouns.unit.core.qrcode;

import io.github.olakouns.qrcode.MerchantChannel;
import io.github.olakouns.qrcode.PiSpiQrCodeBuilder;
import io.github.olakouns.qrcode.PiSpiQrCodeParser;
import io.github.olakouns.qrcode.PiSpiQrPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
@DisplayName("PiSpiQrCode build+parse round-trip Tests")
class PiSpiQrCodeRoundTripTest {

    @Test
    @DisplayName("Should round-trip a static merchant QR (fixture 1) through build and parse")
    void fixture1BuildThenParseRoundTrips() {
        String payload = new PiSpiQrCodeBuilder()
                .alias("111c3e1b-4312-49ec-b75e-4c8c74c10fd7")
                .countryCode("CI")
                .merchantChannel(MerchantChannel.STATIC_MERCHANT)
                .referenceLabel("X2AB528754YY2022")
                .build();

        PiSpiQrPayload parsed = PiSpiQrCodeParser.parse(payload);

        assertThat(parsed.rawPayload()).isEqualTo(payload);
        assertThat(parsed.alias()).isEqualTo("111c3e1b-4312-49ec-b75e-4c8c74c10fd7");
        assertThat(parsed.countryCode()).isEqualTo("CI");
        assertThat(parsed.merchantChannel()).isEqualTo(MerchantChannel.STATIC_MERCHANT);
        assertThat(parsed.referenceLabel()).isEqualTo("X2AB528754YY2022");
        assertThat(parsed.amount()).isNull();
    }

    @Test
    @DisplayName("Should round-trip a dynamic merchant QR with an amount (fixture 2) through build and parse")
    void fixture2BuildThenParseRoundTrips() {
        String payload = new PiSpiQrCodeBuilder()
                .alias("111c3e1b-4312-49ec-b75e-4c8c74c10fd7")
                .countryCode("CI")
                .merchantChannel(MerchantChannel.DYNAMIC_MERCHANT)
                .amount(1000)
                .referenceLabel("F2YG7643D5HH2023")
                .build();
        assertThat(payload).endsWith("2E79");

        PiSpiQrPayload parsed = PiSpiQrCodeParser.parse(payload);

        assertThat(parsed.alias()).isEqualTo("111c3e1b-4312-49ec-b75e-4c8c74c10fd7");
        assertThat(parsed.countryCode()).isEqualTo("CI");
        assertThat(parsed.merchantChannel()).isEqualTo(MerchantChannel.DYNAMIC_MERCHANT);
        assertThat(parsed.amount()).isEqualTo(1000L);
        assertThat(parsed.referenceLabel()).isEqualTo("F2YG7643D5HH2023");
    }

    @Test
    @DisplayName("Should round-trip a transfer QR (fixture 3) through build and parse")
    void fixture3BuildThenParseRoundTrips() {
        String payload = new PiSpiQrCodeBuilder()
                .alias("341c3e1b-4312-49gc-b75e-4i8c74c60hJ2")
                .countryCode("TG")
                .merchantChannel(MerchantChannel.STATIC_INDIVIDUAL)
                .build();

        PiSpiQrPayload parsed = PiSpiQrCodeParser.parse(payload);

        assertThat(parsed.alias()).isEqualTo("341c3e1b-4312-49gc-b75e-4i8c74c60hJ2");
        assertThat(parsed.countryCode()).isEqualTo("TG");
        assertThat(parsed.merchantChannel()).isEqualTo(MerchantChannel.STATIC_INDIVIDUAL);
        assertThat(parsed.referenceLabel()).isNull();
        assertThat(parsed.amount()).isNull();
    }
}
