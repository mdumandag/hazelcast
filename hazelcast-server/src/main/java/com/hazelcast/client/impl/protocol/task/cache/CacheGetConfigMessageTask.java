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

package com.hazelcast.client.impl.protocol.task.cache;

import com.hazelcast.cache.impl.operation.CacheGetConfigOperation;
import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.client.impl.protocol.codec.ServerCacheGetConfigCodec;
import com.hazelcast.client.impl.protocol.codec.holder.CacheConfigHolder;
import com.hazelcast.client.impl.protocol.task.AbstractTargetMessageTask;
import com.hazelcast.config.CacheConfig;
import com.hazelcast.instance.impl.Node;
import com.hazelcast.internal.nio.Connection;
import com.hazelcast.spi.impl.operationservice.Operation;

import java.security.Permission;
import java.util.UUID;

import static com.hazelcast.cache.impl.ICacheService.SERVICE_NAME;

/**
 * This client request specifically calls {@link CacheGetConfigOperation} on the server side.
 *
 * @see CacheGetConfigOperation
 */
public class CacheGetConfigMessageTask
        extends AbstractTargetMessageTask<ServerCacheGetConfigCodec.RequestParameters> {

    public CacheGetConfigMessageTask(ClientMessage clientMessage, Node node, Connection connection) {
        super(clientMessage, node, connection);
    }

    @Override
    protected UUID getTargetUuid() {
        return nodeEngine.getClusterService().getLocalMember().getUuid();
    }

    @Override
    protected Operation prepareOperation() {
        return new CacheGetConfigOperation(parameters.name, parameters.simpleName);
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    @Override
    public String getMethodName() {
        return null;
    }

    @Override
    public Object[] getParameters() {
        return null;
    }

    @Override
    public Permission getRequiredPermission() {
        return null;
    }

    @Override
    protected ServerCacheGetConfigCodec.RequestParameters decodeClientMessage(ClientMessage clientMessage) {
        return ServerCacheGetConfigCodec.decodeRequest(clientMessage);
    }

    @Override
    protected ClientMessage encodeResponse(Object response) {
        CacheConfig cacheConfig = (CacheConfig) response;

        return ServerCacheGetConfigCodec.encodeResponse(CacheConfigHolder.of(cacheConfig, serializationService));
    }

    @Override
    public String getDistributedObjectName() {
        return parameters.name;
    }
}
