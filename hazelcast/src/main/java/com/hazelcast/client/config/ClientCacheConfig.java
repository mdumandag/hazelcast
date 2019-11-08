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

import com.hazelcast.config.CacheSimpleConfig;
import com.hazelcast.config.NearCacheConfig;

public class ClientCacheConfig extends CacheSimpleConfig {

    private NearCacheConfig nearCacheConfig;

    public ClientCacheConfig() {
    }

    public ClientCacheConfig(String name) {
        setName(name);
    }

    public ClientCacheConfig(ClientCacheConfig config) {
        super(config);
        this.nearCacheConfig = config.nearCacheConfig != null ? new NearCacheConfig(config.nearCacheConfig) : null;
    }

    public NearCacheConfig getNearCacheConfig() {
        return nearCacheConfig;
    }

    public ClientCacheConfig setNearCacheConfig(NearCacheConfig nearCacheConfig) {
        this.nearCacheConfig = nearCacheConfig;
        return this;
    }

    public boolean isNearCacheEnabled() {
        return nearCacheConfig != null;
    }
}
