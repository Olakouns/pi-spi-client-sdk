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

import io.github.filter.ApiKeyFilter;
import io.github.filter.BearerAuthFilter;
import io.github.filter.PiSpiClientResponseFilter;
import io.github.provider.ResteasyClientProvider;
import io.github.resource.ApiResource;
import io.github.resource.wrapper.ApiResourceWrapper;
import io.github.token.TokenManager;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.WebTarget;
import java.util.Iterator;
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
    private ApiResourceWrapper apiWrapper;

    PiSpiClient(BaseConfig config, Client client, String authToken) {
        this.config = config;
        this.client = client;
        this.authToken = authToken;
        this.target = client.target(config.getServerUrl());
        tokenManager = authToken == null ? new TokenManager(config, client, clientProvider) : null;
        this.target.register(newAuthFilter());
        this.target.register(new PiSpiClientResponseFilter(), 200);
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

    public static PiSpiClient getInstance(BaseConfig config) {
        return getInstance(config, null, false, null, null, null);
    }

    /**
     * Creates a new instance of PiSpiClient with the provided configuration.
     * @param config the BaseConfig instance containing client configuration
     * @param resteasyClient the Resteasy Client instance to use (can be null)
     * @param disableTrustManager whether to disable the trust manager
     * @param sslContext the SSLContext to use (can be null)
     * @param customJacksonProvider a custom Jackson provider (can be null)
     * @param authToken  the authentication token to use (can be null)
     * @return @return a new instance of PiSpiClient
     */
    public static PiSpiClient getInstance(
            BaseConfig config,
            Client resteasyClient,
            boolean disableTrustManager,
            SSLContext sslContext,
            Object customJacksonProvider,
            String authToken
    ) {
        SSLContext effectiveSslContext = sslContext;

        if (effectiveSslContext == null && config.getClientCertPath() != null && config.getClientKeyPath() != null) {
            // TODO: créer SSLContext à partir du certificat et de la clé
        }

        Client client = resteasyClient != null
                ? resteasyClient
                : newRestClient(customJacksonProvider, effectiveSslContext, disableTrustManager);

        return new PiSpiClient(config, client, authToken);
    }

    public TokenManager tokenManager() {
        return tokenManager;
    }

    public synchronized ApiResourceWrapper api() {
        if (this.apiWrapper == null) {
            ApiResource proxy = clientProvider.targetProxy(target, ApiResource.class);
            this.apiWrapper = new ApiResourceWrapper(proxy, target);
        }
        return this.apiWrapper;
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
