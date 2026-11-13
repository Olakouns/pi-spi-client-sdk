package io.github.olakouns.unit.core.qrcode;

import io.github.olakouns.qrcode.PiCountry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
@DisplayName("PiCountry Tests")
class PiCountryTest {

    @Test
    @DisplayName("Should expose the documented code for each UEMOA constant")
    void knownConstantsExposeDocumentedCodes() {
        assertThat(PiCountry.BJ.code()).isEqualTo("BJ");
        assertThat(PiCountry.BF.code()).isEqualTo("BF");
        assertThat(PiCountry.CI.code()).isEqualTo("CI");
        assertThat(PiCountry.GW.code()).isEqualTo("GW");
        assertThat(PiCountry.ML.code()).isEqualTo("ML");
        assertThat(PiCountry.NE.code()).isEqualTo("NE");
        assertThat(PiCountry.SN.code()).isEqualTo("SN");
        assertThat(PiCountry.TG.code()).isEqualTo("TG");
    }

    @Test
    @DisplayName("Should return the matching known constant when resolving a known code")
    void ofReturnsTheMatchingKnownConstant() {
        assertThat(PiCountry.of("CI")).isEqualTo(PiCountry.CI);
        assertThat(PiCountry.of("TG")).isEqualTo(PiCountry.TG);
    }

    @Test
    @DisplayName("Should wrap an unknown code instead of throwing, for forward compatibility")
    void ofWrapsAnUnknownCodeInsteadOfThrowing() {
        PiCountry future = PiCountry.of("FR");

        assertThat(future.code()).isEqualTo("FR");
    }

    @Test
    @DisplayName("Should consider two countries with the same code equal")
    void equalityIsCodeBased() {
        assertThat(PiCountry.of("CI")).isEqualTo(PiCountry.CI);
        assertThat(PiCountry.of("CI").hashCode()).isEqualTo(PiCountry.CI.hashCode());
        assertThat(PiCountry.of("FR")).isEqualTo(PiCountry.of("FR"));
    }

    @Test
    @DisplayName("Should consider two countries with different codes unequal")
    void differentCodesAreNotEqual() {
        assertThat(PiCountry.CI).isNotEqualTo(PiCountry.TG);
    }

    @Test
    @DisplayName("Should report the declared UEMOA constants as known")
    void declaredConstantsAreKnown() {
        assertThat(PiCountry.CI.isKnown()).isTrue();
        assertThat(PiCountry.TG.isKnown()).isTrue();
    }

    @Test
    @DisplayName("Should report a country resolved from an undocumented code as not known")
    void countryFromUnknownCodeIsNotKnown() {
        assertThat(PiCountry.of("FR").isKnown()).isFalse();
    }

    @Test
    @DisplayName("Should list exactly the eight UEMOA codes")
    void knownCodesListsDocumentedValues() {
        assertThat(PiCountry.knownCodes())
                .containsExactlyInAnyOrder("BJ", "BF", "CI", "GW", "ML", "NE", "SN", "TG");
    }
}
