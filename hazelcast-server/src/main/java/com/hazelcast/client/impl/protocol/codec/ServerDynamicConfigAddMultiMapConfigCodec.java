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
import com.hazelcast.client.impl.protocol.task.dynamicconfig.ListenerConfigHolder;

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
 * Adds a new multimap config to a running cluster.
 * If a multimap configuration with the given {@code name} already exists, then
 * the new multimap config is ignored and the existing one is preserved.
 */
@Generated("dc2b58f0369cded599a9291e354266db")
public final class ServerDynamicConfigAddMultiMapConfigCodec {
    //hex: 0x1B0100
    public static final int REQUEST_MESSAGE_TYPE = 1769728;
    //hex: 0x1B0101
    public static final int RESPONSE_MESSAGE_TYPE = 1769729;
    private static final int REQUEST_BINARY_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_BACKUP_COUNT_FIELD_OFFSET = REQUEST_BINARY_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;
    private static final int REQUEST_ASYNC_BACKUP_COUNT_FIELD_OFFSET = REQUEST_BACKUP_COUNT_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_STATISTICS_ENABLED_FIELD_OFFSET = REQUEST_ASYNC_BACKUP_COUNT_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_MERGE_BATCH_SIZE_FIELD_OFFSET = REQUEST_STATISTICS_ENABLED_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_MERGE_BATCH_SIZE_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_BACKUP_ACKS_FIELD_OFFSET + BYTE_SIZE_IN_BYTES;

    private ServerDynamicConfigAddMultiMapConfigCodec() {
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class RequestParameters {

        /**
         * multimap configuration name
         */
        public String name;

        /**
         * value collection type. Valid values are SET and LIST.
         */
        public String collectionType;

        /**
         * entry listener configurations
         */
        public @Nullable java.util.List<ListenerConfigHolder> listenerConfigs;

        /**
         * {@code true} to store values in {@code BINARY} format or {@code false} to store
         * values in {@code OBJECT} format.
         */
        public boolean binary;

        /**
         * number of synchronous backups
         */
        public int backupCount;

        /**
         * number of asynchronous backups
         */
        public int asyncBackupCount;

        /**
         * set to {@code true} to enable statistics on this multimap configuration
         */
        public boolean statisticsEnabled;

        /**
         * name of an existing configured split brain protection to be used to determine the minimum number of members
         * required in the cluster for the lock to remain functional. When {@code null}, split brain protection does not
         * apply to this lock configuration's operations.
         */
        public @Nullable String splitBrainProtectionName;

        /**
         * Name of a class implementing SplitBrainMergePolicy that handles merging of values for this cache
         * while recovering from network partitioning.
         */
        public String mergePolicy;

        /**
         * Number of entries to be sent in a merge operation.
         */
        public int mergeBatchSize;
    }

    public static RequestParameters decodeRequest(ClientMessage clientMessage) {
        ForwardFrameIterator iterator = clientMessage.frameIterator();
        RequestParameters request = new RequestParameters();
        Frame initialFrame = iterator.next();
        request.binary = decodeBoolean(initialFrame.content, REQUEST_BINARY_FIELD_OFFSET);
        request.backupCount = decodeInt(initialFrame.content, REQUEST_BACKUP_COUNT_FIELD_OFFSET);
        request.asyncBackupCount = decodeInt(initialFrame.content, REQUEST_ASYNC_BACKUP_COUNT_FIELD_OFFSET);
        request.statisticsEnabled = decodeBoolean(initialFrame.content, REQUEST_STATISTICS_ENABLED_FIELD_OFFSET);
        request.mergeBatchSize = decodeInt(initialFrame.content, REQUEST_MERGE_BATCH_SIZE_FIELD_OFFSET);
        request.name = StringCodec.decode(iterator);
        request.collectionType = StringCodec.decode(iterator);
        request.listenerConfigs = ListMultiFrameCodec.decodeNullable(iterator, ListenerConfigHolderCodec::decode);
        request.splitBrainProtectionName = CodecUtil.decodeNullable(iterator, StringCodec::decode);
        request.mergePolicy = StringCodec.decode(iterator);
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