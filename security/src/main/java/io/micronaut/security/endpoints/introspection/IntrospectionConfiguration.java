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
package io.micronaut.security.endpoints.introspection;

import io.micronaut.context.annotation.DefaultImplementation;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.Toggleable;

/**
 * Encapsulates the configuration of {@link IntrospectionController}.
 * @author Sergio del Amo
 * @since 2.1.0
 */
@DefaultImplementation(IntrospectionConfigurationProperties.class)
public interface IntrospectionConfiguration extends Toggleable {

    /**
     * @return path
     */
    @NonNull
    String getPath();
}
