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
package io.micronaut.security.oauth2.endpoint.authorization.state;

import io.micronaut.core.type.Argument;
import io.micronaut.json.JsonMapper;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Base64;

/**
 * Jackson based implementation for state serdes.
 *
 * @author James Kleeh
 * @since 1.2.0
 */
@Singleton
public class JacksonStateSerDes implements StateSerDes {

    private static final Logger LOG = LoggerFactory.getLogger(JacksonStateSerDes.class);

    private final JsonMapper jsonMapper;

    /**
     * @param jsonMapper To serialize/de-serialize the state
     * @since 3.3
     */
    public JacksonStateSerDes(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public State deserialize(String base64State) {
        try {
            byte[] decodedBytes = Base64.getUrlDecoder().decode(base64State);
            return jsonMapper.readValue(decodedBytes, Argument.of(DefaultState.class));
        } catch (IOException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Failed to deserialize the authorization request state", e);
            }
        }
        return null;
    }

    @Override
    public String serialize(State state) {
        try {
            return Base64.getEncoder().encodeToString(jsonMapper.writeValueAsBytes(state));
        } catch (IOException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Failed to serialize the authorization request state to JSON", e);
            }
        }
        return null;
    }

}
