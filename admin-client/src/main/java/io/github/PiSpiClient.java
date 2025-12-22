package io.github;

import io.github.provider.ResteasyClientProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;

import java.util.Iterator;
import java.util.ServiceLoader;

public class PiSpiClient implements AutoCloseable{

    private static volatile ResteasyClientProvider CLIENT_PROVIDER = resolveResteasyClientProvider();

    private static ResteasyClientProvider resolveResteasyClientProvider() {
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
        CLIENT_PROVIDER = provider;
    }

    public static ResteasyClientProvider getClientProvider() {
        return CLIENT_PROVIDER;
    }


    @Override
    public void close() throws Exception {
        SSLContextBuilder builder = SSLContexts.custom();
    }
}
