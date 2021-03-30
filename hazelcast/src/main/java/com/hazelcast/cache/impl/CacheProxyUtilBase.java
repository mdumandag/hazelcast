/*
 * Copyright (c) 2008-2021, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.cache.impl;

import com.hazelcast.config.CacheConfig;

import static com.hazelcast.internal.util.Preconditions.checkNotNull;

/**
 * Static util methods for {@linkplain com.hazelcast.cache.ICache} implementations.
 *
 * @see CacheProxy
 */
public class CacheProxyUtilBase {

    public static final int AWAIT_COMPLETION_TIMEOUT_SECONDS = 60;

    public static final String NULL_KEY_IS_NOT_ALLOWED = "Null key is not allowed!";

    protected CacheProxyUtilBase() {
    }

    /**
     * Validates that a key is not null.
     *
     * @param key the key to be validated.
     * @param <K> the type of key.
     * @throws NullPointerException if provided key is null.
     */
    public static <K> void validateNotNull(K key) {
        checkNotNull(key, NULL_KEY_IS_NOT_ALLOWED);
    }


    /**
     * Validates that the configured key matches the provided key.
     *
     * @param cacheConfig Cache configuration.
     * @param key         the key to be validated with its type.
     * @param <K>         the type of key.
     * @throws ClassCastException if the provided key does not match with configured type.
     */
    public static <K> void validateConfiguredTypes(CacheConfig cacheConfig, K key) throws ClassCastException {
        Class keyType = cacheConfig.getKeyType();
        validateConfiguredKeyType(keyType, key);
    }

    /**
     * Validates the key with key type.
     *
     * @param keyType key class.
     * @param key     key to be validated.
     * @param <K>     the type of key.
     * @throws ClassCastException if the provided key do not match with keyType.
     */
    public static <K> void validateConfiguredKeyType(Class<K> keyType, K key) throws ClassCastException {
        if (Object.class != keyType) {
            // means that type checks is required
            if (!keyType.isAssignableFrom(key.getClass())) {
                throw new ClassCastException("Key '" + key + "' is not assignable to " + keyType);
            }
        }
    }
}
