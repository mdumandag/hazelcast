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
import java.util.Set;

public class DefaultCompactWithSchemaWriter extends AbstractDefaultCompactWriter {

    private final CompactWithSchemaStreamSerializer serializer;
    private final Set<Schema> schemas;

    public DefaultCompactWithSchemaWriter(CompactWithSchemaStreamSerializer serializer, BufferObjectDataOutput out, Schema schema, Set<Schema> schemas) {
        super(out, schema);
        this.serializer = serializer;
        this.schemas = schemas;
    }

    @Override
    public void writeObjectInternal(BufferObjectDataOutput out, Object value) throws IOException {
        serializer.writeObject(out, value, schemas);
    }

    @Override
    public void writeGenericRecordInternal(BufferObjectDataOutput out, CompactGenericRecord value) throws IOException {
        serializer.writeObject(out, value, schemas);
    }
}
