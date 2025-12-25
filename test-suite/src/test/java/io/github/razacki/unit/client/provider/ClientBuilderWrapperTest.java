package io.github.razacki.unit.client.provider;

import io.github.provider.ClientBuilderWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.ClientBuilder;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Tag("unit")
@DisplayName("ClientBuilderWrapper Tests")
public class ClientBuilderWrapperTest {

    private SSLContext sslContext;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        // Use the default system SSL context for testing
        sslContext = SSLContext.getDefault();
    }

    /**
     * Helper method to access the private 'clazz' field using reflection.
     * This is necessary because the test is in a different package than the source.
     *
     * @return The Class object stored in the static field 'clazz'
     * @throws Exception if the field cannot be accessed
     */
    private Class<?> getInternalClazz() throws Exception {
        Field field = ClientBuilderWrapper.class.getDeclaredField("clazz");
        field.setAccessible(true);
        return (Class<?>) field.get(null);
    }

    @Test
    @DisplayName("Should load the correct RESTEasy implementation class")
    void shouldLoadResteasyClass() throws Exception {
        // Act: Get the class detected by the static block
        Class<?> internalClazz = getInternalClazz();

        // Assert: Ensure it's not null and belongs to RESTEasy
        assertThat(internalClazz).isNotNull();
        assertThat(internalClazz.getName()).contains("ResteasyClientBuilder");
    }

    @Test
    @DisplayName("Should create a ClientBuilder instance and invoke methods by reflection")
    void shouldCreateClientBuilderSuccessfully() throws Exception {
        // Act: Create the builder using the wrapper's factory method
        ClientBuilder builder = ClientBuilderWrapper.create(sslContext, false);

        // Assert: Verify the builder is created and matches the detected RESTEasy implementation
        assertThat(builder).isNotNull();
        assertThat(builder.getClass()).isEqualTo(getInternalClazz());
    }

    @Test
    @DisplayName("Should not throw exception when disableTrustManager is true")
    void shouldHandleDisableTrustManagerFlag() {
        // Act & Assert: Verify that calling the dynamic method 'disableTrustManager' works without errors
        assertThatCode(() -> {
            ClientBuilder builder = ClientBuilderWrapper.create(sslContext, true);
            assertThat(builder).isNotNull();
        }).doesNotThrowAnyException();
    }
}