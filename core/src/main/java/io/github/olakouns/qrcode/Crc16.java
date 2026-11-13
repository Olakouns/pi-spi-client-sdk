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

import java.nio.charset.StandardCharsets;

/**
 * CRC16-CCITT (polynomial {@code 0x1021}, initial value {@code 0xFFFF}) as required for the
 * checksum field (EMV tag {@code 63}) of a PI-SPI interoperable QR code payload.
 *
 * @author Razacki KOUNASSO
 * @since 1.0.1
 */
public final class Crc16 {

    private static final int POLYNOMIAL = 0x1021;
    private static final int INITIAL_VALUE = 0xFFFF;

    private Crc16() {
    }

    /**
     * Computes the CRC16-CCITT checksum of {@code data}, encoded as UTF-8 bytes per the BCEAO
     * PI-SPI specification.
     *
     * @param data the payload content to checksum (the full EMV payload up to and including the
     *             CRC field's own tag and length, but excluding the CRC value itself)
     * @return the 4-character uppercase hexadecimal representation of the 16-bit CRC
     */
    public static String compute(String data) {
        int crc = INITIAL_VALUE;
        for (byte b : data.getBytes(StandardCharsets.UTF_8)) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = ((crc << 1) ^ POLYNOMIAL) & 0xFFFF;
                } else {
                    crc = (crc << 1) & 0xFFFF;
                }
            }
        }
        return String.format("%04X", crc);
    }
}
