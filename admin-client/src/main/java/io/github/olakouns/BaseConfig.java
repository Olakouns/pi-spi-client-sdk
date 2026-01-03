/*
 * Copyright 2025 Razacki KOUNASSO
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.olakouns;

import java.util.List;

public class BaseConfig {
    private String serverUrl;
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String apiKey;
    private List<String> scopes;


    public static class Builder {
        private final BaseConfig instance = new BaseConfig();

        public Builder serverUrl(String serverUrl) {
            this.instance.setServerUrl(serverUrl);
            return this;
        }

        public Builder clientId(String clientId) {
            this.instance.setClientId(clientId);
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.instance.setClientSecret(clientSecret);
            return this;
        }

        public Builder grantType(String grantType) {
            this.instance.setGrantType(grantType);
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.instance.setApiKey(apiKey);
            return this;
        }

        public Builder scopes(List<String> scopes) {
            this.instance.setScopes(scopes);
            return this;
        }

        public BaseConfig build() {
            return instance;
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

    public boolean isPublicClient() {
        return clientSecret == null;
    }
}
