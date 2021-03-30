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

package com.hazelcast.internal.usercodedeployment.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * Thread-Local Class Cache is useful when the regular class-cache is disabled - we want to keep classes cached
 * at very least for the duration of a single deserialization request. Otherwise things may get funky with e.g.
 * class hierarchies.
 */
public final class ThreadLocalClassCache<E> {

    public static final ThreadLocal<ThreadLocalClassCache> THREAD_LOCAL_CLASS_CACHE = new ThreadLocal<>();

    private int counter = 1;
    private Map<String, E> map = new HashMap<>();

    private ThreadLocalClassCache() {
    }

    private int decCounter() {
        counter--;
        return counter;
    }

    private void incCounter() {
        counter++;
    }

    public static <E> void onStartDeserialization() {
        ThreadLocalClassCache<E> threadLocalClassCache = THREAD_LOCAL_CLASS_CACHE.get();
        if (threadLocalClassCache != null) {
            threadLocalClassCache.incCounter();
        }
    }

    public static <E> void onFinishDeserialization() {
        ThreadLocalClassCache<E> threadLocalClassCache = THREAD_LOCAL_CLASS_CACHE.get();
        if (threadLocalClassCache != null && threadLocalClassCache.decCounter() == 0) {
            THREAD_LOCAL_CLASS_CACHE.remove();
        }
    }

    public static <E> void store(String name, E classSource) {
        ThreadLocalClassCache<E> threadLocalClassCache = THREAD_LOCAL_CLASS_CACHE.get();
        if (threadLocalClassCache == null) {
            threadLocalClassCache = new ThreadLocalClassCache<E>();
            THREAD_LOCAL_CLASS_CACHE.set(threadLocalClassCache);
        }
        threadLocalClassCache.map.put(name, classSource);
    }

    public static <E> E getFromCache(String name) {
        ThreadLocalClassCache<E> threadLocalClassCache = THREAD_LOCAL_CLASS_CACHE.get();
        if (threadLocalClassCache != null) {
            return (E) threadLocalClassCache.map.get(name);
        }
        return null;
    }
}
