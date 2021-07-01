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
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.hazelcast.internal.serialization.impl.FieldOperations.fieldOperations;
import static com.hazelcast.internal.serialization.impl.SerializationConstants.TYPE_COMPACT;

public class CompactStreamSerializer extends CompactStreamSerializerBase implements StreamSerializer<Object> {

    public CompactStreamSerializer(CompactSerializationConfig compactSerializationConfig,
                                   ManagedContext managedContext, SchemaService schemaService,
                                   ClassLoader classLoader,
                                   Function<byte[], BufferObjectDataInput> bufferObjectDataInputFunc,
                                   Supplier<BufferObjectDataOutput> bufferObjectDataOutputSupplier) {
        super(compactSerializationConfig, managedContext, schemaService, classLoader, bufferObjectDataInputFunc, bufferObjectDataOutputSupplier);
    }

    public GenericRecord readGenericRecord(ObjectDataInput in) throws IOException {
        Schema schema = getSchema(in);
        BufferObjectDataInput input = (BufferObjectDataInput) in;
        return new DefaultCompactReader(this, input, schema, null);
    }

    @Override
    public int getTypeId() {
        return TYPE_COMPACT;
    }

    public GenericRecordBuilder createGenericRecordBuilder(Schema schema) {
        // TODO: this seems hacky, update it
        return new SerializingGenericRecordBuilder(schema,
                new DefaultCompactWriter(this, bufferObjectDataOutputSupplier.get(), schema),
                (bytes -> new DefaultCompactReader(this, bufferObjectDataInputFunc.apply(bytes), schema, null)),
                bufferObjectDataInputFunc);
    }

    public GenericRecordBuilder createGenericRecordCloner(Schema schema, AbstractDefaultCompactReader record) {
        // TODO: this seems hacky, update it
        return new SerializingGenericRecordCloner(schema, record,
                new DefaultCompactWriter(this, bufferObjectDataOutputSupplier.get(), schema),
                (bytes, associatedClass) -> new DefaultCompactReader(this, bufferObjectDataInputFunc.apply(bytes), schema, associatedClass),
                bufferObjectDataInputFunc);
    }

    //========================== WRITE =============================//
    @Override
    public void write(ObjectDataOutput out, Object o) throws IOException {
        assert out instanceof BufferObjectDataOutput;
        BufferObjectDataOutput bufferObjectDataOutput = (BufferObjectDataOutput) out;
        if (o instanceof CompactGenericRecord) {
            writeGenericRecord(bufferObjectDataOutput, (CompactGenericRecord) o);
        } else {
            writeObject(bufferObjectDataOutput, o);
        }
    }

    void writeGenericRecord(BufferObjectDataOutput out, CompactGenericRecord record) throws IOException {
        Schema schema = record.getSchema();
        schemaService.put(schema);
        out.writeLong(schema.getSchemaId());
        DefaultCompactWriter writer = new DefaultCompactWriter(this, out, schema);
        Collection<FieldDescriptor> fields = schema.getFields();
        for (FieldDescriptor fieldDescriptor : fields) {
            String fieldName = fieldDescriptor.getFieldName();
            FieldType fieldType = fieldDescriptor.getType();
            fieldOperations(fieldType).readFromGenericRecordToWriter(writer, record, fieldName);
        }
        writer.end();
    }

    public void writeObject(BufferObjectDataOutput out, Object o) throws IOException {
        ConfigurationRegistry registry = getOrCreateRegistry(o);
        Class<?> aClass = o.getClass();

        Schema schema = classToSchemaMap.get(aClass);
        if (schema == null) {
            SchemaWriter writer = new SchemaWriter(registry.getTypeName());
            registry.getSerializer().write(writer, o);
            schema = writer.build();
            schemaService.put(schema);
            classToSchemaMap.put(aClass, schema);
        }
        out.writeLong(schema.getSchemaId());
        DefaultCompactWriter writer = new DefaultCompactWriter(this, out, schema);
        registry.getSerializer().write(writer, o);
        writer.end();
    }

    //========================== READ =============================//

    @Override
    public Object read(@Nonnull ObjectDataInput in) throws IOException {
        BufferObjectDataInput input = (BufferObjectDataInput) in;
        Schema schema = getSchema(in);
        ConfigurationRegistry registry = getOrCreateRegistry(schema.getTypeName());

        if (registry == null) {
            //we have tried to load class via class loader, it did not work. We are returning a GenericRecord.
            return new DefaultCompactReader(this, input, schema, null);
        }

        DefaultCompactReader genericRecord = new DefaultCompactReader(this, input, schema, registry.getClazz());
        Object object = registry.getSerializer().read(genericRecord);
        return managedContext != null ? managedContext.initialize(object) : object;
    }

    private Schema getSchema(ObjectDataInput input) throws IOException {
        long schemaId = input.readLong();
        Schema schema = schemaService.get(schemaId);
        if (schema != null) {
            return schema;
        }
        throw new HazelcastSerializationException("The schema can not be found with id " + schemaId);
    }
}
