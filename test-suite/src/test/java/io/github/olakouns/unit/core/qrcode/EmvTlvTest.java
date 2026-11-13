package io.github.olakouns.unit.core.qrcode;

import io.github.olakouns.exception.PiSpiException;
import io.github.olakouns.qrcode.EmvTlv;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;

@Tag("unit")
@DisplayName("EmvTlv Tests")
class EmvTlvTest {

    @Test
    @DisplayName("Should encode a tag/value pair as ID + 2-digit length + value")
    void encodeProducesIdLengthValue() {
        assertThat(EmvTlv.encode("00", "01")).isEqualTo("000201");
    }

    @Test
    @DisplayName("Should encode a longer value with its exact 2-digit length")
    void encodeHandlesTwoDigitLengths() {
        assertThat(EmvTlv.encode("36", "int.bceao.pi")).isEqualTo("3612int.bceao.pi");
    }

    @Test
    @DisplayName("Should reject a value longer than 99 characters (EMV 2-digit length ceiling)")
    void encodeRejectsValueLongerThan99Chars() {
        String tooLong = String.join("", Collections.nCopies(100, "a"));

        assertThatThrownBy(() -> EmvTlv.encode("36", tooLong))
                .isInstanceOf(PiSpiException.class);
    }

    @Test
    @DisplayName("Should decode multiple concatenated segments in order")
    void decodeParsesMultipleSegmentsInOrder() {
        Map<String, String> result = EmvTlv.decode("0002015802CI");

        assertThat(result).containsExactly(entry("00", "01"), entry("58", "CI"));
    }

    @Test
    @DisplayName("Should throw when a declared length runs past the end of the input")
    void decodeThrowsOnTruncatedSegment() {
        // tag "58", declared length 10, but only 2 chars ("CI") actually follow
        assertThatThrownBy(() -> EmvTlv.decode("5810CI"))
                .isInstanceOf(PiSpiException.class);
    }

    @Test
    @DisplayName("Should throw when fewer than 4 characters remain for a new tag and length")
    void decodeThrowsWhenNotEnoughCharactersRemainForATagAndLength() {
        // one complete segment ("000201") followed by a 1-character dangling remainder,
        // too short to even contain a tag + length header
        assertThatThrownBy(() -> EmvTlv.decode("0002013"))
                .isInstanceOf(PiSpiException.class);
    }

    @Test
    @DisplayName("Should throw when the length field is not numeric")
    void decodeThrowsOnNonNumericLength() {
        assertThatThrownBy(() -> EmvTlv.decode("00XX01"))
                .isInstanceOf(PiSpiException.class);
    }

    @Test
    @DisplayName("Should round-trip an encoded value back through decode")
    void encodeThenDecodeRoundTrips() {
        String encoded = EmvTlv.encode("01", "111c3e1b-4312-49ec-b75e-4c8c74c10fd7");

        Map<String, String> decoded = EmvTlv.decode(encoded);

        assertThat(decoded).containsExactly(entry("01", "111c3e1b-4312-49ec-b75e-4c8c74c10fd7"));
    }
}
