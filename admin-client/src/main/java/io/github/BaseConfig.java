package io.github;

import java.util.List;

public class BaseConfig {
    private String serverUrl;
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String apiKey;
    private List<String> scope;

    private String clientCertPath;   // client-cert.pem
    private String clientKeyPath;    // client-key.pem
}
