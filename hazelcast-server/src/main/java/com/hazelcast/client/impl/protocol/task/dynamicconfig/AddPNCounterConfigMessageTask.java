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

package com.hazelcast.client.impl.protocol.task.dynamicconfig;

import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.client.impl.protocol.codec.ServerDynamicConfigAddPNCounterConfigCodec;
import com.hazelcast.config.PNCounterConfig;
import com.hazelcast.instance.impl.Node;
import com.hazelcast.internal.dynamicconfig.DynamicConfigurationAwareConfig;
import com.hazelcast.internal.nio.Connection;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public class AddPNCounterConfigMessageTask
        extends AbstractAddConfigMessageTask<ServerDynamicConfigAddPNCounterConfigCodec.RequestParameters> {

    public AddPNCounterConfigMessageTask(ClientMessage clientMessage, Node node, Connection connection) {
        super(clientMessage, node, connection);
    }

    @Override
    protected ServerDynamicConfigAddPNCounterConfigCodec.RequestParameters decodeClientMessage(ClientMessage clientMessage) {
        return ServerDynamicConfigAddPNCounterConfigCodec.decodeRequest(clientMessage);
    }

    @Override
    protected ClientMessage encodeResponse(Object response) {
        return ServerDynamicConfigAddPNCounterConfigCodec.encodeResponse();
    }

    @Override
    protected IdentifiedDataSerializable getConfig() {
        PNCounterConfig config = new PNCounterConfig(parameters.name);
        config.setReplicaCount(parameters.replicaCount);
        config.setStatisticsEnabled(parameters.statisticsEnabled);
        config.setSplitBrainProtectionName(parameters.splitBrainProtectionName);
        return config;
    }

    @Override
    public String getMethodName() {
        return "addPNCounterConfig";
    }

    @Override
    protected boolean checkStaticConfigDoesNotExist(IdentifiedDataSerializable config) {
        DynamicConfigurationAwareConfig nodeConfig = (DynamicConfigurationAwareConfig) nodeEngine.getConfig();
        PNCounterConfig pnCounterConfig = (PNCounterConfig) config;
        return nodeConfig.checkStaticConfigDoesNotExist(nodeConfig.getStaticConfig().getPNCounterConfigs(),
                pnCounterConfig.getName(), pnCounterConfig);
    }
}
