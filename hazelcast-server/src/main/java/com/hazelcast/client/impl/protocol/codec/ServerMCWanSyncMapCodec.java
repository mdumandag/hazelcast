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
 * Initiate WAN sync for a specific map or all maps
 */
@Generated("5ccc06ac05a97e799f87b9a6901e6548")
public final class ServerMCWanSyncMapCodec {
    //hex: 0x201600
    public static final int REQUEST_MESSAGE_TYPE = 2102784;
    //hex: 0x201601
    public static final int RESPONSE_MESSAGE_TYPE = 2102785;
    private static final int REQUEST_WAN_SYNC_TYPE_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_WAN_SYNC_TYPE_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int RESPONSE_UUID_FIELD_OFFSET = RESPONSE_BACKUP_ACKS_FIELD_OFFSET + BYTE_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_UUID_FIELD_OFFSET + UUID_SIZE_IN_BYTES;

    private ServerMCWanSyncMapCodec() {
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class RequestParameters {

        /**
         * Name of the WAN replication to initiate WAN sync for
         */
        public String wanReplicationName;

        /**
         * ID of the WAN publisher to initiate WAN sync for
         */
        public String wanPublisherId;

        /**
         * Whether all maps are going to be synced or only a single one:
         * 0 - ALL_MAPS
         * 1 - SINGLE_MAP
         */
        public int wanSyncType;

        /**
         * Name of the map to trigger WAN sync on, null if all maps are to be synced
         */
        public @Nullable String mapName;
    }

    public static RequestParameters decodeRequest(ClientMessage clientMessage) {
        ForwardFrameIterator iterator = clientMessage.frameIterator();
        RequestParameters request = new RequestParameters();
        Frame initialFrame = iterator.next();
        request.wanSyncType = decodeInt(initialFrame.content, REQUEST_WAN_SYNC_TYPE_FIELD_OFFSET);
        request.wanReplicationName = StringCodec.decode(iterator);
        request.wanPublisherId = StringCodec.decode(iterator);
        request.mapName = CodecUtil.decodeNullable(iterator, StringCodec::decode);
        return request;
    }

    public static ClientMessage encodeResponse(java.util.UUID uuid) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        Frame initialFrame = new Frame(new byte[RESPONSE_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, RESPONSE_MESSAGE_TYPE);
        encodeUUID(initialFrame.content, RESPONSE_UUID_FIELD_OFFSET, uuid);
        clientMessage.add(initialFrame);

        return clientMessage;
    }

}
