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

package com.hazelcast.internal.serialization.impl.compact;

import com.hazelcast.internal.nio.BufferObjectDataOutput;

import java.io.IOException;

public class DefaultCompactWriter extends AbstractDefaultCompactWriter {

    private final CompactStreamSerializer serializer;

    public DefaultCompactWriter(CompactStreamSerializer serializer, BufferObjectDataOutput out, Schema schema) {
        super(out, schema);
        this.serializer = serializer;
    }

    @Override
    public void writeObjectInternal(BufferObjectDataOutput out, Object value) throws IOException {
        serializer.writeObject(out, value);
    }

    @Override
    public void writeGenericRecordInternal(BufferObjectDataOutput out, CompactGenericRecord value) throws IOException {
        serializer.writeGenericRecord(out, value);
    }
}
