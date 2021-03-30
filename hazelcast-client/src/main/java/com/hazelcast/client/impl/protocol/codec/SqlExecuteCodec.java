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
 * Starts execution of an SQL query (as of 4.2).
 */
@Generated("4b0c17852c33f9a7162fe59ce194ef73")
public final class SqlExecuteCodec {
    //hex: 0x210400
    public static final int REQUEST_MESSAGE_TYPE = 2163712;
    //hex: 0x210401
    public static final int RESPONSE_MESSAGE_TYPE = 2163713;
    private static final int REQUEST_TIMEOUT_MILLIS_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_CURSOR_BUFFER_SIZE_FIELD_OFFSET = REQUEST_TIMEOUT_MILLIS_FIELD_OFFSET + LONG_SIZE_IN_BYTES;
    private static final int REQUEST_EXPECTED_RESULT_TYPE_FIELD_OFFSET = REQUEST_CURSOR_BUFFER_SIZE_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_EXPECTED_RESULT_TYPE_FIELD_OFFSET + BYTE_SIZE_IN_BYTES;
    private static final int RESPONSE_UPDATE_COUNT_FIELD_OFFSET = RESPONSE_BACKUP_ACKS_FIELD_OFFSET + BYTE_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_UPDATE_COUNT_FIELD_OFFSET + LONG_SIZE_IN_BYTES;

    private SqlExecuteCodec() {
    }

    public static ClientMessage encodeRequest(String sql, java.util.Collection<com.hazelcast.internal.serialization.Data> parameters, long timeoutMillis, int cursorBufferSize, @Nullable String schema, byte expectedResultType, com.hazelcast.sql.impl.QueryId queryId) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        clientMessage.setRetryable(false);
        clientMessage.setOperationName("Sql.Execute");
        Frame initialFrame = new Frame(new byte[REQUEST_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, REQUEST_MESSAGE_TYPE);
        encodeInt(initialFrame.content, PARTITION_ID_FIELD_OFFSET, -1);
        encodeLong(initialFrame.content, REQUEST_TIMEOUT_MILLIS_FIELD_OFFSET, timeoutMillis);
        encodeInt(initialFrame.content, REQUEST_CURSOR_BUFFER_SIZE_FIELD_OFFSET, cursorBufferSize);
        encodeByte(initialFrame.content, REQUEST_EXPECTED_RESULT_TYPE_FIELD_OFFSET, expectedResultType);
        clientMessage.add(initialFrame);
        StringCodec.encode(clientMessage, sql);
        ListMultiFrameCodec.encodeContainsNullable(clientMessage, parameters, DataCodec::encode);
        CodecUtil.encodeNullable(clientMessage, schema, StringCodec::encode);
        SqlQueryIdCodec.encode(clientMessage, queryId);
        return clientMessage;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class ResponseParameters {

        /**
         * Row metadata.
         */
        public @Nullable java.util.List<com.hazelcast.sql.SqlColumnMetadata> rowMetadata;

        /**
         * Row page.
         */
        public @Nullable com.hazelcast.sql.impl.client.SqlPage rowPage;

        /**
         * The number of updated rows.
         */
        public long updateCount;

        /**
         * Error object.
         */
        public @Nullable com.hazelcast.sql.impl.client.SqlError error;
    }

    public static ResponseParameters decodeResponse(ClientMessage clientMessage) {
        ForwardFrameIterator iterator = clientMessage.frameIterator();
        ResponseParameters response = new ResponseParameters();
        Frame initialFrame = iterator.next();
        response.updateCount = decodeLong(initialFrame.content, RESPONSE_UPDATE_COUNT_FIELD_OFFSET);
        response.rowMetadata = ListMultiFrameCodec.decodeNullable(iterator, SqlColumnMetadataCodec::decode);
        response.rowPage = CodecUtil.decodeNullable(iterator, SqlPageCodec::decode);
        response.error = CodecUtil.decodeNullable(iterator, SqlErrorCodec::decode);
        return response;
    }

}