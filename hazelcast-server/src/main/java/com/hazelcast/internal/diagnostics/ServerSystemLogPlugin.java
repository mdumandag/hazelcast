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

package com.hazelcast.internal.diagnostics;

import com.hazelcast.cluster.Address;
import com.hazelcast.cluster.Member;
import com.hazelcast.cluster.MembershipAdapter;
import com.hazelcast.cluster.MembershipEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.LifecycleEvent;
import com.hazelcast.core.LifecycleListener;
import com.hazelcast.instance.impl.NodeExtension;
import com.hazelcast.internal.cluster.ClusterVersionListener;
import com.hazelcast.internal.nio.Connection;
import com.hazelcast.internal.nio.ConnectionListenable;
import com.hazelcast.internal.nio.ConnectionListener;
import com.hazelcast.internal.server.ServerConnection;
import com.hazelcast.logging.ILogger;
import com.hazelcast.partition.MigrationListener;
import com.hazelcast.partition.MigrationState;
import com.hazelcast.partition.ReplicaMigrationEvent;
import com.hazelcast.spi.impl.NodeEngineImpl;
import com.hazelcast.spi.properties.HazelcastProperties;
import com.hazelcast.spi.properties.HazelcastProperty;
import com.hazelcast.version.Version;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * The {@link ServerSystemLogPlugin} is responsible for:
 * <ol>
 * <li>Showing lifecycle changes like shutting down, merging etc.</li>
 * <li>Show connection creation and connection closing. Also the causes of closes are included.</li>
 * <li>Showing membership changes.</li>
 * <li>Optionally showing partition migration</li>
 * </ol>
 * This plugin is very useful to get an idea what is happening inside a cluster;
 * especially when there are connection related problems.
 * <p>
 * This plugin has a low overhead and is meant to run in production.
 */
public class ServerSystemLogPlugin extends SystemLogPlugin {

    private final NodeExtension nodeExtension;

    public ServerSystemLogPlugin(NodeEngineImpl nodeEngine) {
        super(nodeEngine.getProperties(),
                nodeEngine.getNode().getServer(),
                nodeEngine.getHazelcastInstance(),
                nodeEngine.getLogger(ServerSystemLogPlugin.class));
        nodeExtension = nodeEngine.getNode().getNodeExtension();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (nodeExtension != null) {
            nodeExtension.registerListener(new ClusterVersionListenerImpl());
        }
    }

    @Override
    void writeConnectionType(DiagnosticsLogWriter writer, Connection connection) {
        super.writeConnectionType(writer, connection);
        if (connection instanceof ServerConnection) {
            writer.writeKeyValueEntry("type", ((ServerConnection) connection).getConnectionType());
        }
    }

    protected class ClusterVersionListenerImpl implements ClusterVersionListener {
        @Override
        public void onClusterVersionChange(Version newVersion) {
            logQueue.add(newVersion);
        }
    }
}
