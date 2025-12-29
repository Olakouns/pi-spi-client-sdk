package io.github.razacki.provider;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

public interface ResteasyClientProvider {
    Client newRestClient(Object customJacksonProvider, SSLContext sslContext, boolean disableTrustManager);

    <R> R targetProxy(WebTarget client, Class<R> targetClass);
}
