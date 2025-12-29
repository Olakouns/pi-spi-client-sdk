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

package io.github.razacki.filter;

import io.github.razacki.token.TokenManager;
import org.jboss.logging.Logger;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.List;

public class BearerAuthFilter extends BaseFilter implements ClientRequestFilter, ClientResponseFilter {
    public static final String AUTH_HEADER_PREFIX = "Bearer ";
    private final String tokenString;
    protected final TokenManager tokenManager;
    private static final Logger logger = Logger.getLogger(BearerAuthFilter.class);


    public BearerAuthFilter(String tokenString) {
        this.tokenString = tokenString;
        this.tokenManager = null;
    }

    public BearerAuthFilter(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
        this.tokenString = null;
    }

    @Override
    public void filter(ClientRequestContext clientRequestContext) throws IOException {
        String authHeader = (tokenManager != null ? tokenManager.getAccessTokenString() : tokenString);
        if (authHeader != null && !authHeader.startsWith(AUTH_HEADER_PREFIX)) {
            authHeader = AUTH_HEADER_PREFIX + authHeader;
        }
        clientRequestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
    }

    @Override
    public void filter(ClientRequestContext clientRequestContext, ClientResponseContext clientResponseContext) throws IOException {
        if (clientResponseContext.getStatus() == 401 && tokenManager != null) {
            List<Object> authHeaders = clientRequestContext.getHeaders().get(HttpHeaders.AUTHORIZATION);
            if (authHeaders == null) {
                return;
            }
            for (Object authHeader : authHeaders) {
                if (authHeader instanceof String) {
                    String headerValue = (String) authHeader;
                    String authHeaderPrefix = getAuthHeaderPrefix();
                    if (headerValue.startsWith(authHeaderPrefix)) {
                        String token = headerValue.substring( authHeaderPrefix.length() );
                        tokenManager.invalidate( token );
                    }
                }
            }
        }
    }

    protected String getAuthHeaderPrefix() {
        return AUTH_HEADER_PREFIX;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
