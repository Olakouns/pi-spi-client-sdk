package io.github.provider;


import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

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
