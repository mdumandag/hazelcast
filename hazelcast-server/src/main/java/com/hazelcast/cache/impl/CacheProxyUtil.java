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
import com.hazelcast.internal.serialization.Data;
import com.hazelcast.internal.util.ExceptionUtil;
import com.hazelcast.spi.impl.NodeEngine;

import java.util.Map;
import java.util.Set;

import static com.hazelcast.internal.util.EmptyStatement.ignore;
import static com.hazelcast.internal.util.Preconditions.checkNotNull;

/**
 * Static util methods for {@linkplain com.hazelcast.cache.ICache} implementations.
 *
 * @see CacheProxy
 */
public final class CacheProxyUtil extends CacheProxyUtilBase {

    /**
     * Cache clear response validator, loop on results to validate that no exception exists on the result map.
     * Throws the first exception in the map.
     *
     * @param results map of {@link CacheClearResponse}.
     */
    public static void validateResults(Map<Integer, Object> results) {
        for (Object result : results.values()) {
            if (result != null && result instanceof CacheClearResponse) {
                Object response = ((CacheClearResponse) result).getResponse();
                if (response instanceof Throwable) {
                    ExceptionUtil.sneakyThrow((Throwable) response);
                }
            }
        }
    }

    public static int getPartitionId(NodeEngine nodeEngine, Data key) {
        return nodeEngine.getPartitionService().getPartitionId(key);
    }

}
