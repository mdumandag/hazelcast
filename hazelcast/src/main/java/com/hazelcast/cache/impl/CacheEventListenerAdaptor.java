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

package com.hazelcast.cache.impl;

import com.hazelcast.cache.CacheEventType;
import com.hazelcast.cache.ICache;
import com.hazelcast.core.ManagedContext;
import com.hazelcast.internal.serialization.Data;
import com.hazelcast.internal.serialization.SerializationService;
import com.hazelcast.internal.services.ListenerWrapperEventFilter;
import com.hazelcast.internal.services.NotifiableEventListener;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import com.hazelcast.spi.impl.eventservice.EventRegistration;

import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;
import javax.cache.event.CacheEntryExpiredListener;
import javax.cache.event.CacheEntryListener;
import javax.cache.event.CacheEntryRemovedListener;
import javax.cache.event.CacheEntryUpdatedListener;
import javax.cache.event.EventType;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

/**
 * This implementation of {@link CacheEventListener} uses the adapter pattern for wrapping all cache event listener
 * types into a single listener.
 * <p>JCache has multiple {@link CacheEntryListener} sub-interfaces for each event type. This adapter
 * implementation delegates to the correct subtype using the event type.</p>
 * <p>
 * <p>Another responsibility of this implementation is filtering events by using the already configured
 * event filters.</p>
 *
 * @param <K> the type of key.
 * @param <V> the type of value.
 * @see CacheEntryCreatedListener
 * @see CacheEntryUpdatedListener
 * @see CacheEntryRemovedListener
 * @see CacheEntryExpiredListener
 * @see CacheEntryEventFilter
 */
public class CacheEventListenerAdaptor<K, V> extends CacheEventListenerAdaptorBase<K, V>
        implements CacheEventListener,
                   NotifiableEventListener<CacheService>,
                   ListenerWrapperEventFilter,
                   IdentifiedDataSerializable {

    public CacheEventListenerAdaptor() {
        super();
    }

    public CacheEventListenerAdaptor(
            CacheProxy<K, V> proxy,
            CacheEntryListenerConfiguration<K, V> entryListenerConfiguration,
            SerializationService serializationService
    ) {
        super(proxy, entryListenerConfiguration, serializationService);
    }

    @Override
    public void handleEvent(Object eventObject) {
        if (eventObject instanceof CacheEventSet) {
            CacheEventSet cacheEventSet = (CacheEventSet) eventObject;
            try {
                if (cacheEventSet.getEventType() != CacheEventType.COMPLETED) {
                    handleEvent(cacheEventSet.getEventType().getType(), cacheEventSet.getEvents());
                }
            } finally {
                ((CacheSyncListenerCompleter) source).countDownCompletionLatch(cacheEventSet.getCompletionId());
            }
        }
    }

    @Override
    public void onRegister(CacheService cacheService, String serviceName,
                           String topic, EventRegistration registration) {
        CacheContext cacheContext = cacheService.getOrCreateCacheContext(topic);
        cacheContext.increaseCacheEntryListenerCount();
    }

    @Override
    public void onDeregister(CacheService cacheService, String serviceName,
                             String topic, EventRegistration registration) {
        CacheContext cacheContext = cacheService.getOrCreateCacheContext(topic);
        cacheContext.decreaseCacheEntryListenerCount();
    }

    @Override
    public boolean eval(Object event) {
        return true;
    }

    @Override
    public Object getListener() {
        return this;
    }

    @Override
    public int getFactoryId() {
        return CacheDataSerializerHook.F_ID;
    }

    @Override
    public int getClassId() {
        return CacheDataSerializerHook.CACHE_EVENT_LISTENER_ADAPTOR;
    }

    @Override
    public void writeData(ObjectDataOutput out)
            throws IOException {

    }

    @Override
    public void readData(ObjectDataInput in)
            throws IOException {

    }
}
