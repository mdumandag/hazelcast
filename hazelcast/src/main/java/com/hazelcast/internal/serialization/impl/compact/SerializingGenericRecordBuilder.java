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
import com.hazelcast.nio.serialization.HazelcastSerializationException;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class SerializingGenericRecordBuilder implements GenericRecordBuilder {

    private final AbstractDefaultCompactWriter compactWriter;
    private final Schema schema;
    private final Function<byte[], BufferObjectDataInput> bufferObjectDataInputFn;
    private final Function<byte[], AbstractDefaultCompactReader> compactReaderFn;
    private final Set<String> writtenFields = new HashSet<>();

    public SerializingGenericRecordBuilder(Schema schema,
                                           AbstractDefaultCompactWriter compactWriter,
                                           Function<byte[], AbstractDefaultCompactReader> compactReaderFn,
                                           Function<byte[], BufferObjectDataInput> bufferObjectDataInputFn
                                           ) {
        this.schema = schema;
        this.compactWriter = compactWriter;
        this.compactReaderFn = compactReaderFn;
        this.bufferObjectDataInputFn = bufferObjectDataInputFn;
    }

    @Override
    @Nonnull
    public GenericRecord build() {
        Set<String> fieldNames = schema.getFieldNames();
        for (String fieldName : fieldNames) {
            if (!writtenFields.contains(fieldName)) {
                throw new HazelcastSerializationException("Found an unset field " + fieldName
                        + ". All the fields must be set before build");
            }
        }
        compactWriter.end();
        byte[] bytes = compactWriter.toByteArray();
        return compactReaderFn.apply(bytes);
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setInt(@Nonnull String fieldName, int value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeInt(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setLong(@Nonnull String fieldName, long value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeLong(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setString(@Nonnull String fieldName, String value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeString(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setBoolean(@Nonnull String fieldName, boolean value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeBoolean(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setByte(@Nonnull String fieldName, byte value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeByte(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setChar(@Nonnull String fieldName, char value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeChar(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setDouble(@Nonnull String fieldName, double value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeDouble(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setFloat(@Nonnull String fieldName, float value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeFloat(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setShort(@Nonnull String fieldName, short value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeShort(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setDecimal(@Nonnull String fieldName, BigDecimal value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeDecimal(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setTime(@Nonnull String fieldName, LocalTime value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeTime(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setDate(@Nonnull String fieldName, LocalDate value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeDate(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setTimestamp(@Nonnull String fieldName, LocalDateTime value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeTimestamp(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setTimestampWithTimezone(@Nonnull String fieldName, OffsetDateTime value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeTimestampWithTimezone(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setGenericRecord(@Nonnull String fieldName, GenericRecord value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeGenericRecord(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setGenericRecordArray(@Nonnull String fieldName, GenericRecord[] value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeGenericRecordArray(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setByteArray(@Nonnull String fieldName, byte[] value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeByteArray(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setBooleanArray(@Nonnull String fieldName, boolean[] value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeBooleanArray(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setCharArray(@Nonnull String fieldName, char[] value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeCharArray(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setIntArray(@Nonnull String fieldName, int[] value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeIntArray(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setLongArray(@Nonnull String fieldName, long[] value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeLongArray(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setDoubleArray(@Nonnull String fieldName, double[] value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeDoubleArray(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setFloatArray(@Nonnull String fieldName, float[] value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeFloatArray(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setShortArray(@Nonnull String fieldName, short[] value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeShortArray(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setStringArray(@Nonnull String fieldName, String[] value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeStringArray(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setDecimalArray(@Nonnull String fieldName, BigDecimal[] value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeDecimalArray(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setTimeArray(@Nonnull String fieldName, LocalTime[] value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeTimeArray(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setDateArray(@Nonnull String fieldName, LocalDate[] value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeDateArray(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setTimestampArray(@Nonnull String fieldName, LocalDateTime[] value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeTimestampArray(fieldName, value);
        return this;
    }

    @Override
    @Nonnull
    public GenericRecordBuilder setTimestampWithTimezoneArray(@Nonnull String fieldName, OffsetDateTime[] value) {
        checkIfAlreadyWritten(fieldName);
        compactWriter.writeTimestampWithTimezoneArray(fieldName, value);
        return this;
    }

    private void checkIfAlreadyWritten(@Nonnull String fieldName) {
        if (!writtenFields.add(fieldName)) {
            throw new HazelcastSerializationException("Field can only be written once");
        }
    }

}
