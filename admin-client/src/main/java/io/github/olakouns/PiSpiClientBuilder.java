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

package io.github.olakouns;

import io.github.olakouns.constants.OAuth2Constants;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import java.util.ArrayList;
import java.util.List;

public class PiSpiClientBuilder {
    /**
     * The base URL of the PI-SPI API server.
     */
    private String serverUrl;
    /**
     * The Client ID provided by the PI-SPI platform for OAuth2 authentication.
     */
    private String clientId;
    /**
     * The Client Secret provided by the PI-SPI platform.
     */
    private String clientSecret;
    /**
     * The OAuth2 grant type (e.g., "client_credentials"). If it's null, client_credentials are used by default.
     */
    private String grantType;
    /**
     * The API Key for additional security headers if required by the target environment.
     */
    private String apiKey;
    /**
     * The list of scopes requested for the access token.
     *
     * @see io.github.olakouns.constants.ScopeConstants
     */
    private List<String> scopes;


    /**
     * Custom SSL Context for secure connections.
     * <p>
     * If this property is provided <b>without</b> a custom {@link #resteasyClient},
     * a default Resteasy client will be automatically created and configured using this SSL context.
     * </p>
     */
    private SSLContext sslContext;

    /**
     * Custom Resteasy Client.
     * <p>
     * <b>Note:</b> If you provide a custom client, you are responsible for its full configuration,
     * including SSL settings and JacksonProvider. If both a custom client and a {@link #sslContext} are provided,
     * the custom client takes precedence and the SSL context property will be ignored.
     * </p>
     */
    private Client resteasyClient;

    /**
     * The base URL of the PI-SPI API server.
     */
    public PiSpiClientBuilder serverUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        return this;
    }

    /**
     * The Client ID provided by the PI-SPI platform for OAuth2 authentication.
     */
    public PiSpiClientBuilder clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    /**
     * The Client Secret provided by the PI-SPI platform.
     */
    public PiSpiClientBuilder clientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    /**
     * The OAuth2 grant type (e.g., "client_credentials"). If it's null, client_credentials are used by default.
     */
    public PiSpiClientBuilder grantType(String grantType) {
        this.grantType = grantType;
        return this;
    }

    /**
     * The API Key for additional security headers if required by the target environment.
     */
    public PiSpiClientBuilder apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    /**
     * The list of scopes requested for the access token.
     *
     * @see io.github.olakouns.constants.ScopeConstants
     */
    public PiSpiClientBuilder scopes(List<String> scopes) {
        this.scopes = new ArrayList<>(scopes);
        return this;
    }

    /**
     * Custom Resteasy Client.
     * <p>
     * <b>Note:</b> If you provide a custom client, you are responsible for its full configuration,
     * including SSL settings and JacksonProvider. If both a custom client and a {@link #sslContext} are provided,
     * the custom client takes precedence and the SSL context property will be ignored.
     * </p>
     */
    public PiSpiClientBuilder resteasyClient(Client resteasyClient) {
        this.resteasyClient = resteasyClient;
        return this;
    }


    /**
     * Custom SSL Context for secure connections.
     * <p>
     * If this property is provided <b>without</b> a custom {@link #resteasyClient},
     * a default Resteasy client will be automatically created and configured using this SSL context.
     * </p>
     */
    public PiSpiClientBuilder sslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }


    /**
     * Builds a new PiSpiClient client from this builder.
     */
    public PiSpiClient build() {
        if (serverUrl == null) {
            throw new IllegalStateException("serverUrl required");
        }

        if (grantType == null) {
            grantType = OAuth2Constants.CLIENT_CREDENTIALS;
        }

        if (grantType.equals(OAuth2Constants.CLIENT_CREDENTIALS) && clientSecret == null) {
            throw new IllegalStateException("clientSecret required for confidential client");
        }

        if (clientId == null) {
            throw new IllegalStateException("clientId required");
        }

        if (scopes != null) {
            for (String s : scopes) {
                if (s == null || s.isEmpty()) {
                    throw new IllegalStateException("Scopes cannot contain null or empty values");
                }
            }
        }

        return PiSpiClient.getInstance(
                BaseConfig.builder()
                        .serverUrl(serverUrl)
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .grantType(grantType)
                        .apiKey(apiKey)
                        .scopes(scopes)
                        .build(),
                resteasyClient,
                false,
                sslContext,
                null,
                null
        );
    }

    private PiSpiClientBuilder() {
    }

    /**
     * Returns a new PiSpiClient builder.
     */
    public static PiSpiClientBuilder builder() {
        return new PiSpiClientBuilder();
    }

}
