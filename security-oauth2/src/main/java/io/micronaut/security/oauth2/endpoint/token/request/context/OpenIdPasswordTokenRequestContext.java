/*
 * Copyright 2017-2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.security.oauth2.endpoint.token.request.context;

import io.micronaut.core.type.Argument;
import io.micronaut.http.MediaType;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.oauth2.configuration.OauthClientConfiguration;
import io.micronaut.security.oauth2.configuration.OpenIdClientConfiguration;
import io.micronaut.security.oauth2.configuration.endpoints.TokenEndpointConfiguration;
import io.micronaut.security.oauth2.endpoint.SecureEndpoint;
import io.micronaut.security.oauth2.endpoint.token.response.OpenIdTokenResponse;
import io.micronaut.security.oauth2.endpoint.token.response.TokenErrorResponse;
import io.micronaut.security.oauth2.grants.PasswordGrant;
import java.util.Map;

/**
 * A token request context for sending a password grant
 * request to an OpenID provider.
 *
 * @author James Kleeh
 * @since 1.2.0
 */
public class OpenIdPasswordTokenRequestContext extends AbstractTokenRequestContext<Map<String, String>, OpenIdTokenResponse> {

    private final AuthenticationRequest authenticationRequest;

    /**
     * @param authenticationRequest The authentication request
     * @param tokenEndpoint The token endpoint
     * @param clientConfiguration The client configuration
     */
    public OpenIdPasswordTokenRequestContext(AuthenticationRequest authenticationRequest,
                                             SecureEndpoint tokenEndpoint,
                                             OauthClientConfiguration clientConfiguration) {
        super(getMediaType(clientConfiguration), tokenEndpoint, clientConfiguration);
        this.authenticationRequest = authenticationRequest;
    }

    @Override
    public Map<String, String> getGrant() {
        PasswordGrant passwordGrant = new PasswordGrant(authenticationRequest, clientConfiguration);
        return passwordGrant.toMap();
    }

    /**
     * Resolves the media type for the request body.
     *
     * @param clientConfiguration The client configuration
     * @return The media type
     */
    protected static MediaType getMediaType(OauthClientConfiguration clientConfiguration) {
        return clientConfiguration.getOpenid()
                .flatMap(OpenIdClientConfiguration::getToken)
                .map(TokenEndpointConfiguration::getContentType)
                .orElse(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
    }

    @Override
    public Argument<OpenIdTokenResponse> getResponseType() {
        return Argument.of(OpenIdTokenResponse.class);
    }

    @Override
    public Argument<?> getErrorResponseType() {
        return Argument.of(TokenErrorResponse.class);
    }
}
