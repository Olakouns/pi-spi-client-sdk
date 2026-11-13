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

import java.util.Objects;

/**
 * Shared base for this package's typesafe constant classes ({@link MerchantChannel},
 * {@link PiCountry}): a raw EMV field value, plus whether it is one of the subclass's declared
 * constants ({@code documented}) or was built from a code not yet documented by BCEAO via that
 * subclass's own {@code of(String)} factory.
 * <p>
 * Each subclass still owns its own static registry (constants, {@code of(String)},
 * {@code knownCodes()}), since those return the subclass's own type — only the per-instance state
 * and behavior are shared here.
 *
 * @author Razacki KOUNASSO
 * @since 1.0.1
 */
abstract class PiSpiCode {

    private final String code;
    private final boolean documented;

    protected PiSpiCode(String code, boolean documented) {
        this.code = code;
        this.documented = documented;
    }

    /**
     * @return the raw EMV field value
     */
    public String code() {
        return code;
    }

    /**
     * @return {@code true} if this instance is one of the constants declared on its subclass,
     *         {@code false} if it was built from a code not currently documented by BCEAO
     */
    public boolean isKnown() {
        return documented;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return code.equals(((PiSpiCode) o).code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + code + '}';
    }
}
