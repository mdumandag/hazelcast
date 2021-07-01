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

import com.hazelcast.config.CompactSerializationConfig;
import com.hazelcast.core.ManagedContext;
import com.hazelcast.internal.nio.BufferObjectDataInput;
import com.hazelcast.internal.nio.BufferObjectDataOutput;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.FieldType;
import com.hazelcast.nio.serialization.GenericRecord;
import com.hazelcast.nio.serialization.GenericRecordBuilder;
import com.hazelcast.nio.serialization.HazelcastSerializationException;
import com.hazelcast.nio.serialization.StreamSerializer;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.hazelcast.internal.nio.Bits.INT_SIZE_IN_BYTES;
import static com.hazelcast.internal.serialization.impl.FieldOperations.fieldOperations;
import static com.hazelcast.internal.serialization.impl.SerializationConstants.TYPE_COMPACT_WITH_SCHEMA;

public class CompactWithSchemaStreamSerializer extends CompactStreamSerializerBase implements StreamSerializer<Object> {

    public CompactWithSchemaStreamSerializer(CompactSerializationConfig compactSerializationConfig,
                                   ManagedContext managedContext, SchemaService schemaService,
                                   ClassLoader classLoader,
                                   Function<byte[], BufferObjectDataInput> bufferObjectDataInputFunc,
                                   Supplier<BufferObjectDataOutput> bufferObjectDataOutputSupplier) {
        super(compactSerializationConfig, managedContext, schemaService, classLoader, bufferObjectDataInputFunc, bufferObjectDataOutputSupplier);
    }

    @Override
    public int getTypeId() {
        return TYPE_COMPACT_WITH_SCHEMA;
    }

    public GenericRecordBuilder createGenericRecordBuilder(Schema schema) {
        return new SerializingGenericRecordBuilder(schema,
                new DefaultCompactWithSchemaWriter(this, bufferObjectDataOutputSupplier.get(), schema, new HashSet<>()),
                (bytes -> new DefaultCompactWithSchemaReader(this, bufferObjectDataInputFunc.apply(bytes), schema, null)),
                bufferObjectDataInputFunc);
    }

    public GenericRecordBuilder createGenericRecordCloner(Schema schema, AbstractCompactInternalGenericRecord record) {
        return new SerializingGenericRecordCloner(schema, record,
                new DefaultCompactWithSchemaWriter(this, bufferObjectDataOutputSupplier.get(), schema, new HashSet<>()),
                (bytes, associatedClass) -> new DefaultCompactWithSchemaReader(this, bufferObjectDataInputFunc.apply(bytes), schema, associatedClass),
                bufferObjectDataInputFunc);
    }

    //========================== WRITE =============================//
    @Override
    public void write(ObjectDataOutput out, Object o) throws IOException {
        assert out instanceof BufferObjectDataOutput;
        BufferObjectDataOutput bufferObjectDataOutput = (BufferObjectDataOutput) out;
        Set<Schema> schemas = new HashSet<>(1);
        int position = bufferObjectDataOutput.position();
        bufferObjectDataOutput.writeZeroBytes(INT_SIZE_IN_BYTES);
        if (o instanceof CompactGenericRecord) {
            writeGenericRecord(bufferObjectDataOutput, (CompactGenericRecord) o, schemas);
        } else {
            writeObject(bufferObjectDataOutput, o, schemas);
        }
        writeSchemas(bufferObjectDataOutput, position, schemas);
    }

    void writeGenericRecord(BufferObjectDataOutput out, CompactGenericRecord record, Set<Schema> schemas) throws IOException {
        Schema schema = record.getSchema();
        schemaService.put(schema);
        out.writeLong(schema.getSchemaId());
        schemas.add(schema);
        DefaultCompactWithSchemaWriter writer = new DefaultCompactWithSchemaWriter(this, out, schema, schemas);
        Collection<FieldDescriptor> fields = schema.getFields();
        for (FieldDescriptor fieldDescriptor : fields) {
            String fieldName = fieldDescriptor.getFieldName();
            FieldType fieldType = fieldDescriptor.getType();
            fieldOperations(fieldType).readFromGenericRecordToWriter(writer, record, fieldName);
        }
        writer.end();
    }

    public void writeObject(BufferObjectDataOutput out, Object o, Set<Schema> schemas) throws IOException {
        ConfigurationRegistry registry = getOrCreateRegistry(o);
        Class<?> aClass = o.getClass();

        Schema schema = classToSchemaMap.get(aClass);
        if (schema == null) {
            SchemaWriter writer = new SchemaWriter(registry.getTypeName());
            registry.getSerializer().write(writer, o);
            schema = writer.build();
            //if we will include the schema on binary, the schema will be delivered anyway.
            //No need to put it to cluster. Putting it local only in order not to ask from remote on read.
            schemaService.putLocal(schema);
            classToSchemaMap.put(aClass, schema);
        }
        out.writeLong(schema.getSchemaId());
        schemas.add(schema);
        DefaultCompactWithSchemaWriter writer = new DefaultCompactWithSchemaWriter(this, out, schema, schemas);
        registry.getSerializer().write(writer, o);
        writer.end();
    }

    //========================== READ =============================//

    @Override
    public Object read(@Nonnull ObjectDataInput in) throws IOException {
        BufferObjectDataInput input = (BufferObjectDataInput) in;
        int schemaStartPosition = input.readInt();
        LazySchemaReader schemaReader = new LazySchemaReader(input, schemaStartPosition);
        return read(input, schemaReader);
    }

    public Object read(BufferObjectDataInput in, LazySchemaReader schemaReader) throws IOException {
        Schema schema = getOrReadSchema(in, schemaReader);
        ConfigurationRegistry registry = getOrCreateRegistry(schema.getTypeName());

        if (registry == null) {
            //we have tried to load class via class loader, it did not work. We are returning a GenericRecord.
            return new DefaultCompactWithSchemaReader(this, in, schema, null, schemaReader);
        }

        DefaultCompactWithSchemaReader genericRecord = new DefaultCompactWithSchemaReader(this, in, schema,
                registry.getClazz(), schemaReader);
        Object object = registry.getSerializer().read(genericRecord);
        return managedContext != null ? managedContext.initialize(object) : object;
    }

    public GenericRecord readGenericRecord(ObjectDataInput in) throws IOException {
        BufferObjectDataInput input = (BufferObjectDataInput) in;
        int schemaStartPosition = input.readInt();
        LazySchemaReader schemaReader = new LazySchemaReader(input, schemaStartPosition);
        return readGenericRecord(input, schemaReader);
    }

    public GenericRecord readGenericRecord(BufferObjectDataInput in, LazySchemaReader schemaReader) throws IOException {
        Schema schema = getOrReadSchema(in, schemaReader);
        return new DefaultCompactWithSchemaReader(this, in, schema, null, schemaReader);
    }

    private void writeSchemas(BufferObjectDataOutput out, int position, Set<Schema> schemas) throws IOException {
        int pos = out.position();
        out.writeInt(position, pos);
        out.writeInt(schemas.size());
        for (Schema schema : schemas) {
            out.writeLong(schema.getSchemaId());
            int sizeOfSchemaPosition = out.position();
            out.writeInt(0);
            int schemaBeginPos = out.position();
            schema.writeData(out);
            int schemaEndPosition = out.position();
            out.writeInt(sizeOfSchemaPosition, schemaEndPosition - schemaBeginPos);
        }
    }

    private Schema getOrReadSchema(ObjectDataInput input, LazySchemaReader schemaReader) throws IOException {
        long schemaId = input.readLong();
        Schema schema = schemaService.get(schemaId);
        if (schema != null) {
            return schema;
        }

        schema = schemaReader.getSchema(schemaId);
        if (schema == null) {
            throw new HazelcastSerializationException("Cannot find schema with the id of '" + schemaId + "' in data.");
        }

        // TODO: Should we put local here?
        schemaService.put(schema);
        return schema;
    }
}
