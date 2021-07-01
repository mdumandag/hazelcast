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

import com.hazelcast.internal.nio.BufferObjectDataInput;
import com.hazelcast.internal.util.collection.Long2ObjectHashMap;

import java.io.IOException;
import java.util.Map;

public class LazySchemaReader {

    private final Map<Long, LazySchema> schemas;

    public LazySchemaReader(BufferObjectDataInput in, int schemaStartPosition) throws IOException {
        int originalPosition = in.position();

        in.position(schemaStartPosition);

        int schemaCount = in.readInt();
        schemas = new Long2ObjectHashMap<>(schemaCount);

        for (int i = 0; i < schemaCount; i++) {
            long schemaId = in.readLong();
            int lengthOfSchema = in.readInt();
            LazySchema schema = new LazySchema(in, in.position());
            schemas.put(schemaId, schema);
            in.skipBytes(lengthOfSchema);
        }

        in.position(originalPosition);
    }

    public Schema getSchema(long schemaId) throws IOException {
        LazySchema schema = schemas.get(schemaId);
        if (schema != null) {
            schema.init();
        }
        return schema;
    }

}
