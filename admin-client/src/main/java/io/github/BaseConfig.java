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

    public BaseConfig() {
    }

    private BaseConfig(Builder builder) {
        this.serverUrl = builder.serverUrl;
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.grantType = builder.grantType;
        this.apiKey = builder.apiKey;
        this.scopes = builder.scopes;
        this.clientCertPath = builder.clientCertPath;
        this.clientKeyPath = builder.clientKeyPath;
    }

    public static class Builder {
        private String serverUrl;
        private String clientId;
        private String clientSecret;
        private String grantType;
        private String apiKey;
        private List<String> scopes;
        private String clientCertPath;
        private String clientKeyPath;

        public Builder serverUrl(String serverUrl) {
            this.serverUrl = serverUrl;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder grantType(String grantType) {
            this.grantType = grantType;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder scopes(List<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public Builder clientCertPath(String path) {
            this.clientCertPath = path;
            return this;
        }

        public Builder clientKeyPath(String path) {
            this.clientKeyPath = path;
            return this;
        }

        public BaseConfig build() {
            return new BaseConfig(this);
        }
    }

    public static Builder builder() {
        return new Builder();
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
