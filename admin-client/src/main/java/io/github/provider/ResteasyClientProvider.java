package io.github.provider;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;

import javax.net.ssl.SSLContext;

public interface ResteasyClientProvider {
    Client newRestClient(Object customJacksonProvider, SSLContext sslContext, boolean disableTrustManager);

    <R> R targetProxy(WebTarget client, Class<R> targetClass);
}
