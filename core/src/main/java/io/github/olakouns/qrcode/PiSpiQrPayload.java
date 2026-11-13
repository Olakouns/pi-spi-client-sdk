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

/**
 * The fields extracted from a validated PI-SPI interoperable EMV QR payload, as produced by
 * {@link PiSpiQrCodeParser#parse(String)}.
 *
 * @author Razacki KOUNASSO
 * @since 1.0.1
 */
public final class PiSpiQrPayload {

    private final String alias;
    private final PiCountry countryCode;
    private final MerchantChannel merchantChannel;
    private final Long amount;
    private final String referenceLabel;
    private final String rawPayload;

    PiSpiQrPayload(String alias, PiCountry countryCode, MerchantChannel merchantChannel, Long amount,
                   String referenceLabel, String rawPayload) {
        this.alias = alias;
        this.countryCode = countryCode;
        this.merchantChannel = merchantChannel;
        this.amount = amount;
        this.referenceLabel = referenceLabel;
        this.rawPayload = rawPayload;
    }

    /** @return the alias (account proxy / payment address) of the payee */
    public String alias() {
        return alias;
    }

    /** @return the ISO 3166-1 alpha-2 country code of the payee's institution */
    public PiCountry countryCode() {
        return countryCode;
    }

    /** @return the merchant channel (EMV sub-field {@code 62-11}) */
    public MerchantChannel merchantChannel() {
        return merchantChannel;
    }

    /** @return the transaction amount, or {@code null} if the payload does not carry one */
    public Long amount() {
        return amount;
    }

    /** @return the transaction reference / TxID, or {@code null} if the payload does not carry one */
    public String referenceLabel() {
        return referenceLabel;
    }

    /** @return the original, unparsed payload string this instance was extracted from */
    public String rawPayload() {
        return rawPayload;
    }
}
