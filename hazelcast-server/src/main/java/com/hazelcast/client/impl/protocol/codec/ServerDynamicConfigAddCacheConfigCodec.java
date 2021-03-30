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

package com.hazelcast.client.impl.protocol.codec;

import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.client.impl.protocol.Generated;
import com.hazelcast.client.impl.protocol.codec.builtin.*;
import com.hazelcast.client.impl.protocol.codec.custom.*;

import javax.annotation.Nullable;

import static com.hazelcast.client.impl.protocol.ClientMessage.*;
import static com.hazelcast.client.impl.protocol.codec.builtin.FixedSizeTypesCodec.*;

/*
 * This file is auto-generated by the Hazelcast Client Protocol Code Generator.
 * To change this file, edit the templates or the protocol
 * definitions on the https://github.com/hazelcast/hazelcast-client-protocol
 * and regenerate it.
 */

/**
 * Adds a new cache configuration to a running cluster.
 * If a cache configuration with the given {@code name} already exists, then
 * the new configuration is ignored and the existing one is preserved.
 */
@Generated("4f802348b3be206274360e75712b5528")
public final class ServerDynamicConfigAddCacheConfigCodec {
    //hex: 0x1B0E00
    public static final int REQUEST_MESSAGE_TYPE = 1773056;
    //hex: 0x1B0E01
    public static final int RESPONSE_MESSAGE_TYPE = 1773057;
    private static final int REQUEST_STATISTICS_ENABLED_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_MANAGEMENT_ENABLED_FIELD_OFFSET = REQUEST_STATISTICS_ENABLED_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;
    private static final int REQUEST_READ_THROUGH_FIELD_OFFSET = REQUEST_MANAGEMENT_ENABLED_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;
    private static final int REQUEST_WRITE_THROUGH_FIELD_OFFSET = REQUEST_READ_THROUGH_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;
    private static final int REQUEST_BACKUP_COUNT_FIELD_OFFSET = REQUEST_WRITE_THROUGH_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;
    private static final int REQUEST_ASYNC_BACKUP_COUNT_FIELD_OFFSET = REQUEST_BACKUP_COUNT_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_MERGE_BATCH_SIZE_FIELD_OFFSET = REQUEST_ASYNC_BACKUP_COUNT_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_DISABLE_PER_ENTRY_INVALIDATION_EVENTS_FIELD_OFFSET = REQUEST_MERGE_BATCH_SIZE_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_DISABLE_PER_ENTRY_INVALIDATION_EVENTS_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_BACKUP_ACKS_FIELD_OFFSET + BYTE_SIZE_IN_BYTES;

    private ServerDynamicConfigAddCacheConfigCodec() {
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class RequestParameters {

        /**
         * cache name
         */
        public String name;

        /**
         * class name of key type
         */
        public @Nullable String keyType;

        /**
         * class name of value type
         */
        public @Nullable String valueType;

        /**
         * {@code true} to enable gathering of statistics, otherwise {@code false}
         */
        public boolean statisticsEnabled;

        /**
         * {@code true} to enable management interface on this cache or {@code false}
         */
        public boolean managementEnabled;

        /**
         * {@code true} to enable read through from a {@code CacheLoader}
         */
        public boolean readThrough;

        /**
         * {@code true} to enable write through to a {@code CacheWriter}
         */
        public boolean writeThrough;

        /**
         * name of cache loader factory class, if one is configured
         */
        public @Nullable String cacheLoaderFactory;

        /**
         * name of cache writer factory class, if one is configured
         */
        public @Nullable String cacheWriterFactory;

        /**
         * Factory name of cache loader factory class, if one is configured
         */
        public @Nullable String cacheLoader;

        /**
         * Factory name of cache writer factory class, if one is configured
         */
        public @Nullable String cacheWriter;

        /**
         * number of synchronous backups
         */
        public int backupCount;

        /**
         * number of asynchronous backups
         */
        public int asyncBackupCount;

        /**
         * data type used to store entries. Valid values are {@code BINARY},
         * {@code OBJECT} and {@code NATIVE}.
         */
        public String inMemoryFormat;

        /**
         * name of an existing configured split brain protection to be used to determine the minimum
         * number of members required in the cluster for the cache to remain functional.
         * When {@code null}, split brain protection does not apply to this cache's operations.
         */
        public @Nullable String splitBrainProtectionName;

        /**
         * name of a class implementing SplitBrainMergePolicy
         * that handles merging of values for this cache while recovering from
         * network partitioning
         */
        public @Nullable String mergePolicy;

        /**
         * number of entries to be sent in a merge operation
         */
        public int mergeBatchSize;

        /**
         * when {@code true} disables invalidation events for per entry but
         * full-flush invalidation events are still enabled.
         */
        public boolean disablePerEntryInvalidationEvents;

        /**
         * partition lost listener configurations
         */
        public @Nullable java.util.List<com.hazelcast.client.impl.protocol.task.dynamicconfig.ListenerConfigHolder> partitionLostListenerConfigs;

        /**
         * expiry policy factory class name. When configuring an expiry policy,
         * either this or {@ode timedExpiryPolicyFactoryConfig} should be configured.
         */
        public @Nullable String expiryPolicyFactoryClassName;

        /**
         * expiry policy factory with duration configuration
         */
        public @Nullable com.hazelcast.config.CacheSimpleConfig.ExpiryPolicyFactoryConfig.TimedExpiryPolicyFactoryConfig timedExpiryPolicyFactoryConfig;

        /**
         * cache entry listeners configuration
         */
        public @Nullable java.util.List<com.hazelcast.config.CacheSimpleEntryListenerConfig> cacheEntryListeners;

        /**
         * cache eviction configuration
         */
        public @Nullable com.hazelcast.client.impl.protocol.task.dynamicconfig.EvictionConfigHolder evictionConfig;

        /**
         * reference to an existing WAN replication configuration
         */
        public @Nullable com.hazelcast.config.WanReplicationRef wanReplicationRef;

        /**
         * Event Journal configuration
         */
        public @Nullable com.hazelcast.config.EventJournalConfig eventJournalConfig;

        /**
         * hot restart configuration
         */
        public @Nullable com.hazelcast.config.HotRestartConfig hotRestartConfig;
    }

    public static RequestParameters decodeRequest(ClientMessage clientMessage) {
        ForwardFrameIterator iterator = clientMessage.frameIterator();
        RequestParameters request = new RequestParameters();
        Frame initialFrame = iterator.next();
        request.statisticsEnabled = decodeBoolean(initialFrame.content, REQUEST_STATISTICS_ENABLED_FIELD_OFFSET);
        request.managementEnabled = decodeBoolean(initialFrame.content, REQUEST_MANAGEMENT_ENABLED_FIELD_OFFSET);
        request.readThrough = decodeBoolean(initialFrame.content, REQUEST_READ_THROUGH_FIELD_OFFSET);
        request.writeThrough = decodeBoolean(initialFrame.content, REQUEST_WRITE_THROUGH_FIELD_OFFSET);
        request.backupCount = decodeInt(initialFrame.content, REQUEST_BACKUP_COUNT_FIELD_OFFSET);
        request.asyncBackupCount = decodeInt(initialFrame.content, REQUEST_ASYNC_BACKUP_COUNT_FIELD_OFFSET);
        request.mergeBatchSize = decodeInt(initialFrame.content, REQUEST_MERGE_BATCH_SIZE_FIELD_OFFSET);
        request.disablePerEntryInvalidationEvents = decodeBoolean(initialFrame.content, REQUEST_DISABLE_PER_ENTRY_INVALIDATION_EVENTS_FIELD_OFFSET);
        request.name = StringCodec.decode(iterator);
        request.keyType = CodecUtil.decodeNullable(iterator, StringCodec::decode);
        request.valueType = CodecUtil.decodeNullable(iterator, StringCodec::decode);
        request.cacheLoaderFactory = CodecUtil.decodeNullable(iterator, StringCodec::decode);
        request.cacheWriterFactory = CodecUtil.decodeNullable(iterator, StringCodec::decode);
        request.cacheLoader = CodecUtil.decodeNullable(iterator, StringCodec::decode);
        request.cacheWriter = CodecUtil.decodeNullable(iterator, StringCodec::decode);
        request.inMemoryFormat = StringCodec.decode(iterator);
        request.splitBrainProtectionName = CodecUtil.decodeNullable(iterator, StringCodec::decode);
        request.mergePolicy = CodecUtil.decodeNullable(iterator, StringCodec::decode);
        request.partitionLostListenerConfigs = ListMultiFrameCodec.decodeNullable(iterator, ListenerConfigHolderCodec::decode);
        request.expiryPolicyFactoryClassName = CodecUtil.decodeNullable(iterator, StringCodec::decode);
        request.timedExpiryPolicyFactoryConfig = CodecUtil.decodeNullable(iterator, TimedExpiryPolicyFactoryConfigCodec::decode);
        request.cacheEntryListeners = ListMultiFrameCodec.decodeNullable(iterator, CacheSimpleEntryListenerConfigCodec::decode);
        request.evictionConfig = CodecUtil.decodeNullable(iterator, EvictionConfigHolderCodec::decode);
        request.wanReplicationRef = CodecUtil.decodeNullable(iterator, WanReplicationRefCodec::decode);
        request.eventJournalConfig = CodecUtil.decodeNullable(iterator, EventJournalConfigCodec::decode);
        request.hotRestartConfig = CodecUtil.decodeNullable(iterator, HotRestartConfigCodec::decode);
        return request;
    }

    public static ClientMessage encodeResponse() {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        Frame initialFrame = new Frame(new byte[RESPONSE_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, RESPONSE_MESSAGE_TYPE);
        clientMessage.add(initialFrame);

        return clientMessage;
    }

}
