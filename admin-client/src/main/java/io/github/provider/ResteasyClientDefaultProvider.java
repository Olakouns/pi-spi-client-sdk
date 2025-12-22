package io.github.provider;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.net.ssl.SSLContext;

public class ResteasyClientDefaultProvider implements ResteasyClientProvider{
    @Override
    public Client newRestClient(Object customJacksonProvider, SSLContext sslContext, boolean disableTrustManager) {
        ClientBuilder clientBuilder = ClientBuilderWrapper.create(sslContext, disableTrustManager);

        if (customJacksonProvider != null) {
            clientBuilder.register(customJacksonProvider, 100);
        } else {
            clientBuilder.register(JacksonProvider.class, 100);
        }

        return clientBuilder.build();
    }

    @Override
    public <R> R targetProxy(WebTarget client, Class<R> targetClass) {
        return ResteasyWebTarget.class.cast(client).proxy(targetClass);
    }
}
