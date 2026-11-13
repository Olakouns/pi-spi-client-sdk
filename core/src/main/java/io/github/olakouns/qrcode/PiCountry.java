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
import java.util.Set;

/**
 * The ISO 3166-1 alpha-2 country code of a PI-SPI participant's institution (EMV field
 * {@code 58}), restricted to the UEMOA zone.
 * <p>
 * Modeled as a typesafe constant class rather than a Java {@code enum} so that a country code
 * BCEAO adds to the union in the future can still be represented via {@link #of(String)} without
 * requiring a new release of this SDK.
 *
 * @author Razacki KOUNASSO
 * @since 1.0.1
 */
public final class PiCountry extends PiSpiCode {

    private static final Map<String, PiCountry> KNOWN = new HashMap<>();

    /** Bénin. */
    public static final PiCountry BJ = register("BJ");

    /** Burkina Faso. */
    public static final PiCountry BF = register("BF");

    /** Côte d'Ivoire. */
    public static final PiCountry CI = register("CI");

    /** Guinée-Bissau. */
    public static final PiCountry GW = register("GW");

    /** Mali. */
    public static final PiCountry ML = register("ML");

    /** Niger. */
    public static final PiCountry NE = register("NE");

    /** Sénégal. */
    public static final PiCountry SN = register("SN");

    /** Togo. */
    public static final PiCountry TG = register("TG");

    private PiCountry(String code, boolean documented) {
        super(code, documented);
    }

    private static PiCountry register(String code) {
        PiCountry country = new PiCountry(code, true);
        KNOWN.put(code, country);
        return country;
    }

    /**
     * Resolves an ISO 3166-1 alpha-2 country code to its known constant, or wraps it as-is if it
     * is not one of the UEMOA codes currently documented by BCEAO. The result's {@link #isKnown()}
     * reflects which of the two happened.
     *
     * @param code the raw EMV field {@code 58} value
     * @return the matching known constant, or a new, not-yet-known instance wrapping {@code code}
     */
    public static PiCountry of(String code) {
        PiCountry match = KNOWN.get(code);
        return match != null ? match : new PiCountry(code, false);
    }

    /**
     * @return the ISO 3166-1 alpha-2 codes of the UEMOA countries currently documented by BCEAO
     */
    public static Set<String> knownCodes() {
        return Collections.unmodifiableSet(KNOWN.keySet());
    }
}
