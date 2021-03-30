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
 * Reads from the map event journal in batches. You may specify the start sequence,
 * the minumum required number of items in the response, the maximum number of items
 * in the response, a predicate that the events should pass and a projection to
 * apply to the events in the journal.
 * If the event journal currently contains less events than {@code minSize}, the
 * call will wait until it has sufficient items.
 * The predicate, filter and projection may be {@code null} in which case all elements are returned
 * and no projection is applied.
 */
@Generated("d8864806e4332231a6d8db07b9f090a2")
public final class MapEventJournalReadCodec {
    //hex: 0x014200
    public static final int REQUEST_MESSAGE_TYPE = 82432;
    //hex: 0x014201
    public static final int RESPONSE_MESSAGE_TYPE = 82433;
    private static final int REQUEST_START_SEQUENCE_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_MIN_SIZE_FIELD_OFFSET = REQUEST_START_SEQUENCE_FIELD_OFFSET + LONG_SIZE_IN_BYTES;
    private static final int REQUEST_MAX_SIZE_FIELD_OFFSET = REQUEST_MIN_SIZE_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_MAX_SIZE_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int RESPONSE_READ_COUNT_FIELD_OFFSET = RESPONSE_BACKUP_ACKS_FIELD_OFFSET + BYTE_SIZE_IN_BYTES;
    private static final int RESPONSE_NEXT_SEQ_FIELD_OFFSET = RESPONSE_READ_COUNT_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_NEXT_SEQ_FIELD_OFFSET + LONG_SIZE_IN_BYTES;

    private MapEventJournalReadCodec() {
    }

    public static ClientMessage encodeRequest(String name, long startSequence, int minSize, int maxSize, @Nullable com.hazelcast.internal.serialization.Data predicate, @Nullable com.hazelcast.internal.serialization.Data projection) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        clientMessage.setRetryable(true);
        clientMessage.setOperationName("Map.EventJournalRead");
        Frame initialFrame = new Frame(new byte[REQUEST_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, REQUEST_MESSAGE_TYPE);
        encodeInt(initialFrame.content, PARTITION_ID_FIELD_OFFSET, -1);
        encodeLong(initialFrame.content, REQUEST_START_SEQUENCE_FIELD_OFFSET, startSequence);
        encodeInt(initialFrame.content, REQUEST_MIN_SIZE_FIELD_OFFSET, minSize);
        encodeInt(initialFrame.content, REQUEST_MAX_SIZE_FIELD_OFFSET, maxSize);
        clientMessage.add(initialFrame);
        StringCodec.encode(clientMessage, name);
        CodecUtil.encodeNullable(clientMessage, predicate, DataCodec::encode);
        CodecUtil.encodeNullable(clientMessage, projection, DataCodec::encode);
        return clientMessage;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class ResponseParameters {

        /**
         * Number of items that have been read.
         */
        public int readCount;

        /**
         * List of items that have been read.
         */
        public java.util.List<com.hazelcast.internal.serialization.Data> items;

        /**
         * Sequence numbers of items in the event journal.
         */
        public @Nullable long[] itemSeqs;

        /**
         * Sequence number of the item following the last read item.
         */
        public long nextSeq;
    }

    public static ResponseParameters decodeResponse(ClientMessage clientMessage) {
        ForwardFrameIterator iterator = clientMessage.frameIterator();
        ResponseParameters response = new ResponseParameters();
        Frame initialFrame = iterator.next();
        response.readCount = decodeInt(initialFrame.content, RESPONSE_READ_COUNT_FIELD_OFFSET);
        response.nextSeq = decodeLong(initialFrame.content, RESPONSE_NEXT_SEQ_FIELD_OFFSET);
        response.items = ListMultiFrameCodec.decode(iterator, DataCodec::decode);
        response.itemSeqs = CodecUtil.decodeNullable(iterator, LongArrayCodec::decode);
        return response;
    }

}