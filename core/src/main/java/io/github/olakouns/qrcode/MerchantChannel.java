/*
 * Copyright 2026 Razacki KOUNASSO
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.olakouns.qrcode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The PI-SPI merchant channel (EMV sub-field {@code 62-11}), identifying both the QR type
 * (static/dynamic) and the payee's legal nature.
 * <p>
 * Modeled as a typesafe constant class rather than a Java {@code enum} so that a merchant
 * channel value BCEAO introduces in the future can still be represented via {@link #of(String)}
 * without requiring a new release of this SDK.
 *
 * @author Razacki KOUNASSO
 * @since 1.0.1
 */
public final class MerchantChannel {

    private static final Map<String, MerchantChannel> KNOWN = new HashMap<>();

    /** Static QR, legal entity (merchant point-of-sale, reusable). */
    public static final MerchantChannel STATIC_MERCHANT = register("000");

    /** Dynamic QR, legal entity (merchant, single transaction). */
    public static final MerchantChannel DYNAMIC_MERCHANT = register("400");

    /** Static QR, natural person (person-to-person transfer). */
    public static final MerchantChannel STATIC_INDIVIDUAL = register("731");

    private final String code;
    private final boolean known;

    private MerchantChannel(String code, boolean known) {
        this.code = code;
        this.known = known;
    }

    private static MerchantChannel register(String code) {
        MerchantChannel channel = new MerchantChannel(code, true);
        KNOWN.put(code, channel);
        return channel;
    }

    /**
     * Resolves a merchant channel code to its known constant, or wraps it as-is if it is not one
     * of the values currently documented by BCEAO. The result's {@link #isKnown()} reflects which
     * of the two happened.
     *
     * @param code the raw EMV sub-field {@code 62-11} value
     * @return the matching known constant, or a new, not-yet-known instance wrapping {@code code}
     */
    public static MerchantChannel of(String code) {
        MerchantChannel known = KNOWN.get(code);
        return known != null ? known : new MerchantChannel(code, false);
    }

    /**
     * @return the EMV sub-field {@code 62-11} values currently documented by BCEAO
     */
    public static Set<String> knownCodes() {
        return Collections.unmodifiableSet(KNOWN.keySet());
    }

    /**
     * @return the raw EMV sub-field {@code 62-11} value
     */
    public String code() {
        return code;
    }

    /**
     * @return {@code true} if this instance is one of the constants declared on this class (e.g.
     *         {@link #STATIC_MERCHANT}), {@code false} if it was built by {@link #of(String)}
     *         from a code not currently documented by BCEAO
     */
    public boolean isKnown() {
        return known;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MerchantChannel)) {
            return false;
        }
        return code.equals(((MerchantChannel) o).code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }

    @Override
    public String toString() {
        return "MerchantChannel{" + code + '}';
    }
}
