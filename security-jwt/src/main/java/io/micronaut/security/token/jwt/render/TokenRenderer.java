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
package io.micronaut.security.token.jwt.render;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.security.authentication.Authentication;

/**
 * Responsible for converting token information to an {@link AccessRefreshToken}.
 *
 * @author Sergio del Amo
 * @since 1.0
 */
public interface TokenRenderer {

    /**
     * @param expiresIn In milliseconds
     * @param accessToken JWT token
     * @param refreshToken JWT token
     * @return instance of {@link AccessRefreshToken}
     */
    AccessRefreshToken render(Integer expiresIn, String accessToken, @Nullable String refreshToken);

    /**
     *
     * @param authentication Authenticated user's representation.
     * @param expiresIn In milliseconds
     * @param accessToken  JWT token
     * @param refreshToken JWT token
     * @return instance of {@link AccessRefreshToken}
     */
    AccessRefreshToken render(Authentication authentication, Integer expiresIn, String accessToken, @Nullable String refreshToken);
}
