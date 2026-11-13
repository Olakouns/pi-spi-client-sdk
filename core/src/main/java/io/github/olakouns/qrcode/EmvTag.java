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

/**
 * EMV tag identifiers for the PI-SPI interoperable QR code payload, mirroring the field table in
 * {@code docs/superpowers/specs/2026-11-11-qrcode-design.md} (itself taken verbatim from BCEAO's
 * {@code qr-specifications} guide). Grouping sub-fields as nested classes under their parent tag
 * keeps this class readable as the same table the spec documents, rather than a flat list of
 * unrelated constants.
 *
 * @author Razacki KOUNASSO
 * @since 1.0.0
 */
final class EmvTag {

    private EmvTag() {
    }

    static final String PAYLOAD_FORMAT_INDICATOR = "00";
    static final String MERCHANT_ACCOUNT_INFORMATION = "36";
    static final String MERCHANT_CATEGORY_CODE = "52";
    static final String TRANSACTION_CURRENCY = "53";
    static final String TRANSACTION_AMOUNT = "54";
    static final String COUNTRY_CODE = "58";
    static final String MERCHANT_NAME = "59";
    static final String MERCHANT_CITY = "60";
    static final String ADDITIONAL_DATA_FIELD_TEMPLATE = "62";
    static final String CRC = "63";
    static final int CRC_VALUE_LENGTH = 4;

    /** Sub-fields of tag {@link #MERCHANT_ACCOUNT_INFORMATION} ({@code 36}). */
    static final class MerchantAccountInformation {
        private MerchantAccountInformation() {
        }

        static final String GUI = "00";
        static final String ALIAS = "01";
    }

    /** Sub-fields of tag {@link #ADDITIONAL_DATA_FIELD_TEMPLATE} ({@code 62}). */
    static final class AdditionalData {
        private AdditionalData() {
        }

        static final String REFERENCE_LABEL = "05";
        static final String MERCHANT_CHANNEL = "11";
    }
}
