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

import com.hazelcast.internal.serialization.impl.FactoryIdHelper;

import static com.hazelcast.internal.serialization.impl.FactoryIdHelper.PARTITION_DS_FACTORY;
import static com.hazelcast.internal.serialization.impl.FactoryIdHelper.PARTITION_DS_FACTORY_ID;

public class PartitionDataSerializerHookBase {
    public static final int F_ID = FactoryIdHelper.getFactoryId(PARTITION_DS_FACTORY, PARTITION_DS_FACTORY_ID);

    public static final int PARTITION_RUNTIME_STATE = 1;
    public static final int ASSIGN_PARTITIONS = 2;
    public static final int PARTITION_BACKUP_REPLICA_ANTI_ENTROPY = 3;
    public static final int FETCH_PARTITION_STATE = 4;
    public static final int HAS_ONGOING_MIGRATION = 5;
    public static final int MIGRATION_COMMIT = 6;
    public static final int PARTITION_STATE_OP = 7;
    public static final int PROMOTION_COMMIT = 8;
    public static final int REPLICA_SYNC_REQUEST = 9;
    public static final int REPLICA_SYNC_RESPONSE = 10;
    public static final int REPLICA_SYNC_RETRY_RESPONSE = 11;
    public static final int SAFE_STATE_CHECK = 12;
    public static final int SHUTDOWN_REQUEST = 13;
    public static final int SHUTDOWN_RESPONSE = 14;
    public static final int REPLICA_FRAGMENT_MIGRATION_STATE = 15;
    public static final int MIGRATION = 16;
    public static final int MIGRATION_REQUEST = 17;
    public static final int NON_FRAGMENTED_SERVICE_NAMESPACE = 18;
    public static final int PARTITION_REPLICA = 19;
    public static final int PUBLISH_COMPLETED_MIGRATIONS = 20;
    public static final int PARTITION_STATE_CHECK_OP = 21;
    public static final int REPLICA_MIGRATION_EVENT = 22;
    public static final int MIGRATION_EVENT = 23;
    public static final int PARTITION_LOST_EVENT = 24;

    protected static final int LEN = PARTITION_LOST_EVENT + 1;
}
