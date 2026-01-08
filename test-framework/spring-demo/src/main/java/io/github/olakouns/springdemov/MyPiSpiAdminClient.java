package io.github.olakouns.springdemov;

import io.github.olakouns.PiSpiClient;
import io.github.olakouns.PiSpiClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

@Configuration
public class MyPiSpiAdminClient {

    private final PiSpiProperties piSpiProperties;

    public MyPiSpiAdminClient(PiSpiProperties piSpiProperties) {
        this.piSpiProperties = piSpiProperties;
    }

    @Bean
    @Primary
    public PiSpiClient configureMyClient() {
        return PiSpiClientBuilder.builder()
                .serverUrl(piSpiProperties.getServerUrl())
                .clientId(piSpiProperties.getClientId())
                .clientSecret(piSpiProperties.getClientSecret())
                .apiKey(piSpiProperties.getApiKey())
                .build();
    }

    @Bean("myClientWithSSLContext")
    public PiSpiClient myClientWithSSLContext() throws NoSuchAlgorithmException {
        // TODO: To be configure using you certs...
        SSLContext sslContext = SSLContext.getDefault();
        return PiSpiClientBuilder.builder()
                .serverUrl(piSpiProperties.getServerUrl())
                .clientId(piSpiProperties.getClientId())
                .clientSecret(piSpiProperties.getClientSecret())
                .apiKey(piSpiProperties.getApiKey())
                .sslContext(sslContext)
                .build();
    }
}
