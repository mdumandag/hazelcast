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

package com.hazelcast.internal.serialization.impl.compact.schema;

import com.hazelcast.cluster.Address;
import com.hazelcast.cluster.ClusterState;
import com.hazelcast.cluster.Member;
import com.hazelcast.internal.cluster.ClusterService;
import com.hazelcast.internal.serialization.impl.compact.Schema;
import com.hazelcast.internal.serialization.impl.compact.SchemaService;
import com.hazelcast.internal.services.ManagedService;
import com.hazelcast.internal.services.PreJoinAwareService;
import com.hazelcast.logging.ILogger;
import com.hazelcast.spi.impl.NodeEngine;
import com.hazelcast.spi.impl.operationservice.Operation;
import com.hazelcast.spi.impl.operationservice.OperationService;
import com.hazelcast.spi.impl.operationservice.impl.InvocationFuture;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static com.hazelcast.internal.util.InvocationUtil.invokeOnStableClusterSerial;

public class MemberSchemaService implements ManagedService, PreJoinAwareService, SchemaService {

    public static final String SERVICE_NAME = "schema-service";
    private static final int MAX_RETRIES = 100;
    private final Map<Long, Schema> schemas = new ConcurrentHashMap<>();
    private final Set<Schema> seenSchemas = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private ILogger logger;
    private NodeEngine nodeEngine;

    @Override
    public void init(NodeEngine nodeEngine, Properties properties) {
        this.logger = nodeEngine.getLogger(SchemaService.class);
        this.nodeEngine = nodeEngine;
    }

    @Override
    public void reset() {
    }

    @Override
    public void shutdown(boolean terminate) {
        schemas.clear();
    }

    @Override
    public Operation getPreJoinOperation() {
        if (schemas.size() == 0) {
            return null;
        }
        if (logger.isFinestEnabled()) {
            logger.finest("Preparing prejoin operation with schemas " + schemas);
        }
        return new SendAllSchemasOperation(schemas);
    }

    @Override
    public Schema get(long schemaId) {
        return getAsync(schemaId).join();
    }

    public CompletableFuture<Schema> getAsync(long schemaId) {
        Schema schema = getLocal(schemaId);
        if (schema != null) {
            return CompletableFuture.completedFuture(schema);
        }
        if (logger.isFinestEnabled()) {
            logger.finest("Could not find schema id  " + schemaId + " locally, will search on the cluster" + schemaId);
        }
        ClusterService cluster = nodeEngine.getClusterService();
        OperationService operationService = nodeEngine.getOperationService();
        Set<Member> members = cluster.getMembers();
        Iterator<Member> iterator = members.iterator();
        return searchClusterAsync(schemaId, iterator, operationService);
    }

    private CompletableFuture<Schema> searchClusterAsync(long schemaId, Iterator<Member> iterator,
                                                         OperationService operationService) {
        if (!iterator.hasNext()) {
            return CompletableFuture.completedFuture(null);
        }
        Address address = iterator.next().getAddress();
        FetchSchemaOperation op = new FetchSchemaOperation(schemaId);
        InvocationFuture<Schema> future = operationService.invokeOnTarget(SERVICE_NAME, op, address);
        return future.handle((data, throwable) -> {
            //handle the exception and carry it to next `thenCompose` method
            if (throwable != null) {
                return throwable;
            }
            return data;
        }).thenCompose(o -> {
            if (o instanceof Throwable || o == null) {
                return searchClusterAsync(schemaId, iterator, operationService);
            }
            Schema retrievedSchema = (Schema) o;
            putLocal(schemaId, retrievedSchema);
            return CompletableFuture.completedFuture(getLocal(schemaId));
        });
    }

    public Schema getLocal(long schemaId) {
        return schemas.get(schemaId);
    }

    @Override
    public void put(Schema schema) {
        if (!seenSchemas.add(schema)) {
            //this is to prevent converting every schema put to data and calculate fingerprint
            return;
        }
        long schemaId = schema.getSchemaId();
        if (putIfAbsent(schemaId, schema)) {
            if (logger.isFinestEnabled()) {
                logger.finest("Sending schema id  " + schemaId + " locally, will search on the cluster" + schemaId);
            }
            if (nodeEngine.getClusterService().getClusterState().equals(ClusterState.PASSIVE)) {
                return;
            }
            invokeOnStableClusterSerial(nodeEngine, () -> new SendSchemaOperation(schemaId, schema), MAX_RETRIES)
                    .joinInternal();
        }
    }

    public CompletableFuture<Void> putAsync(long schemaId, Schema schema) {
        schema.setSchemaId(schemaId);
        if (putIfAbsent(schemaId, schema)) {
            if (nodeEngine.getClusterService().getClusterState().equals(ClusterState.PASSIVE)) {
                return CompletableFuture.completedFuture(null);
            }
            return invokeOnStableClusterSerial(nodeEngine, () -> new SendSchemaOperation(schemaId, schema), MAX_RETRIES);
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

    @Nonnull
    public CompletableFuture<Void> putAllAsync(List<Map.Entry<Long, Schema>> parameters) {
        ArrayList<CompletableFuture<Void>> futures = new ArrayList<>(parameters.size());
        if (logger.isFinestEnabled()) {
            logger.finest("Putting schemas to the cluster" + parameters);
        }
        for (Map.Entry<Long, Schema> parameter : parameters) {
            futures.add(putAsync(parameter.getKey(), parameter.getValue()));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    public void putLocal(long schemaId, Schema schema) {
        schema.setSchemaId(schemaId);
        putIfAbsent(schemaId, schema);
    }

    public boolean putIfAbsent(long schemaId, Schema schema) {
        Schema existingSchema = schemas.putIfAbsent(schemaId, schema);
        if (existingSchema == null) {
            return true;
        }
        if (!schema.equals(existingSchema)) {
            throw new IllegalStateException("Schema with schemaId " + schemaId + " already exists. Existing schema "
                    + existingSchema + "new schema " + schema);
        }
        return false;
    }

}