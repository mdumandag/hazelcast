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
 * Query operation to retrieve the current value of the PNCounter.
 * <p>
 * The invocation will return the replica timestamps (vector clock) which
 * can then be sent with the next invocation to keep session consistency
 * guarantees.
 * The target replica is determined by the {@code targetReplica} parameter.
 * If smart routing is disabled, the actual member processing the client
 * message may act as a proxy.
 */
@Generated("68eb46544933df5d2bf47a0a59ad0612")
public final class PNCounterGetCodec {
    //hex: 0x1D0100
    public static final int REQUEST_MESSAGE_TYPE = 1900800;
    //hex: 0x1D0101
    public static final int RESPONSE_MESSAGE_TYPE = 1900801;
    private static final int REQUEST_TARGET_REPLICA_UUID_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_TARGET_REPLICA_UUID_FIELD_OFFSET + UUID_SIZE_IN_BYTES;
    private static final int RESPONSE_VALUE_FIELD_OFFSET = RESPONSE_BACKUP_ACKS_FIELD_OFFSET + BYTE_SIZE_IN_BYTES;
    private static final int RESPONSE_REPLICA_COUNT_FIELD_OFFSET = RESPONSE_VALUE_FIELD_OFFSET + LONG_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_REPLICA_COUNT_FIELD_OFFSET + INT_SIZE_IN_BYTES;

    private PNCounterGetCodec() {
    }

    public static ClientMessage encodeRequest(String name, java.util.Collection<java.util.Map.Entry<java.util.UUID, Long>> replicaTimestamps, java.util.UUID targetReplicaUUID) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        clientMessage.setRetryable(true);
        clientMessage.setOperationName("PNCounter.Get");
        Frame initialFrame = new Frame(new byte[REQUEST_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, REQUEST_MESSAGE_TYPE);
        encodeInt(initialFrame.content, PARTITION_ID_FIELD_OFFSET, -1);
        encodeUUID(initialFrame.content, REQUEST_TARGET_REPLICA_UUID_FIELD_OFFSET, targetReplicaUUID);
        clientMessage.add(initialFrame);
        StringCodec.encode(clientMessage, name);
        EntryListUUIDLongCodec.encode(clientMessage, replicaTimestamps);
        return clientMessage;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class ResponseParameters {

        /**
         * Value of the counter.
         */
        public long value;

        /**
         * last observed replica timestamps (vector clock)
         */
        public java.util.List<java.util.Map.Entry<java.util.UUID, Long>> replicaTimestamps;

        /**
         * Number of replicas that keep the state of this counter.
         */
        public int replicaCount;
    }

    public static ResponseParameters decodeResponse(ClientMessage clientMessage) {
        ForwardFrameIterator iterator = clientMessage.frameIterator();
        ResponseParameters response = new ResponseParameters();
        Frame initialFrame = iterator.next();
        response.value = decodeLong(initialFrame.content, RESPONSE_VALUE_FIELD_OFFSET);
        response.replicaCount = decodeInt(initialFrame.content, RESPONSE_REPLICA_COUNT_FIELD_OFFSET);
        response.replicaTimestamps = EntryListUUIDLongCodec.decode(iterator);
        return response;
    }

}