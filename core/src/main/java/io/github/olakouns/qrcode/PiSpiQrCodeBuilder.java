/*
 * Copyright 2025 Razacki KOUNASSO
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

import io.github.olakouns.exception.PiSpiException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Fluent builder for the PI-SPI interoperable EMV QR code payload (BCEAO), covering both static
 * and dynamic merchant QR codes as well as person-to-person transfer QR codes.
 * <p>
 * Merchant Category Code, Transaction Currency, Merchant Name and Merchant City are not
 * configurable: BCEAO fixes these values by design, as part of the "no personal data" structural
 * choice documented for this QR format (see
 * {@code docs/superpowers/specs/2026-11-11-qrcode-design.md}).
 *
 * @author Razacki KOUNASSO
 * @since 1.0.0
 */
public class PiSpiQrCodeBuilder {

    private static final String PAYLOAD_FORMAT_INDICATOR_VALUE = "01";
    private static final String GUI_VALUE = "int.bceao.pi";
    private static final String MERCHANT_CATEGORY_CODE_VALUE = "0000";
    private static final String TRANSACTION_CURRENCY_VALUE = "952";
    private static final String MERCHANT_NAME_VALUE = "X";
    private static final String MERCHANT_CITY_VALUE = "X";

    private static final int MAX_ALIAS_LENGTH = 36;
    private static final int MAX_AMOUNT_DIGITS = 13;
    private static final int MAX_REFERENCE_LABEL_LENGTH = 25;

    private static final Set<String> UEMOA_COUNTRY_CODES = new HashSet<>(
            Arrays.asList("BJ", "BF", "CI", "GW", "ML", "NE", "SN", "TG"));

    private String alias;
    private String countryCode;
    private MerchantChannel merchantChannel;
    private Long amount;
    private String referenceLabel;

    /**
     * The PI-SPI alias (account proxy / payment address) of the payee.
     *
     * @param alias the alias, at most {@value #MAX_ALIAS_LENGTH} characters
     * @return this builder
     */
    public PiSpiQrCodeBuilder alias(String alias) {
        this.alias = alias;
        return this;
    }

    /**
     * The ISO 3166-1 alpha-2 country code of the payee's institution, restricted to the UEMOA
     * zone.
     *
     * @param countryCode the country code (e.g. {@code "CI"})
     * @return this builder
     */
    public PiSpiQrCodeBuilder countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    /**
     * The merchant channel (EMV sub-field {@code 62-11}).
     *
     * @param merchantChannel the merchant channel
     * @return this builder
     */
    public PiSpiQrCodeBuilder merchantChannel(MerchantChannel merchantChannel) {
        this.merchantChannel = merchantChannel;
        return this;
    }

    /**
     * The transaction amount, in the smallest unit with no separator (e.g. {@code 1000} for
     * 1000 XOF). Optional.
     *
     * @param amount a non-negative amount of at most {@value #MAX_AMOUNT_DIGITS} digits
     * @return this builder
     */
    public PiSpiQrCodeBuilder amount(long amount) {
        this.amount = amount;
        return this;
    }

    /**
     * The transaction reference / TxID (EMV sub-field {@code 62-05}). Optional.
     *
     * @param referenceLabel the reference label, at most {@value #MAX_REFERENCE_LABEL_LENGTH}
     *                       characters
     * @return this builder
     */
    public PiSpiQrCodeBuilder referenceLabel(String referenceLabel) {
        this.referenceLabel = referenceLabel;
        return this;
    }

    /**
     * Builds the complete PI-SPI EMV QR payload, CRC included.
     *
     * @return the encoded payload
     * @throws PiSpiException if a required field is missing, or any field violates its
     *                        documented constraint
     */
    public String build() {
        validate();

        StringBuilder payload = new StringBuilder();
        payload.append(EmvTlv.encode(EmvTag.PAYLOAD_FORMAT_INDICATOR, PAYLOAD_FORMAT_INDICATOR_VALUE));

        String merchantAccountInformation =
                EmvTlv.encode(EmvTag.MerchantAccountInformation.GUI, GUI_VALUE)
                        + EmvTlv.encode(EmvTag.MerchantAccountInformation.ALIAS, alias);
        payload.append(EmvTlv.encode(EmvTag.MERCHANT_ACCOUNT_INFORMATION, merchantAccountInformation));

        payload.append(EmvTlv.encode(EmvTag.MERCHANT_CATEGORY_CODE, MERCHANT_CATEGORY_CODE_VALUE));
        payload.append(EmvTlv.encode(EmvTag.TRANSACTION_CURRENCY, TRANSACTION_CURRENCY_VALUE));

        if (amount != null) {
            payload.append(EmvTlv.encode(EmvTag.TRANSACTION_AMOUNT, String.valueOf(amount)));
        }

        payload.append(EmvTlv.encode(EmvTag.COUNTRY_CODE, countryCode));
        payload.append(EmvTlv.encode(EmvTag.MERCHANT_NAME, MERCHANT_NAME_VALUE));
        payload.append(EmvTlv.encode(EmvTag.MERCHANT_CITY, MERCHANT_CITY_VALUE));

        StringBuilder additionalData = new StringBuilder();
        if (referenceLabel != null) {
            additionalData.append(EmvTlv.encode(EmvTag.AdditionalData.REFERENCE_LABEL, referenceLabel));
        }
        additionalData.append(EmvTlv.encode(EmvTag.AdditionalData.MERCHANT_CHANNEL, merchantChannel.code()));
        payload.append(EmvTlv.encode(EmvTag.ADDITIONAL_DATA_FIELD_TEMPLATE, additionalData.toString()));

        payload.append(EmvTag.CRC).append(String.format("%02d", EmvTag.CRC_VALUE_LENGTH));
        payload.append(Crc16.compute(payload.toString()));

        return payload.toString();
    }

    private void validate() {
        if (alias == null || alias.trim().isEmpty()) {
            throw new PiSpiException("alias is required");
        }
        if (alias.length() > MAX_ALIAS_LENGTH) {
            throw new PiSpiException("alias must not exceed " + MAX_ALIAS_LENGTH + " characters");
        }
        if (countryCode == null || !UEMOA_COUNTRY_CODES.contains(countryCode)) {
            throw new PiSpiException("countryCode must be one of the UEMOA ISO 3166-1 alpha-2 codes: " + UEMOA_COUNTRY_CODES);
        }
        if (merchantChannel == null) {
            throw new PiSpiException("merchantChannel is required");
        }
        if (amount != null) {
            if (amount < 0) {
                throw new PiSpiException("amount must not be negative");
            }
            if (String.valueOf(amount).length() > MAX_AMOUNT_DIGITS) {
                throw new PiSpiException("amount must not exceed " + MAX_AMOUNT_DIGITS + " digits");
            }
        }
        if (referenceLabel != null && referenceLabel.length() > MAX_REFERENCE_LABEL_LENGTH) {
            throw new PiSpiException("referenceLabel must not exceed " + MAX_REFERENCE_LABEL_LENGTH + " characters");
        }
    }
}
