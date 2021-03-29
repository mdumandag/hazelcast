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

package com.hazelcast.internal.server.tcp;

import com.hazelcast.client.impl.ClientEndpoint;
import com.hazelcast.client.impl.ClientEndpointManager;
import com.hazelcast.client.impl.ClientEngine;
import com.hazelcast.client.impl.protocol.util.ClientMessageDecoder;
import com.hazelcast.client.impl.protocol.util.ClientMessageEncoder;
import com.hazelcast.config.EndpointConfig;
import com.hazelcast.internal.networking.Channel;
import com.hazelcast.internal.nio.Connection;
import com.hazelcast.internal.server.ServerConnection;
import com.hazelcast.internal.server.ServerContext;

import java.util.function.Function;

import static com.hazelcast.instance.ProtocolType.CLIENT;

public class ClientChannelInitializer
        extends AbstractChannelInitializer {

    ClientChannelInitializer(ServerContext serverContext, EndpointConfig config) {
        super(serverContext, config);
    }

    @Override
    public void initChannel(Channel channel) {
        ServerConnection connection = (TcpServerConnection) channel.attributeMap().get(ServerConnection.class);
        ClientEngine clientEngine = serverContext.getClientEngine();
        SingleProtocolDecoder protocolDecoder = new SingleProtocolDecoder(CLIENT,
                new ClientMessageDecoder(connection, createIsTrustedFn(clientEngine), clientEngine, serverContext.properties()));

        channel.outboundPipeline().addLast(new ClientMessageEncoder());
        channel.inboundPipeline().addLast(protocolDecoder);
    }

    public static Function<Connection, Boolean> createIsTrustedFn(ClientEngine clientEngine) {
        return new Function<Connection, Boolean>() {
            ClientEndpointManager clientEndpointManager = clientEngine.getEndpointManager();
            boolean clientIsTrusted;

            @Override
            public Boolean apply(Connection connection) {
                if (clientEndpointManager == null || clientIsTrusted) {
                    return true;
                }
                ClientEndpoint endpoint = clientEndpointManager.getEndpoint(connection);
                clientIsTrusted = endpoint != null && endpoint.isAuthenticated();
                return clientIsTrusted;
            }
        };
    }
}
