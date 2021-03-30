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
 * THIS MESSAGE IS NO LONGER USED BUT KEPT FOR BACKWARD COMPATIBILITY TESTS
 * Starts execution of an SQL query.
 */
@Generated("6f92cedb5d162e143ed20022d4fa5587")
public final class ServerSqlExecute_reservedCodec {
    //hex: 0x210100
    public static final int REQUEST_MESSAGE_TYPE = 2162944;
    //hex: 0x210101
    public static final int RESPONSE_MESSAGE_TYPE = 2162945;
    private static final int REQUEST_TIMEOUT_MILLIS_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_CURSOR_BUFFER_SIZE_FIELD_OFFSET = REQUEST_TIMEOUT_MILLIS_FIELD_OFFSET + LONG_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_CURSOR_BUFFER_SIZE_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int RESPONSE_ROW_PAGE_LAST_FIELD_OFFSET = RESPONSE_BACKUP_ACKS_FIELD_OFFSET + BYTE_SIZE_IN_BYTES;
    private static final int RESPONSE_UPDATE_COUNT_FIELD_OFFSET = RESPONSE_ROW_PAGE_LAST_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_UPDATE_COUNT_FIELD_OFFSET + LONG_SIZE_IN_BYTES;

    private ServerSqlExecute_reservedCodec() {
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class RequestParameters {

        /**
         * Query string.
         */
        public String sql;

        /**
         * Query parameters.
         */
        public java.util.List<com.hazelcast.internal.serialization.Data> parameters;

        /**
         * Timeout in milliseconds.
         */
        public long timeoutMillis;

        /**
         * Cursor buffer size.
         */
        public int cursorBufferSize;
    }

    public static RequestParameters decodeRequest(ClientMessage clientMessage) {
        ForwardFrameIterator iterator = clientMessage.frameIterator();
        RequestParameters request = new RequestParameters();
        Frame initialFrame = iterator.next();
        request.timeoutMillis = decodeLong(initialFrame.content, REQUEST_TIMEOUT_MILLIS_FIELD_OFFSET);
        request.cursorBufferSize = decodeInt(initialFrame.content, REQUEST_CURSOR_BUFFER_SIZE_FIELD_OFFSET);
        request.sql = StringCodec.decode(iterator);
        request.parameters = ListMultiFrameCodec.decode(iterator, DataCodec::decode);
        return request;
    }

    public static ClientMessage encodeResponse(@Nullable com.hazelcast.sql.impl.QueryId queryId, @Nullable java.util.List<com.hazelcast.sql.SqlColumnMetadata> rowMetadata, @Nullable java.util.Collection<java.util.Collection<com.hazelcast.internal.serialization.Data>> rowPage, boolean rowPageLast, long updateCount, @Nullable com.hazelcast.sql.impl.client.SqlError error) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        Frame initialFrame = new Frame(new byte[RESPONSE_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, RESPONSE_MESSAGE_TYPE);
        encodeBoolean(initialFrame.content, RESPONSE_ROW_PAGE_LAST_FIELD_OFFSET, rowPageLast);
        encodeLong(initialFrame.content, RESPONSE_UPDATE_COUNT_FIELD_OFFSET, updateCount);
        clientMessage.add(initialFrame);

        CodecUtil.encodeNullable(clientMessage, queryId, SqlQueryIdCodec::encode);
        ListMultiFrameCodec.encodeNullable(clientMessage, rowMetadata, SqlColumnMetadataCodec::encode);
        ListMultiFrameCodec.encodeNullable(clientMessage, rowPage, ListCNDataCodec::encode);
        CodecUtil.encodeNullable(clientMessage, error, SqlErrorCodec::encode);
        return clientMessage;
    }

}
