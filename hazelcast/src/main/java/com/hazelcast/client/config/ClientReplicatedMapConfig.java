/*
 * Copyright (c) 2008-2019, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.client.config;

import com.hazelcast.config.NamedConfig;
import com.hazelcast.config.NearCacheConfig;

import java.util.Objects;

import static com.hazelcast.internal.util.Preconditions.checkNotNull;

/**
 *  The {@code ClientReplicatedMapConfig} contains the configuration for the client
 *  regarding {@link com.hazelcast.core.HazelcastInstance#getReplicatedMap(String)
 *  ReplicatedMap}.
 */
public class ClientReplicatedMapConfig implements NamedConfig {

    private String name;
    private NearCacheConfig nearCacheConfig;

    public ClientReplicatedMapConfig() {
    }

    /**
     * Creates a new {@link ClientReplicatedMapConfig} with the given configuration name for the
     * {@link com.hazelcast.replicatedmap.ReplicatedMap} instance. This could be a pattern or
     * an actual instance name. The name must not be modified after this instance is added
     * to the {@link ClientConfig}.
     *
     * @param name the configuration name for the {@link com.hazelcast.replicatedmap.ReplicatedMap} instance.
     */
    public ClientReplicatedMapConfig(String name) {
        setName(name);
    }

    /**
     * Copy constructor.
     *
     * @param config {@link ClientMapConfig} instance to be copied.
     */
    public ClientReplicatedMapConfig(ClientReplicatedMapConfig config) {

    }

    /**
     * Returns the configuration name for the {@link com.hazelcast.replicatedmap.ReplicatedMap} instance.
     *
     * @return the configuration name for the {@link com.hazelcast.replicatedmap.ReplicatedMap} instance.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the configuration name for the {@link com.hazelcast.replicatedmap.ReplicatedMap} instance.
     * This could be a pattern or an actual instance name. The name must not be
     * modified after this instance is added to the {@link ClientConfig}.
     *
     * @param name the configuration name for the {@link com.hazelcast.replicatedmap.ReplicatedMap} instance.
     * @return configured {@link ClientReplicatedMapConfig} for chaining.
     */
    public ClientReplicatedMapConfig setName(String name) {
        this.name = checkNotNull(name, "Configuration name cannot be null.");
        this.name = name;
        return this;
    }

    /**
     * Returns the {@link NearCacheConfig} instance of this {@link ClientReplicatedMapConfig}.
     *
     * @return the {@link NearCacheConfig} instance of this {@link ClientReplicatedMapConfig}.
     */
    public NearCacheConfig getNearCacheConfig() {
        return nearCacheConfig;
    }

    /**
     * Sets the {@link NearCacheConfig} instance for this {@link ClientReplicatedMapConfig}.
     *
     * @param nearCacheConfig the {@link NearCacheConfig} instance for this {@link ClientReplicatedMapConfig}.
     * @return configured {@link ClientReplicatedMapConfig} for chaining.
     */
    public ClientReplicatedMapConfig setNearCacheConfig(NearCacheConfig nearCacheConfig) {
        this.nearCacheConfig = nearCacheConfig;
        return this;
    }

    /**
     * Checks if Near Cache is enabled or not.
     *
     * @return {@code true} if Near Cache is enabled, {@code false} otherwise
     */
    public boolean isNearCacheEnabled() {
        return nearCacheConfig != null;
    }

    @Override
    public String toString() {
        return "ClientReplicatedMapConfig{"
                + "name='" + name + '\''
                + ", nearCacheConfig=" + nearCacheConfig
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientReplicatedMapConfig that = (ClientReplicatedMapConfig) o;
        return name.equals(that.name)
                && Objects.equals(nearCacheConfig, that.nearCacheConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, nearCacheConfig);
    }
}
