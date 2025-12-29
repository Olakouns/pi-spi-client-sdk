package io.github.razacki.unit.client.provider;

import io.github.razacki.provider.JacksonProvider;
import io.github.razacki.provider.ResteasyClientDefaultProvider;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("unit")
@DisplayName("ResteasyClientDefaultProvider Tests")
public class ResteasyClientDefaultProviderTest {
    private ResteasyClientDefaultProvider provider;
    private SSLContext sslContext;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        provider = new ResteasyClientDefaultProvider();
        sslContext = SSLContext.getDefault();
    }

    @Test
    @DisplayName("Should create a new REST client with default Jackson provider")
    void shouldCreateClientWithDefaultJackson() {
        // Act
        Client client = provider.newRestClient(null, sslContext, false);

        // Assert
        assertThat(client).isNotNull();
        assertThat(client.getConfiguration().isRegistered(JacksonProvider.class)).isTrue();
    }

    @Test
    @DisplayName("Should create a new REST client with custom Jackson provider")
    void shouldCreateClientWithCustomJackson() {
        // Arrange
        Object customProvider = new Object();

        // Act
        Client client = provider.newRestClient(customProvider, sslContext, false);

        // Assert
        assertThat(client).isNotNull();
        assertThat(client.getConfiguration().isRegistered(customProvider)).isTrue();
        assertThat(client.getConfiguration().isRegistered(JacksonProvider.class)).isFalse();
    }

    @Test
    @DisplayName("Should cast WebTarget and create a proxy")
    void shouldCreateProxyFromTarget() {
        // Arrange
        Client client = provider.newRestClient(null, sslContext, false);
        WebTarget target = client.target("http://localhost");

        // Act
        TestResource proxy = provider.targetProxy(target, TestResource.class);

        // Assert
        assertThat(proxy).isNotNull();
        assertThat(target).isInstanceOf(ResteasyWebTarget.class);
    }

    interface TestResource {
        void get();
    }
}
