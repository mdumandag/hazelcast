/*
 * Copyright (c) 2008-2020, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.client.impl;

import com.hazelcast.client.impl.client.CSharpCallableSerializable;
import com.hazelcast.client.impl.client.CSharpEntryProcessorSerializable;
import com.hazelcast.client.impl.operations.GetConnectedClientsOperation;
import com.hazelcast.client.impl.operations.OperationFactoryWrapper;
import com.hazelcast.internal.serialization.DataSerializerHook;
import com.hazelcast.internal.serialization.impl.FactoryIdHelper;
import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import static com.hazelcast.internal.serialization.impl.FactoryIdHelper.CLIENT_DS_FACTORY;
import static com.hazelcast.internal.serialization.impl.FactoryIdHelper.CLIENT_DS_FACTORY_ID;

public class ClientDataSerializerHook implements DataSerializerHook {

    public static final int F_ID = FactoryIdHelper.getFactoryId(CLIENT_DS_FACTORY, CLIENT_DS_FACTORY_ID);

    public static final int GET_CONNECTED_CLIENTS = 2;
    public static final int OP_FACTORY_WRAPPER = 4;
    public static final int CSHARP_CALLABLE_SERIALIZABLE = 5;
    public static final int CSHARP_ENTRY_PROCESSOR_SERIALIZABLE = 6;

    @Override
    public int getFactoryId() {
        return F_ID;
    }

    @Override
    public DataSerializableFactory createFactory() {
        return new DataSerializableFactory() {
            @Override
            public IdentifiedDataSerializable create(int typeId) {
                switch (typeId) {
                    case GET_CONNECTED_CLIENTS:
                        return new GetConnectedClientsOperation();
                    case OP_FACTORY_WRAPPER:
                        return new OperationFactoryWrapper();
                    case CSHARP_CALLABLE_SERIALIZABLE:
                        return new CSharpCallableSerializable();
                    case CSHARP_ENTRY_PROCESSOR_SERIALIZABLE:
                        return new CSharpEntryProcessorSerializable();
                    default:
                        return null;
                }
            }
        };
    }

}
