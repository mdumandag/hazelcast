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
import com.hazelcast.internal.nio.ClassLoaderUtil;
import com.hazelcast.internal.util.TriTuple;
import com.hazelcast.nio.serialization.HazelcastSerializationException;
import com.hazelcast.nio.serialization.compact.CompactSerializer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;


public class CompactStreamSerializerBase {
    protected final Map<Class, ConfigurationRegistry> classToRegistryMap = new ConcurrentHashMap<>();
    protected final Map<String, ConfigurationRegistry> classNameToRegistryMap = new ConcurrentHashMap<>();
    protected final Map<Class, Schema> classToSchemaMap = new ConcurrentHashMap<>();
    protected final ReflectiveCompactSerializer reflectiveSerializer = new ReflectiveCompactSerializer();
    protected final SchemaService schemaService;
    protected final ManagedContext managedContext;
    protected final ClassLoader classLoader;
    protected final Function<byte[], BufferObjectDataInput> bufferObjectDataInputFunc;
    protected final Supplier<BufferObjectDataOutput> bufferObjectDataOutputSupplier;

    public CompactStreamSerializerBase(CompactSerializationConfig compactSerializationConfig,
                                       ManagedContext managedContext, SchemaService schemaService,
                                       ClassLoader classLoader,
                                       Function<byte[], BufferObjectDataInput> bufferObjectDataInputFunc,
                                       Supplier<BufferObjectDataOutput> bufferObjectDataOutputSupplier) {
        this.managedContext = managedContext;
        this.schemaService = schemaService;
        this.bufferObjectDataInputFunc = bufferObjectDataInputFunc;
        this.bufferObjectDataOutputSupplier = bufferObjectDataOutputSupplier;
        this.classLoader = classLoader;
        Map<String, TriTuple<Class, String, CompactSerializer>> registries = compactSerializationConfig.getRegistries();
        for (Map.Entry<String, TriTuple<Class, String, CompactSerializer>> entry : registries.entrySet()) {
            String typeName = entry.getKey();
            CompactSerializer serializer = entry.getValue().element3;
            InternalCompactSerializer compactSerializer = serializer == null ? reflectiveSerializer : serializer;
            Class clazz = entry.getValue().element1;
            classToRegistryMap.put(clazz, new ConfigurationRegistry(clazz, typeName, compactSerializer));
            classNameToRegistryMap.put(typeName, new ConfigurationRegistry(clazz, typeName, compactSerializer));
        }
    }

    protected ConfigurationRegistry getOrCreateRegistry(Object object) {
        return classToRegistryMap.computeIfAbsent(object.getClass(), aClass -> {
            if (object instanceof Compactable) {
                CompactSerializer<?> serializer = ((Compactable<?>) object).getCompactSerializer();
                return new ConfigurationRegistry(aClass, aClass.getName(), serializer);
            }
            return new ConfigurationRegistry(aClass, aClass.getName(), reflectiveSerializer);
        });
    }

    protected ConfigurationRegistry getOrCreateRegistry(String className) {
        return classNameToRegistryMap.computeIfAbsent(className, s -> {
            Class<?> clazz;
            try {
                clazz = ClassLoaderUtil.loadClass(classLoader, className);
            } catch (Exception e) {
                return null;
            }
            try {
                Object object = ClassLoaderUtil.newInstance(clazz.getClassLoader(), clazz);
                return getOrCreateRegistry(object);
            } catch (Exception e) {
                throw new HazelcastSerializationException("Class " + clazz + " must have an empty constructor", e);
            }
        });
    }
}
