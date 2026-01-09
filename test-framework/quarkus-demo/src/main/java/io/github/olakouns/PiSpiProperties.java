package io.github.olakouns;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "pi-spi-admin")
public interface PiSpiProperties {
    String serverUrl();
    String clientId();
    String clientSecret();
    String apiKey();
}
