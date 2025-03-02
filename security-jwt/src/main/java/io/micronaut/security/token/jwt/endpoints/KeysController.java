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
package io.micronaut.security.token.jwt.endpoints;

import com.nimbusds.jose.jwk.JWKSet;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.json.JsonMapper;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

/**
 * Endpoint which exposes a JSON Web Key Set built with the JWK provided by {@link io.micronaut.security.token.jwt.endpoints.JwkProvider} beans.
 *
 * @since 1.1.0
 * @author Sergio del Amo
 */
@Requires(property = KeysControllerConfigurationProperties.PREFIX + ".enabled", notEquals = StringUtils.FALSE, defaultValue = StringUtils.TRUE)
@Requires(beans = JwkProvider.class)
@Controller("${" + KeysControllerConfigurationProperties.PREFIX + ".path:/keys}")
@Secured(SecurityRule.IS_ANONYMOUS)
public class KeysController {
    private static final Logger LOG = LoggerFactory.getLogger(KeysController.class);
    private static final String EMPTY_KEYS = "{\"keys\":[]}";

    private final Collection<JwkProvider> jwkProviders;
    private final JsonMapper jsonMapper;

    /**
     * Instantiates a {@link io.micronaut.security.token.jwt.endpoints.KeysController}.
     * @param jwkProviders a collection of JSON Web Key providers.
     * @param jsonMapper Jackson ObjectMapper used to do serialization.
     * @since 3.3
     */
    public KeysController(Collection<JwkProvider> jwkProviders, JsonMapper jsonMapper) {
        this.jwkProviders = jwkProviders;
        this.jsonMapper = jsonMapper;
    }

    /**
     *
     * @return a JSON Web Key Set (JWKS) payload.
     */
    @Get
    @SingleResult
    public Publisher<String> keys() {
        if (jwkProviders.isEmpty()) {
            return Mono.just(EMPTY_KEYS);
        }
        return Flux.fromIterable(jwkProviders)
                .flatMapIterable(JwkProvider::retrieveJsonWebKeys)
                .collectList()
                .map(JWKSet::new)
                .map(JWKSet::toJSONObject)
                .map(m -> {
                    // we need "keys" in the output, and the mapper may be configured to drop empty lists, so we have a
                    // separate branch for that.
                    if (!((Collection<?>) m.getOrDefault("keys", Collections.emptyList())).isEmpty()) {
                        try {
                            return new String(jsonMapper.writeValueAsBytes(m));
                        } catch (IOException e) {
                            if (LOG.isErrorEnabled()) {
                                LOG.error("JSON Processing exception getting JSON representation of the JSON Web Key sets");
                            }
                        }
                    }
                    return EMPTY_KEYS;
                });

    }
}
