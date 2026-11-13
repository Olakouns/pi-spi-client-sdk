package io.github.olakouns.unit.core.qrcode;

import io.github.olakouns.qrcode.MerchantChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
@DisplayName("MerchantChannel Tests")
class MerchantChannelTest {

    @Test
    @DisplayName("Should expose the documented code for each known constant")
    void knownConstantsExposeDocumentedCodes() {
        assertThat(MerchantChannel.STATIC_MERCHANT.code()).isEqualTo("000");
        assertThat(MerchantChannel.DYNAMIC_MERCHANT.code()).isEqualTo("400");
        assertThat(MerchantChannel.STATIC_INDIVIDUAL.code()).isEqualTo("731");
    }

    @Test
    @DisplayName("Should return the matching known constant when resolving a known code")
    void ofReturnsTheMatchingKnownConstant() {
        assertThat(MerchantChannel.of("400")).isEqualTo(MerchantChannel.DYNAMIC_MERCHANT);
        assertThat(MerchantChannel.of("000")).isEqualTo(MerchantChannel.STATIC_MERCHANT);
        assertThat(MerchantChannel.of("731")).isEqualTo(MerchantChannel.STATIC_INDIVIDUAL);
    }

    @Test
    @DisplayName("Should wrap an unknown code instead of throwing, for forward compatibility")
    void ofWrapsAnUnknownCodeInsteadOfThrowing() {
        MerchantChannel future = MerchantChannel.of("999");

        assertThat(future.code()).isEqualTo("999");
    }

    @Test
    @DisplayName("Should consider two channels with the same code equal")
    void equalityIsCodeBased() {
        assertThat(MerchantChannel.of("000")).isEqualTo(MerchantChannel.STATIC_MERCHANT);
        assertThat(MerchantChannel.of("000").hashCode()).isEqualTo(MerchantChannel.STATIC_MERCHANT.hashCode());
        assertThat(MerchantChannel.of("999")).isEqualTo(MerchantChannel.of("999"));
    }

    @Test
    @DisplayName("Should consider two channels with different codes unequal")
    void differentCodesAreNotEqual() {
        assertThat(MerchantChannel.STATIC_MERCHANT).isNotEqualTo(MerchantChannel.DYNAMIC_MERCHANT);
    }
}
