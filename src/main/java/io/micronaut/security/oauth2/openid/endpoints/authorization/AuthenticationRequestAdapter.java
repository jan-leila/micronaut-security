/*
 * Copyright 2017-2019 original authors
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

package io.micronaut.security.oauth2.openid.endpoints.authorization;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.oauth2.configuration.OauthConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Provides an {@link AuthenticationRequest} by combining {@link OauthConfiguration},
 * {@link StateProvider}, {@link NonceProvider}, {@link AuthorizationEndpoint}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
public class AuthenticationRequestAdapter implements AuthenticationRequest {

    private HttpRequest<?> request;
    private final boolean unauthorized;
    private OauthConfiguration oauthConfiguration;
    private StateProvider stateProvider;
    private NonceProvider nonceProvider;
    private LoginHintProvider loginHintProvider;
    private IdTokenHintProvider idTokenHintProvider;
    private AuthorizationEndpointRequestConfiguration authorizationEndpointConfiguration;

    /**
     *
     * @param request the Original request prior redirect.
     * @param unauthorized If the reason for redirection is because the user
     *                     requested a resource that requires authorization.
     * @param oauthConfiguration OAuth 2.0 Configuration
     * @param authorizationEndpointConfiguration Authorization Endpoint Configuration
     * @param stateProvider State Provider
     * @param nonceProvider Nonce Provider
     * @param loginHintProvider Login Hint Provider
     * @param idTokenHintProvider Id Token Hint Provider
     */
    public AuthenticationRequestAdapter(HttpRequest<?> request,
                                        boolean unauthorized,
                                        OauthConfiguration oauthConfiguration,
                                        AuthorizationEndpointRequestConfiguration authorizationEndpointConfiguration,
                                        @Nullable StateProvider stateProvider,
                                        @Nullable NonceProvider nonceProvider,
                                        @Nullable LoginHintProvider loginHintProvider,
                                        @Nullable IdTokenHintProvider idTokenHintProvider
                                        ) {
        this.request = request;
        this.unauthorized = unauthorized;
        this.oauthConfiguration = oauthConfiguration;
        this.authorizationEndpointConfiguration = authorizationEndpointConfiguration;
        this.stateProvider = stateProvider;
        this.nonceProvider = nonceProvider;
        this.loginHintProvider = loginHintProvider;
        this.idTokenHintProvider = idTokenHintProvider;
    }

    @Override
    @Nonnull
    public String getClientId() {
        return getOauthConfiguration().getClientId();
    }

    @Override
    @Nullable
    public String getState() {
        return getStateProvider().map(sp -> sp.generateState(request, unauthorized)).orElse(null);
    }

    @Nullable
    @Override
    public String getNonce() {
        return getNonceProvider().map(NonceProvider::generateNonce).orElse(null);
    }

    @Override
    @Nonnull
    public List<String> getScopes() {
        return getAuthorizationEndpointConfiguration().getScopes();
    }

    @Nonnull
    @Override
    public String getResponseType() {
        return getAuthorizationEndpointConfiguration().getResponseType();
    }

    @Nullable
    @Override
    public String getRedirectUri() {
        return getAuthorizationEndpointConfiguration().getRedirectUri();
    }

    @Nullable
    @Override
    public String getResponseMode() {
        return getAuthorizationEndpointConfiguration().getResponseMode();
    }

    @Nullable
    @Override
    public Display getDisplay() {
        return getAuthorizationEndpointConfiguration().getDisplay();
    }

    @Nullable
    @Override
    public Prompt getPrompt() {
        return getAuthorizationEndpointConfiguration().getPrompt();
    }

    @Nullable
    @Override
    public Integer getMaxAge() {
        return getAuthorizationEndpointConfiguration().getMaxAge();
    }

    @Nullable
    @Override
    public List<String> getUiLocales() {
        return getAuthorizationEndpointConfiguration().getUiLocales();
    }

    @Nullable
    @Override
    public String getIdTokenHint() {
        return getIdTokenHintProvider().map(IdTokenHintProvider::resolveIdTokenHint).orElse(null);
    }

    @Nullable
    @Override
    public String getLoginHint() {
        return getLoginHintProvider().map(LoginHintProvider::resolveLoginHint).orElse(null);
    }

    @Nullable
    @Override
    public List<String> getAcrValues() {
        return getAuthorizationEndpointConfiguration().getAcrValues();
    }

    private OauthConfiguration getOauthConfiguration() {
        return oauthConfiguration;
    }

    private Optional<IdTokenHintProvider> getIdTokenHintProvider() {
        return Optional.ofNullable(idTokenHintProvider);
    }

    private Optional<LoginHintProvider> getLoginHintProvider() {
        return Optional.ofNullable(loginHintProvider);
    }

    private Optional<StateProvider> getStateProvider() {
        return Optional.ofNullable(stateProvider);
    }

    private Optional<NonceProvider> getNonceProvider() {
        return Optional.ofNullable(nonceProvider);
    }

    private AuthorizationEndpointRequestConfiguration getAuthorizationEndpointConfiguration() {
        return authorizationEndpointConfiguration;
    }
}
