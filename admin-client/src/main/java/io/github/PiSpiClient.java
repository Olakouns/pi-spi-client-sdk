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

package io.github;

import io.github.constants.OAuth2Constants;
import io.github.filter.ApiKeyFilter;
import io.github.filter.BearerAuthFilter;
import io.github.provider.ResteasyClientProvider;
import io.github.resource.ApiResource;
import io.github.resource.ComptesResource;
import io.github.resource.ComptesResourceWrapper;
import io.github.token.TokenManager;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.WebTarget;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class PiSpiClient implements AutoCloseable {

    private static ResteasyClientProvider clientProvider = resolveResteasyClientProvider();

    private static synchronized ResteasyClientProvider resolveResteasyClientProvider() {
        Iterator<ResteasyClientProvider> providers = ServiceLoader.load(ResteasyClientProvider.class).iterator();

        if (providers.hasNext()) {
            ResteasyClientProvider provider = providers.next();

            if (providers.hasNext()) {
                throw new IllegalArgumentException("Multiple " + ResteasyClientProvider.class + " implementations found");
            }

            return provider;
        }

        return createDefaultResteasyClientProvider();
    }

    private static ResteasyClientProvider createDefaultResteasyClientProvider() {
        try {
            return (ResteasyClientProvider) PiSpiClient.class.getClassLoader().loadClass("io.github.provider.ResteasyClientDefaultProvider").getDeclaredConstructor().newInstance();
        } catch (Exception cause) {
            throw new RuntimeException("Could not instantiate default client provider", cause);
        }
    }

    public static void setClientProvider(ResteasyClientProvider provider) {
        clientProvider = provider;
    }

    public static ResteasyClientProvider getClientProvider() {
        return clientProvider;
    }

    private static Client newRestClient(Object customJacksonProvider, SSLContext sslContext, boolean disableTrustManager) {
        return clientProvider.newRestClient(customJacksonProvider, sslContext, disableTrustManager);
    }

    private final BaseConfig config;
    private final WebTarget target;
    private final Client client;
    private boolean closed = false;
    private final String authToken;
    private final TokenManager tokenManager;
    private ComptesResourceWrapper comptesResourceWrapper;

    PiSpiClient(BaseConfig config, Client client, String authToken) {
        this.config = config;
        this.client = client;
        this.authToken = authToken;
        this.target = client.target(config.getServerUrl());
        tokenManager = authToken == null ? new TokenManager(config, client) : null;
        this.target.register(newAuthFilter());
        if (config.getApiKey() != null) {
            this.target.register(getAPiKeyFilter());
        }
    }

    private ClientRequestFilter newAuthFilter() {
        return authToken != null ? new BearerAuthFilter(authToken) : new BearerAuthFilter(tokenManager);
    }

    private ClientRequestFilter getAPiKeyFilter() {
        return new ApiKeyFilter(config.getApiKey());
    }


    /**
     * Creates a new instance of PiSpiClient with the provided configuration.
     * <p>
     * Default values:
     * <ul>
     *     <li>grantType: "client_credentials"</li>
     *     <li>resteasyClient: null (a new Client will be created)</li>
     *     <li>disableTrustManager: false</li>
     *     <li>sslContext: null</li>
     * </ul>
     *
     * @param serverUrl             the base URL of the PiSpi server
     * @param clientId              the OAuth2 client ID
     * @param clientSecret          the OAuth2 client secret
     * @param grantType             the OAuth2 grant type
     * @param apiKey                the API key for accessing PiSpi services
     * @param scopes                the OAuth2 scopes
     * @param resteasyClient        a pre-configured Resteasy Client instance (if null, a new one will be created)
     * @param disableTrustManager   flag to disable SSL trust manager
     * @param clientCertPath        the path to the client certificate for mTLS
     * @param clientKeyPath         the path to the client key for mTLS
     * @param sslContext            the SSL context to use for secure connections
     * @param customJacksonProvider a custom Jackson provider for JSON serialization/deserialization
     * @return a new instance of PiSpiClient
     */
     static PiSpiClient getInstance(
            String serverUrl,
            String clientId,
            String clientSecret,
            String grantType,
            String apiKey,
            List<String> scopes,
            Client resteasyClient,
            boolean disableTrustManager,
            String clientCertPath,
            String clientKeyPath,
            SSLContext sslContext,
            Object customJacksonProvider,
            String authtoken
    ) {

         BaseConfig config = BaseConfig.builder()
                 .serverUrl(serverUrl)
                 .clientId(clientId)
                 .clientSecret(clientSecret)
                 .grantType(grantType)
                 .apiKey(apiKey)
                 .scopes(scopes)
                 .clientCertPath(clientCertPath)
                 .clientKeyPath(clientKeyPath)
                 .build();

        SSLContext effectiveSslContext = sslContext;
        if (effectiveSslContext == null && clientCertPath != null && clientKeyPath != null) {
            // TODO: Implement SSLContext creation from client certificate and key
//            effectiveSslContext = MtlsConfigurer.createSslContext(
//                    clientCertPath,
//                    clientKeyPath,
//                    null
//            );
        }

        // If a Client is provided, it is assumed to be fully configured
        // (SSL, mTLS, trust manager, providers). No further modification is applied.
        Client client = resteasyClient != null
                ? resteasyClient
                : newRestClient(customJacksonProvider, effectiveSslContext, disableTrustManager);
        return new PiSpiClient(config, client, authtoken);
    }

    /**
     * See {@link #getInstance(String, String, String, String, String, List, Client, boolean, String, String, SSLContext, Object, String)} for the details about the parameters and their default values
     */
    public static PiSpiClient getInstance(String serverUrl, String clientId, String clientSecret, String apiKey, List<String> scopes, SSLContext sslContext, Object customJacksonProvider, boolean disableTrustManager) {
        return getInstance(serverUrl, clientId, clientSecret, OAuth2Constants.CLIENT_CREDENTIALS, apiKey, scopes, newRestClient(customJacksonProvider, sslContext, disableTrustManager), false, null, null, null, null, null);
    }

    /**
     * See {@link #getInstance(String, String, String, String, String, List, Client, boolean, String, String, SSLContext, Object, String)} for the details about the parameters and their default values
     */
    public static PiSpiClient getInstance(String serverUrl, String clientId, String clientSecret, String apiKey, SSLContext sslContext, Object customJacksonProvider, boolean disableTrustManager) {
        return getInstance(serverUrl, clientId, clientSecret, OAuth2Constants.CLIENT_CREDENTIALS, apiKey, null, newRestClient(customJacksonProvider, sslContext, disableTrustManager), false, null, null, null, null, null);
    }

    /**
     * See {@link #getInstance(String, String, String, String, String, List, Client, boolean, String, String, SSLContext, Object, String)} for the details about the parameters and their default values
     */
    public static PiSpiClient getInstance(String serverUrl, String clientId, String clientSecret, String apiKey) {
        return getInstance(serverUrl, clientId, clientSecret, OAuth2Constants.CLIENT_CREDENTIALS, apiKey, null, null, false, null, null, null, null, null);
    }

    /**
     * See {@link #getInstance(String, String, String, String, SSLContext, Object, boolean)} for the details about the parameters and their default values
     */
    public static PiSpiClient getInstance(String serverUrl, String clientId, String clientSecret, String apiKey, SSLContext sslContext) {
        return getInstance(serverUrl, clientId, clientSecret, apiKey, sslContext, null, false);
    }

    /**
     * See {@link #getInstance(String, String, String, String, SSLContext, Object, boolean)} for the details about the parameters and their default values
     */
    public static PiSpiClient getInstance(String serverUrl, String clientId, String clientSecret, String apiKey, SSLContext sslContext, Object customJacksonProvider) {
        return getInstance(serverUrl, clientId, clientSecret, apiKey, sslContext, customJacksonProvider, false);
    }

    /**
     * See {@link #getInstance(String, String, String, String, String, List, Client, boolean, String, String, SSLContext, Object, String)} for the details about the parameters and their default values
     */
    public static PiSpiClient getInstance(String serverUrl, String clientId, String clientSecret, String apiKey, List<String> scopes, String clientCertPath, String clientKeyPath, boolean disableTrustManager, Object customJacksonProvider) {
        return getInstance(serverUrl, clientId, clientSecret, OAuth2Constants.CLIENT_CREDENTIALS, apiKey, scopes, null, disableTrustManager, clientCertPath, clientKeyPath, null, customJacksonProvider, null);
    }

    /**
     * See {@link #getInstance(String, String, String, String, String, List, Client, boolean, String, String, SSLContext, Object, String)} for the details about the parameters and their default values
     */
    public static PiSpiClient getInstance(String serverUrl, String clientId, String clientSecret, String apiKey, List<String> scopes, String clientCertPath, String clientKeyPath, Object customJacksonProvider) {
        return getInstance(serverUrl, clientId, clientSecret, OAuth2Constants.CLIENT_CREDENTIALS, apiKey, scopes, null, false, clientCertPath, clientKeyPath, null, customJacksonProvider, null);
    }

    /**
     * See {@link #getInstance(String, String, String, String, String, List, Client, boolean, String, String, SSLContext, Object, String)} for the details about the parameters and their default values
     */
    public static PiSpiClient getInstance(String serverUrl, String clientId, String clientSecret, String apiKey, List<String> scopes, String clientCertPath, String clientKeyPath) {
        return getInstance(serverUrl, clientId, clientSecret, OAuth2Constants.CLIENT_CREDENTIALS, apiKey, scopes, null, false, clientCertPath, clientKeyPath, null, null, null);
    }

    public TokenManager tokenManager() {
        return tokenManager;
    }

    public ApiResource api() {
        return clientProvider.targetProxy(target, ApiResource.class);
    }

    public ComptesResource comptes() {
        return clientProvider.targetProxy(target, ComptesResource.class);
    }

    public synchronized ComptesResourceWrapper queryComptes() {
        if (comptesResourceWrapper == null) {
            comptesResourceWrapper = new ComptesResourceWrapper(api().comptes(), target);
        }
        return comptesResourceWrapper;
    }


    @Override
    public void close() throws Exception {
        closed = true;
        client.close();
    }

    /**
     * @return true if the underlying client is closed.
     */
    public boolean isClosed() {
        return closed;
    }
}
