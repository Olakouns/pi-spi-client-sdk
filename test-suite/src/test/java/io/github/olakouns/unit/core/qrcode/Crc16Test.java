package io.github.olakouns.unit.core.qrcode;

import io.github.olakouns.qrcode.Crc16;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
@DisplayName("Crc16 Tests")
class Crc16Test {

    @Test
    @DisplayName("Should compute the CRC documented for BCEAO fixture 1 (static merchant)")
    void fixture1PrefixProducesDocumentedCrc() {
        String prefix = "00020136560012int.bceao.pi0136111c3e1b-4312-49ec-b75e-4c8c74c10fd7" +
                "5204000053039525802CI5901X6001X62270516X2AB528754YY20221103000" +
                "6304";

        assertThat(Crc16.compute(prefix)).isEqualTo("255B");
    }

    @Test
    @DisplayName("Should compute the CRC documented for BCEAO fixture 3 (transfer QR)")
    void fixture3PrefixProducesDocumentedCrc() {
        String prefix = "00020136560012int.bceao.pi0136341c3e1b-4312-49gc-b75e-4i8c74c60hJ2" +
                "5204000053039525802TG5901X6001X62071103731" +
                "6304";

        assertThat(Crc16.compute(prefix)).isEqualTo("4448");
    }

    @Test
    @DisplayName("Should produce a different CRC for a different input")
    void differentInputProducesDifferentCrc() {
        assertThat(Crc16.compute("A6304")).isNotEqualTo(Crc16.compute("B6304"));
    }

    @Test
    @DisplayName("Should always return exactly 4 uppercase hexadecimal characters")
    void alwaysReturnsFourUppercaseHexChars() {
        assertThat(Crc16.compute("000201")).matches("[0-9A-F]{4}");
    }
}
