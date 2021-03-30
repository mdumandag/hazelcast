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

package com.hazelcast.internal.partition.impl;

import com.hazelcast.internal.partition.MigrationStateImpl;
import com.hazelcast.internal.partition.PartitionLostEventImpl;
import com.hazelcast.internal.partition.ReplicaMigrationEventImpl;
import com.hazelcast.internal.serialization.DataSerializerHook;
import com.hazelcast.internal.serialization.impl.ArrayDataSerializableFactory;
import com.hazelcast.internal.util.ConstructorFunction;
import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public final class PartitionDataSerializerHook extends PartitionDataSerializerHookBase implements DataSerializerHook {

    @Override
    public int getFactoryId() {
        return F_ID;
    }

    @Override
    public DataSerializableFactory createFactory() {
        ConstructorFunction<Integer, IdentifiedDataSerializable>[] constructors = new ConstructorFunction[LEN];

//        constructors[PARTITION_RUNTIME_STATE] = arg -> new PartitionRuntimeState();
//        constructors[ASSIGN_PARTITIONS] = arg -> new AssignPartitions();
//        constructors[PARTITION_BACKUP_REPLICA_ANTI_ENTROPY] = arg -> new PartitionBackupReplicaAntiEntropyOperation();
//        constructors[FETCH_PARTITION_STATE] = arg -> new FetchPartitionStateOperation();
//        constructors[HAS_ONGOING_MIGRATION] = arg -> new HasOngoingMigration();
//        constructors[MIGRATION_COMMIT] = arg -> new MigrationCommitOperation();
//        constructors[PARTITION_STATE_OP] = arg -> new PartitionStateOperation();
//        constructors[PROMOTION_COMMIT] = arg -> new PromotionCommitOperation();
//        constructors[REPLICA_SYNC_REQUEST] = arg -> new PartitionReplicaSyncRequest();
//        constructors[REPLICA_SYNC_RESPONSE] = arg -> new PartitionReplicaSyncResponse();
//        constructors[REPLICA_SYNC_RETRY_RESPONSE] = arg -> new PartitionReplicaSyncRetryResponse();
//        constructors[SAFE_STATE_CHECK] = arg -> new SafeStateCheckOperation();
//        constructors[SHUTDOWN_REQUEST] = arg -> new ShutdownRequestOperation();
//        constructors[SHUTDOWN_RESPONSE] = arg -> new ShutdownResponseOperation();
//        constructors[REPLICA_FRAGMENT_MIGRATION_STATE] = arg -> new ReplicaFragmentMigrationState();
//        constructors[MIGRATION] = arg -> new MigrationOperation();
//        constructors[MIGRATION_REQUEST] = arg -> new MigrationRequestOperation();
//        constructors[NON_FRAGMENTED_SERVICE_NAMESPACE] = arg -> NonFragmentedServiceNamespace.INSTANCE;
//        constructors[PARTITION_REPLICA] = arg -> new PartitionReplica();
//        constructors[PUBLISH_COMPLETED_MIGRATIONS] = arg -> new PublishCompletedMigrationsOperation();
//        constructors[PARTITION_STATE_CHECK_OP] = arg -> new PartitionStateCheckOperation();
        constructors[REPLICA_MIGRATION_EVENT] = arg -> new ReplicaMigrationEventImpl();
        constructors[MIGRATION_EVENT] = arg -> new MigrationStateImpl();
        constructors[PARTITION_LOST_EVENT] = arg -> new PartitionLostEventImpl();
        return new ArrayDataSerializableFactory(constructors);
    }
}
