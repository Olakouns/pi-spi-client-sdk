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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Encoding and decoding of EMV TLV (Tag-Length-Value) data objects, as used throughout the
 * PI-SPI interoperable QR code payload: each field is {@code ID (2 digits) + length (2 digits)
 * + value}.
 *
 * @author Razacki KOUNASSO
 * @since 1.0.0
 */
public final class EmvTlv {

    private static final int MAX_VALUE_LENGTH = 99;

    private EmvTlv() {
    }

    /**
     * Encodes a single EMV data object: {@code tag + 2-digit zero-padded length + value}.
     *
     * @param tag   the 2-character EMV tag identifier
     * @param value the field value; must not exceed {@value #MAX_VALUE_LENGTH} characters, since
     *              EMV fixes the length field at exactly 2 digits
     * @return the encoded data object
     * @throws PiSpiException if {@code value} is longer than {@value #MAX_VALUE_LENGTH} characters
     */
    public static String encode(String tag, String value) {
        if (value.length() > MAX_VALUE_LENGTH) {
            throw new PiSpiException("EMV value for tag " + tag + " exceeds the maximum length of "
                    + MAX_VALUE_LENGTH + " characters");
        }
        return tag + String.format("%02d", value.length()) + value;
    }

    /**
     * Decodes a concatenated sequence of EMV data objects into an ordered map of tag to value.
     *
     * @param data the TLV-encoded content to decode
     * @return the decoded tag/value pairs, in the order they appear in {@code data}
     * @throws PiSpiException if {@code data} is truncated or contains a non-numeric length field
     */
    public static Map<String, String> decode(String data) {
        Map<String, String> segments = new LinkedHashMap<>();
        int cursor = 0;

        while (cursor < data.length()) {
            if (cursor + 4 > data.length()) {
                throw new PiSpiException("Truncated EMV data object at position " + cursor);
            }
            String tag = data.substring(cursor, cursor + 2);
            String lengthField = data.substring(cursor + 2, cursor + 4);
            int length;
            try {
                length = Integer.parseInt(lengthField);
            } catch (NumberFormatException e) {
                throw new PiSpiException("Non-numeric EMV length for tag " + tag + ": " + lengthField, e);
            }

            int valueStart = cursor + 4;
            int valueEnd = valueStart + length;
            if (valueEnd > data.length()) {
                throw new PiSpiException("Truncated EMV value for tag " + tag);
            }

            segments.put(tag, data.substring(valueStart, valueEnd));
            cursor = valueEnd;
        }

        return segments;
    }
}
