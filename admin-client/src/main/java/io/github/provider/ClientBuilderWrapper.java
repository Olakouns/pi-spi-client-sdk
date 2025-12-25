package io.github.provider;


import io.github.exception.PiSpiException;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.ClientBuilder;

public class ClientBuilderWrapper {
    private ClientBuilderWrapper(){}
    static Class<?> clazz;
    static {
        try {
            clazz = Class.forName("org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl");
        } catch (ClassNotFoundException e) {
            try {
                clazz = Class.forName("org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder");
            } catch (ClassNotFoundException ex) {
                throw new PiSpiException("RestEasy 3 or 4 not found on classpath", ex);
            }
        }
    }

    public static ClientBuilder create(SSLContext sslContext, boolean disableTrustManager) {
        try {
            Object o = clazz.getDeclaredConstructor().newInstance();
            clazz.getMethod("sslContext", SSLContext.class).invoke(o, sslContext);
            clazz.getMethod("connectionPoolSize", int.class).invoke(o, 10);
            if (disableTrustManager) {
                clazz.getMethod("disableTrustManager").invoke(o);
            }
            return (ClientBuilder) o;
        } catch (Exception e) {
            throw new PiSpiException("Unable to create ResteasyClientBuilder", e);
        }
    }
}
