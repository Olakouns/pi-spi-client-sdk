package io.github;

import java.util.List;

public class BaseConfig {
    private String serverUrl;
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String apiKey;
    private List<String> scopes;

    private String clientCertPath;   // client-cert.pem
    private String clientKeyPath;    // client-key.pem

    public BaseConfig(String serverUrl, String clientId, String clientSecret, String grantType, String apiKey, List<String> scopes, String clientCertPath, String clientKeyPath) {
        this.serverUrl = serverUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.grantType = grantType;
        this.apiKey = apiKey;
        this.scopes = scopes;
        this.clientCertPath = clientCertPath;
        this.clientKeyPath = clientKeyPath;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public String getClientCertPath() {
        return clientCertPath;
    }

    public void setClientCertPath(String clientCertPath) {
        this.clientCertPath = clientCertPath;
    }

    public String getClientKeyPath() {
        return clientKeyPath;
    }

    public void setClientKeyPath(String clientKeyPath) {
        this.clientKeyPath = clientKeyPath;
    }

    public boolean isPublicClient() {
        return clientSecret == null;
    }
}
