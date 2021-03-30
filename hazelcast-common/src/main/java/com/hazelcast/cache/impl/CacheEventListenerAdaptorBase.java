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
import java.util.Collection;
import java.util.HashSet;

public class CacheEventListenerAdaptorBase<K, V> implements CacheEntryListenerProvider<K, V> {

    // all fields are effectively final
    protected transient CacheEntryListener<K, V> cacheEntryListener;
    protected transient CacheEntryCreatedListener cacheEntryCreatedListener;
    protected transient CacheEntryRemovedListener cacheEntryRemovedListener;
    protected transient CacheEntryUpdatedListener cacheEntryUpdatedListener;
    protected transient CacheEntryExpiredListener cacheEntryExpiredListener;
    protected transient CacheEntryEventFilter<? super K, ? super V> filter;
    protected boolean isOldValueRequired;

    protected transient SerializationService serializationService;
    protected transient ICache<K, V> source;

    public CacheEventListenerAdaptorBase() {
    }

    public CacheEventListenerAdaptorBase(ICache<K, V> source,
                                         CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration,
                                         SerializationService serializationService) {
        this.source = source;
        this.serializationService = serializationService;

        this.cacheEntryListener = createCacheEntryListener(cacheEntryListenerConfiguration);
        if (cacheEntryListener instanceof CacheEntryCreatedListener) {
            this.cacheEntryCreatedListener = (CacheEntryCreatedListener) cacheEntryListener;
        } else {
            this.cacheEntryCreatedListener = null;
        }
        if (cacheEntryListener instanceof CacheEntryRemovedListener) {
            this.cacheEntryRemovedListener = (CacheEntryRemovedListener) cacheEntryListener;
        } else {
            this.cacheEntryRemovedListener = null;
        }
        if (cacheEntryListener instanceof CacheEntryUpdatedListener) {
            this.cacheEntryUpdatedListener = (CacheEntryUpdatedListener) cacheEntryListener;
        } else {
            this.cacheEntryUpdatedListener = null;
        }
        if (cacheEntryListener instanceof CacheEntryExpiredListener) {
            this.cacheEntryExpiredListener = (CacheEntryExpiredListener) cacheEntryListener;
        } else {
            this.cacheEntryExpiredListener = null;
        }
        cacheEntryListener = injectDependencies(cacheEntryListener);

        Factory<CacheEntryEventFilter<? super K, ? super V>> filterFactory =
                cacheEntryListenerConfiguration.getCacheEntryEventFilterFactory();
        if (filterFactory != null) {
            this.filter = filterFactory.create();
        } else {
            this.filter = null;
        }
        filter = injectDependencies(filter);

        this.isOldValueRequired = cacheEntryListenerConfiguration.isOldValueRequired();
    }

    @Override
    public CacheEntryListener<K, V> getCacheEntryListener() {
        return cacheEntryListener;
    }


    private CacheEntryListener<K, V> createCacheEntryListener(
            CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
        Factory<CacheEntryListener<? super K, ? super V>> cacheEntryListenerFactory =
                cacheEntryListenerConfiguration.getCacheEntryListenerFactory();
        cacheEntryListenerFactory = injectDependencies(cacheEntryListenerFactory);

        return (CacheEntryListener<K, V>) cacheEntryListenerFactory.create();
    }

    @SuppressWarnings("unchecked")
    private <T> T injectDependencies(Object obj) {
        ManagedContext managedContext = serializationService.getManagedContext();
        return (T) managedContext.initialize(obj);
    }

    public void handle(int type, Collection<CacheEventData> keys, int completionId) {
        try {
            if (CacheEventType.getByType(type) != CacheEventType.COMPLETED) {
                handleEvent(type, keys);
            }
        } finally {
            ((CacheSyncListenerCompleter) source).countDownCompletionLatch(completionId);
        }
    }

    protected void handleEvent(int type, Collection<CacheEventData> keys) {
        final Iterable<CacheEntryEvent<? extends K, ? extends V>> cacheEntryEvent = createCacheEntryEvent(keys);
        CacheEventType eventType = CacheEventType.getByType(type);
        switch (eventType) {
            case CREATED:
                if (this.cacheEntryCreatedListener != null) {
                    this.cacheEntryCreatedListener.onCreated(cacheEntryEvent);
                }
                break;
            case UPDATED:
                if (this.cacheEntryUpdatedListener != null) {
                    this.cacheEntryUpdatedListener.onUpdated(cacheEntryEvent);
                }
                break;
            case REMOVED:
                if (this.cacheEntryRemovedListener != null) {
                    this.cacheEntryRemovedListener.onRemoved(cacheEntryEvent);
                }
                break;
            case EXPIRED:
                if (this.cacheEntryExpiredListener != null) {
                    this.cacheEntryExpiredListener.onExpired(cacheEntryEvent);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid event type: " + eventType.name());
        }
    }

    private Iterable<CacheEntryEvent<? extends K, ? extends V>> createCacheEntryEvent(Collection<CacheEventData> keys) {
        HashSet<CacheEntryEvent<? extends K, ? extends V>> evt = new HashSet<CacheEntryEvent<? extends K, ? extends V>>();
        for (CacheEventData cacheEventData : keys) {
            EventType eventType = CacheEventType.convertToEventType(cacheEventData.getCacheEventType());
            K key = toObject(cacheEventData.getDataKey());
            boolean hasNewValue = !(eventType == EventType.REMOVED || eventType == EventType.EXPIRED);
            final V newValue;
            final V oldValue;
            if (isOldValueRequired) {
                if (hasNewValue) {
                    newValue = toObject(cacheEventData.getDataValue());
                    oldValue = toObject(cacheEventData.getDataOldValue());
                } else {
                    // according to contract of CacheEntryEvent#getValue
                    oldValue = toObject(cacheEventData.getDataValue());
                    newValue = oldValue;
                }
            } else {
                if (hasNewValue) {
                    newValue = toObject(cacheEventData.getDataValue());
                    oldValue = null;
                } else {
                    newValue = null;
                    oldValue = null;
                }
            }
            final CacheEntryEventImpl<K, V> event =
                    new CacheEntryEventImpl<K, V>(source, eventType, key, newValue, oldValue);
            if (filter == null || filter.evaluate(event)) {
                evt.add(event);
            }
        }
        return evt;
    }

    private <T> T toObject(Data data) {
        return serializationService.toObject(data);
    }

}
