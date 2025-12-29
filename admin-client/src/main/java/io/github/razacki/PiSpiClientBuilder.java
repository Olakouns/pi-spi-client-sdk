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

package io.github.razacki;

import io.github.razacki.constants.OAuth2Constants;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import java.util.ArrayList;
import java.util.List;

public class PiSpiClientBuilder {
    private String serverUrl;
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String apiKey;
    private List<String> scopes;

    private String clientCertPath;   // client-cert.pem
    private String clientKeyPath;

    private Client resteasyClient;
    private SSLContext sslContext;

    public PiSpiClientBuilder serverUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        return this;
    }

    public PiSpiClientBuilder clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public PiSpiClientBuilder clientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public PiSpiClientBuilder grantType(String grantType) {
        this.grantType = grantType;
        return this;
    }

    public PiSpiClientBuilder apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public PiSpiClientBuilder apiKey(List<String> scopes) {
        this.scopes = new ArrayList<>(scopes);
        return this;
    }

    public PiSpiClientBuilder clientCertPath(String clientCertPath) {
        this.clientCertPath = clientCertPath;
        return this;
    }

    public PiSpiClientBuilder clientKeyPath(String clientKeyPath) {
        this.clientKeyPath = clientKeyPath;
        return this;
    }

    /**
     * Custom instance of resteasy client.
     *
     * @param resteasyClient Custom RestEasy client
     * @return admin client builder
     */
    public PiSpiClientBuilder resteasyClient(Client resteasyClient) {
        this.resteasyClient = resteasyClient;
        return this;
    }


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
                        .clientCertPath(clientCertPath)
                        .clientKeyPath(clientKeyPath)
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
     * Returns a new Keycloak builder.
     */
    public static PiSpiClientBuilder builder() {
        return new PiSpiClientBuilder();
    }

}
