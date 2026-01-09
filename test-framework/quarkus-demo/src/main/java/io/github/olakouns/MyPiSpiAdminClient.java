package io.github.olakouns;

import io.github.olakouns.PiSpiClient;
import io.github.olakouns.PiSpiClientBuilder;
import io.quarkus.arc.DefaultBean;
import io.smallrye.common.annotation.Identifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

@ApplicationScoped
public class MyPiSpiAdminClient {

    private final PiSpiProperties piSpiProperties;

    public MyPiSpiAdminClient(PiSpiProperties piSpiProperties) {
        this.piSpiProperties = piSpiProperties;
    }

    @Produces
    @DefaultBean
    public PiSpiClient configureMyClient() {
        return PiSpiClientBuilder.builder()
                .serverUrl(piSpiProperties.serverUrl())
                .clientId(piSpiProperties.clientId())
                .clientSecret(piSpiProperties.clientSecret())
                .apiKey(piSpiProperties.apiKey())
                .build();
    }

    @Produces
    @Identifier("myClientWithSSLContext")
    public PiSpiClient myClientWithSSLContext() throws NoSuchAlgorithmException {
        SSLContext sslContext = SSLContext.getDefault();
        return PiSpiClientBuilder.builder()
                .serverUrl(piSpiProperties.serverUrl())
                .clientId(piSpiProperties.clientId())
                .clientSecret(piSpiProperties.clientSecret())
                .apiKey(piSpiProperties.apiKey())
                .sslContext(sslContext)
                .build();
    }
}
