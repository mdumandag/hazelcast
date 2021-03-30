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
 * Stop, pause or resume WAN replication for the given WAN replication and publisher
 */
@Generated("e6f0d6e9f965c7ef4d859b9b7ca87309")
public final class MCChangeWanReplicationStateCodec {
    //hex: 0x201300
    public static final int REQUEST_MESSAGE_TYPE = 2102016;
    //hex: 0x201301
    public static final int RESPONSE_MESSAGE_TYPE = 2102017;
    private static final int REQUEST_NEW_STATE_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_NEW_STATE_FIELD_OFFSET + BYTE_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_BACKUP_ACKS_FIELD_OFFSET + BYTE_SIZE_IN_BYTES;

    private MCChangeWanReplicationStateCodec() {
    }

    public static ClientMessage encodeRequest(String wanReplicationName, String wanPublisherId, byte newState) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        clientMessage.setRetryable(false);
        clientMessage.setOperationName("MC.ChangeWanReplicationState");
        Frame initialFrame = new Frame(new byte[REQUEST_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, REQUEST_MESSAGE_TYPE);
        encodeInt(initialFrame.content, PARTITION_ID_FIELD_OFFSET, -1);
        encodeByte(initialFrame.content, REQUEST_NEW_STATE_FIELD_OFFSET, newState);
        clientMessage.add(initialFrame);
        StringCodec.encode(clientMessage, wanReplicationName);
        StringCodec.encode(clientMessage, wanPublisherId);
        return clientMessage;
    }



}