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
import com.hazelcast.logging.Logger;

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
 * Adds a partition lost listener to the cluster.
 */
@Generated("7a6718add92182eed15be93a3f479b87")
public final class ClientAddPartitionLostListenerCodec {
    //hex: 0x000600
    public static final int REQUEST_MESSAGE_TYPE = 1536;
    //hex: 0x000601
    public static final int RESPONSE_MESSAGE_TYPE = 1537;
    private static final int REQUEST_LOCAL_ONLY_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_LOCAL_ONLY_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;
    private static final int RESPONSE_RESPONSE_FIELD_OFFSET = RESPONSE_BACKUP_ACKS_FIELD_OFFSET + BYTE_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_RESPONSE_FIELD_OFFSET + UUID_SIZE_IN_BYTES;
    private static final int EVENT_PARTITION_LOST_PARTITION_ID_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int EVENT_PARTITION_LOST_LOST_BACKUP_COUNT_FIELD_OFFSET = EVENT_PARTITION_LOST_PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int EVENT_PARTITION_LOST_SOURCE_FIELD_OFFSET = EVENT_PARTITION_LOST_LOST_BACKUP_COUNT_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int EVENT_PARTITION_LOST_INITIAL_FRAME_SIZE = EVENT_PARTITION_LOST_SOURCE_FIELD_OFFSET + UUID_SIZE_IN_BYTES;
    //hex: 0x000602
    private static final int EVENT_PARTITION_LOST_MESSAGE_TYPE = 1538;

    private ClientAddPartitionLostListenerCodec() {
    }

    public static ClientMessage encodeRequest(boolean localOnly) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        clientMessage.setRetryable(false);
        clientMessage.setOperationName("Client.AddPartitionLostListener");
        Frame initialFrame = new Frame(new byte[REQUEST_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, REQUEST_MESSAGE_TYPE);
        encodeInt(initialFrame.content, PARTITION_ID_FIELD_OFFSET, -1);
        encodeBoolean(initialFrame.content, REQUEST_LOCAL_ONLY_FIELD_OFFSET, localOnly);
        clientMessage.add(initialFrame);
        return clientMessage;
    }


    /**
     * The listener registration id.
     */
    public static java.util.UUID decodeResponse(ClientMessage clientMessage) {
        ForwardFrameIterator iterator = clientMessage.frameIterator();
        Frame initialFrame = iterator.next();
        return decodeUUID(initialFrame.content, RESPONSE_RESPONSE_FIELD_OFFSET);
    }

    public abstract static class AbstractEventHandler {

        public void handle(ClientMessage clientMessage) {
            int messageType = clientMessage.getMessageType();
            ForwardFrameIterator iterator = clientMessage.frameIterator();
            if (messageType == EVENT_PARTITION_LOST_MESSAGE_TYPE) {
                Frame initialFrame = iterator.next();
                int partitionId = decodeInt(initialFrame.content, EVENT_PARTITION_LOST_PARTITION_ID_FIELD_OFFSET);
                int lostBackupCount = decodeInt(initialFrame.content, EVENT_PARTITION_LOST_LOST_BACKUP_COUNT_FIELD_OFFSET);
                java.util.UUID source = decodeUUID(initialFrame.content, EVENT_PARTITION_LOST_SOURCE_FIELD_OFFSET);
                handlePartitionLostEvent(partitionId, lostBackupCount, source);
                return;
            }
            Logger.getLogger(super.getClass()).finest("Unknown message type received on event handler :" + messageType);
        }

        /**
         * @param partitionId Id of the lost partition.
         * @param lostBackupCount The number of lost backups for the partition. 0: the owner, 1: first backup, 2: second backup...
         * @param source UUID of the node that dispatches the event
         */
        public abstract void handlePartitionLostEvent(int partitionId, int lostBackupCount, @Nullable java.util.UUID source);
    }
}