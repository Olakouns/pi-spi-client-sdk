package io.github.provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;

public class JacksonProvider extends ResteasyJackson2Provider {

    @Override
    public ObjectMapper locateMapper(Class<?> type, MediaType mediaType) {
        ObjectMapper objectMapper = super.locateMapper(type, mediaType);

        // Same like JSONSerialization class. Makes it possible to use admin-client against older versions
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // The client must work with the newer versions of Keycloak server, which might contain the JSON fields not yet known by the client. So unknown fields will be ignored.
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }
}
