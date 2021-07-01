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
import com.hazelcast.nio.serialization.GenericRecord;
import com.hazelcast.nio.serialization.GenericRecordBuilder;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.io.IOException;

public class DefaultCompactReader extends AbstractDefaultCompactReader {

    private final CompactStreamSerializer serializer;

    public DefaultCompactReader(CompactStreamSerializer serializer, BufferObjectDataInput in, Schema schema, @Nullable Class associatedClass) {
        super(in, schema, associatedClass);
        this.serializer = serializer;
    }

    @Override
    public Object readObjectInternal(BufferObjectDataInput in) throws IOException {
        return serializer.read(in);
    }

    @Override
    public GenericRecord readGenericRecordInternal(BufferObjectDataInput in) throws IOException {
        return serializer.readGenericRecord(in);
    }

    @Nonnull
    @Override
    public GenericRecordBuilder newBuilder() {
        return serializer.createGenericRecordBuilder(getSchema());
    }

    @Nonnull
    @Override
    public GenericRecordBuilder cloneWithBuilder() {
        return serializer.createGenericRecordCloner(getSchema(), this);
    }
}
