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

import com.hazelcast.cache.impl.CacheEventData;
import com.hazelcast.cache.impl.CacheEventSet;
import com.hazelcast.collection.impl.collection.CollectionEvent;
import com.hazelcast.collection.impl.list.ListService;
import com.hazelcast.collection.impl.queue.QueueEvent;
import com.hazelcast.collection.impl.set.SetService;
import com.hazelcast.core.EntryEventType;
import com.hazelcast.internal.util.ItemCounter;
import com.hazelcast.internal.util.executor.StripedExecutor;
import com.hazelcast.logging.ILogger;
import com.hazelcast.map.impl.event.EntryEventData;
import com.hazelcast.spi.impl.NodeEngineImpl;
import com.hazelcast.spi.impl.eventservice.impl.LocalEventDispatcher;
import com.hazelcast.spi.properties.HazelcastProperties;
import com.hazelcast.spi.properties.HazelcastProperty;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import static java.lang.Math.min;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * The EventQueuePlugin checks the event queue and samples the event types if
 * the size is above a certain threshold.
 * <p>
 * This is very useful to figure out why the event queue is running full.
 */
public class ServerEventQueuePlugin extends EventQueuePlugin {

    public ServerEventQueuePlugin(NodeEngineImpl nodeEngine, StripedExecutor eventExecutor) {
        super(nodeEngine.getLogger(EventQueuePlugin.class), eventExecutor, nodeEngine.getProperties());
    }

    int sampleRunnable(Runnable runnable) {
        if (runnable instanceof LocalEventDispatcher) {
            LocalEventDispatcher eventDispatcher = (LocalEventDispatcher) runnable;
            return sampleLocalDispatcherEvent(eventDispatcher);
        }
        return super.sampleRunnable(runnable);
    }

    private int sampleLocalDispatcherEvent(LocalEventDispatcher eventDispatcher) {
        Object dispatcherEvent = eventDispatcher.getEvent();
        if (dispatcherEvent instanceof EntryEventData) {
            // IMap event
            EntryEventData event = (EntryEventData) dispatcherEvent;
            EntryEventType type = EntryEventType.getByType(event.getEventType());
            String mapName = event.getMapName();
            occurrenceMap.add(format("IMap '%s' %s", mapName, type), 1);
            return 1;
        } else if (dispatcherEvent instanceof CacheEventSet) {
            // ICache event
            CacheEventSet eventSet = (CacheEventSet) dispatcherEvent;
            Set<CacheEventData> events = eventSet.getEvents();
            for (CacheEventData event : events) {
                occurrenceMap.add(format("ICache '%s' %s", event.getName(), event.getCacheEventType()), 1);
            }
            return events.size();
        } else if (dispatcherEvent instanceof QueueEvent) {
            // IQueue event
            QueueEvent event = (QueueEvent) dispatcherEvent;
            occurrenceMap.add(format("IQueue '%s' %s", event.getName(), event.getEventType()), 1);
            return 1;
        } else if (dispatcherEvent instanceof CollectionEvent) {
            // ISet or IList event
            CollectionEvent event = (CollectionEvent) dispatcherEvent;
            String serviceName = eventDispatcher.getServiceName();
            if (SetService.SERVICE_NAME.equals(serviceName)) {
                serviceName = "ISet";
            } else if (ListService.SERVICE_NAME.equals(serviceName)) {
                serviceName = "IList";
            }
            occurrenceMap.add(format("%s '%s' %s", serviceName, event.getName(), event.getEventType()), 1);
            return 1;
        }
        occurrenceMap.add(dispatcherEvent.getClass().getSimpleName(), 1);
        return 1;
    }
}
