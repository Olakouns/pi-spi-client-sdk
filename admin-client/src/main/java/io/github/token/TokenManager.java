/*
 * Copyright 2025 Razacki KOUNASSO
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.token;

import io.github.BaseConfig;
import io.github.PiSpiClient;
import io.github.constants.ScopeConstants;
import io.github.representation.AccessTokenResponse;
import io.github.util.Time;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;

import static io.github.constants.OAuth2Constants.*;

public class TokenManager {
    private static final long DEFAULT_MIN_VALIDITY = 30;


    private AccessTokenResponse currentToken;
    private long expirationTime;
    private long refreshExpirationTime;
    private long minTokenValidity = DEFAULT_MIN_VALIDITY;
    private final BaseConfig config;
    private final TokenService tokenService;
    private final String accessTokenGrantType;

    public TokenManager(BaseConfig config, Client client) {
        this.config = config;
        WebTarget target = client.target(config.getServerUrl());
        //if (!config.isPublicClient()) {
        //    target.register(new BasicAuthFilter(config.getClientId(), config.getClientSecret()));
        //}

        this.tokenService = PiSpiClient.getClientProvider().targetProxy(target, TokenService.class);
        this.accessTokenGrantType = config.getGrantType();
    }

    public String getAccessTokenString() {
        return getAccessToken().getToken();
    }

    public synchronized AccessTokenResponse getAccessToken() {
        if (currentToken == null) {
            grantToken();
        } else if (tokenExpired()) {
            refreshToken();
        }
        return currentToken;
    }

    public AccessTokenResponse grantToken() {
        Form form = new Form().param(GRANT_TYPE, accessTokenGrantType);
        form.param(CLIENT_ID, config.getClientId());

        if (config.getScopes() != null) {
            form.param(SCOPE, ScopeConstants.scopesToString(config.getScopes()));
        }

        if (!config.isPublicClient()) {
            form.param(CLIENT_SECRET, config.getClientSecret());
        }

        int requestTime = Time.currentTime();
        synchronized (this) {
            currentToken = tokenService.grantToken(form.asMap());
            expirationTime = requestTime + currentToken.getExpiresIn();
            refreshExpirationTime = requestTime + currentToken.getRefreshExpiresIn();
        }
        return currentToken;
    }

    public synchronized AccessTokenResponse refreshToken() {
        if (currentToken.getRefreshToken() == null || refreshTokenExpired()) {
            return grantToken();
        }

        Form form = new Form().param(GRANT_TYPE, REFRESH_TOKEN)
                .param(REFRESH_TOKEN, currentToken.getRefreshToken());

        if (config.isPublicClient()) {
            form.param(CLIENT_ID, config.getClientId());
        }

        try {
            int requestTime = Time.currentTime();

            currentToken = tokenService.refreshToken(form.asMap());
            expirationTime = requestTime + currentToken.getExpiresIn();
            return currentToken;
        } catch (BadRequestException e) {
            return grantToken();
        }
    }

    public synchronized void setMinTokenValidity(long minTokenValidity) {
        this.minTokenValidity = minTokenValidity;
    }

    private synchronized boolean tokenExpired() {
        return (Time.currentTime() + minTokenValidity) >= expirationTime;
    }

    private synchronized boolean refreshTokenExpired() {
        return (Time.currentTime() + minTokenValidity) >= refreshExpirationTime;
    }

    /**
     * Invalidates the current token, but only when it is equal to the token passed as an argument.
     *
     * @param token the token to invalidate (cannot be null).
     */
    public synchronized void invalidate(String token) {
        if (currentToken == null) {
            return; // There's nothing to invalidate.
        }
        if (token.equals(currentToken.getToken())) {
            // When used next, this cause a refresh attempt, that in turn will cause a grant attempt if refreshing fails.
            expirationTime = -1;
        }
    }


}
