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
 * Adds an continuous entry listener for this map. The listener will be notified for map add/remove/update/evict
 * events filtered by the given predicate.
 */
@Generated("cb3cf09efadd63db1c4a7023bbf562b1")
public final class ServerReplicatedMapAddEntryListenerToKeyWithPredicateCodec {
    //hex: 0x0D0A00
    public static final int REQUEST_MESSAGE_TYPE = 854528;
    //hex: 0x0D0A01
    public static final int RESPONSE_MESSAGE_TYPE = 854529;
    private static final int REQUEST_LOCAL_ONLY_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_LOCAL_ONLY_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;
    private static final int RESPONSE_RESPONSE_FIELD_OFFSET = RESPONSE_BACKUP_ACKS_FIELD_OFFSET + BYTE_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_RESPONSE_FIELD_OFFSET + UUID_SIZE_IN_BYTES;
    private static final int EVENT_ENTRY_EVENT_TYPE_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int EVENT_ENTRY_UUID_FIELD_OFFSET = EVENT_ENTRY_EVENT_TYPE_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int EVENT_ENTRY_NUMBER_OF_AFFECTED_ENTRIES_FIELD_OFFSET = EVENT_ENTRY_UUID_FIELD_OFFSET + UUID_SIZE_IN_BYTES;
    private static final int EVENT_ENTRY_INITIAL_FRAME_SIZE = EVENT_ENTRY_NUMBER_OF_AFFECTED_ENTRIES_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    //hex: 0x0D0A02
    private static final int EVENT_ENTRY_MESSAGE_TYPE = 854530;

    private ServerReplicatedMapAddEntryListenerToKeyWithPredicateCodec() {
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class RequestParameters {

        /**
         * Name of the Replicated Map
         */
        public String name;

        /**
         * Key with which the specified value is to be associated.
         */
        public com.hazelcast.internal.serialization.Data key;

        /**
         * The predicate for filtering entries
         */
        public com.hazelcast.internal.serialization.Data predicate;

        /**
         * if true fires events that originated from this node only, otherwise fires all events
         */
        public boolean localOnly;
    }

    public static RequestParameters decodeRequest(ClientMessage clientMessage) {
        ForwardFrameIterator iterator = clientMessage.frameIterator();
        RequestParameters request = new RequestParameters();
        Frame initialFrame = iterator.next();
        request.localOnly = decodeBoolean(initialFrame.content, REQUEST_LOCAL_ONLY_FIELD_OFFSET);
        request.name = StringCodec.decode(iterator);
        request.key = DataCodec.decode(iterator);
        request.predicate = DataCodec.decode(iterator);
        return request;
    }

    public static ClientMessage encodeResponse(java.util.UUID response) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        Frame initialFrame = new Frame(new byte[RESPONSE_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, RESPONSE_MESSAGE_TYPE);
        encodeUUID(initialFrame.content, RESPONSE_RESPONSE_FIELD_OFFSET, response);
        clientMessage.add(initialFrame);

        return clientMessage;
    }

    public static ClientMessage encodeEntryEvent(@Nullable com.hazelcast.internal.serialization.Data key, @Nullable com.hazelcast.internal.serialization.Data value, @Nullable com.hazelcast.internal.serialization.Data oldValue, @Nullable com.hazelcast.internal.serialization.Data mergingValue, int eventType, java.util.UUID uuid, int numberOfAffectedEntries) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        Frame initialFrame = new Frame(new byte[EVENT_ENTRY_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        initialFrame.flags |= ClientMessage.IS_EVENT_FLAG;
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, EVENT_ENTRY_MESSAGE_TYPE);
        encodeInt(initialFrame.content, PARTITION_ID_FIELD_OFFSET, -1);
        encodeInt(initialFrame.content, EVENT_ENTRY_EVENT_TYPE_FIELD_OFFSET, eventType);
        encodeUUID(initialFrame.content, EVENT_ENTRY_UUID_FIELD_OFFSET, uuid);
        encodeInt(initialFrame.content, EVENT_ENTRY_NUMBER_OF_AFFECTED_ENTRIES_FIELD_OFFSET, numberOfAffectedEntries);
        clientMessage.add(initialFrame);

        CodecUtil.encodeNullable(clientMessage, key, DataCodec::encode);
        CodecUtil.encodeNullable(clientMessage, value, DataCodec::encode);
        CodecUtil.encodeNullable(clientMessage, oldValue, DataCodec::encode);
        CodecUtil.encodeNullable(clientMessage, mergingValue, DataCodec::encode);
        return clientMessage;
    }
}