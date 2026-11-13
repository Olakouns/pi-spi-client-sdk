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

import java.util.Collections;
import java.util.Map;

/**
 * Decodes and validates a PI-SPI interoperable EMV QR payload (BCEAO).
 * <p>
 * Per BCEAO's specification, the GUI (EMV sub-field {@code 36-00}) is checked first, then the
 * CRC (EMV tag {@code 63}) — both must be valid before any field is extracted.
 *
 * @author Razacki KOUNASSO
 * @since 1.0.0
 */
public final class PiSpiQrCodeParser {

    private static final String EXPECTED_GUI = "int.bceao.pi";

    private PiSpiQrCodeParser() {
    }

    /**
     * Parses and validates a PI-SPI EMV QR payload.
     *
     * @param payload the raw payload string
     * @return the extracted fields
     * @throws PiSpiException if the payload is malformed, its GUI does not match
     *                        {@code int.bceao.pi}, or its CRC does not match the recomputed value
     */
    public static PiSpiQrPayload parse(String payload) {
        Map<String, String> segments = EmvTlv.decode(payload);

        String merchantAccountInformation = segments.get(EmvTag.MERCHANT_ACCOUNT_INFORMATION);
        if (merchantAccountInformation == null) {
            throw new PiSpiException("Missing Merchant Account Information (tag "
                    + EmvTag.MERCHANT_ACCOUNT_INFORMATION + ")");
        }
        Map<String, String> merchantAccountSubFields = EmvTlv.decode(merchantAccountInformation);

        String gui = merchantAccountSubFields.get(EmvTag.MerchantAccountInformation.GUI);
        if (!EXPECTED_GUI.equals(gui)) {
            throw new PiSpiException("GUI mismatch: expected \"" + EXPECTED_GUI + "\", got \"" + gui + "\"");
        }

        String crcInput = payload.substring(0, payload.length() - EmvTag.CRC_VALUE_LENGTH);
        String actualCrc = payload.substring(payload.length() - EmvTag.CRC_VALUE_LENGTH);
        String expectedCrc = Crc16.compute(crcInput);
        if (!expectedCrc.equals(actualCrc)) {
            throw new PiSpiException("CRC mismatch: expected \"" + expectedCrc + "\", got \"" + actualCrc + "\"");
        }

        String alias = merchantAccountSubFields.get(EmvTag.MerchantAccountInformation.ALIAS);
        if (alias == null) {
            throw new PiSpiException("Missing alias (tag " + EmvTag.MERCHANT_ACCOUNT_INFORMATION
                    + "-" + EmvTag.MerchantAccountInformation.ALIAS + ")");
        }

        String countryCode = segments.get(EmvTag.COUNTRY_CODE);
        if (countryCode == null) {
            throw new PiSpiException("Missing country code (tag " + EmvTag.COUNTRY_CODE + ")");
        }

        String additionalDataField = segments.get(EmvTag.ADDITIONAL_DATA_FIELD_TEMPLATE);
        Map<String, String> additionalDataSubFields = additionalDataField != null
                ? EmvTlv.decode(additionalDataField)
                : Collections.emptyMap();

        String merchantChannelCode = additionalDataSubFields.get(EmvTag.AdditionalData.MERCHANT_CHANNEL);
        if (merchantChannelCode == null) {
            throw new PiSpiException("Missing merchant channel (tag " + EmvTag.ADDITIONAL_DATA_FIELD_TEMPLATE
                    + "-" + EmvTag.AdditionalData.MERCHANT_CHANNEL + ")");
        }
        MerchantChannel merchantChannel = MerchantChannel.of(merchantChannelCode);
        String referenceLabel = additionalDataSubFields.get(EmvTag.AdditionalData.REFERENCE_LABEL);

        Long amount = null;
        String amountField = segments.get(EmvTag.TRANSACTION_AMOUNT);
        if (amountField != null) {
            try {
                amount = Long.valueOf(amountField);
            } catch (NumberFormatException e) {
                throw new PiSpiException("Non-numeric transaction amount (tag "
                        + EmvTag.TRANSACTION_AMOUNT + "): " + amountField, e);
            }
        }

        return new PiSpiQrPayload(alias, countryCode, merchantChannel, amount, referenceLabel, payload);
    }
}
