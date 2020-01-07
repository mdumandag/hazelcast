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

package com.hazelcast.collection.impl.queue;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.collection.IQueue;
import com.hazelcast.test.HazelcastParallelClassRunner;
import com.hazelcast.test.HazelcastTestSupport;
import com.hazelcast.test.annotation.ParallelJVMTest;
import com.hazelcast.test.annotation.QuickTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(HazelcastParallelClassRunner.class)
@Category({QuickTest.class, ParallelJVMTest.class})
public class QueueIteratorTest extends HazelcastTestSupport {

    @Test
    public void testIterator() {
        IQueue<String> queue = newQueue();
        for (int i = 0; i < 10; i++) {
            queue.offer("item" + i);
        }
        Iterator<String> iterator = queue.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Object o = iterator.next();
            assertEquals(o, "item" + i++);
        }
    }

    @Test
    public void testIterator_whenQueueEmpty() {
        IQueue<String> queue = newQueue();
        Iterator<String> iterator = queue.iterator();

        assertFalse(iterator.hasNext());
        try {
            assertNull(iterator.next());
            fail();
        } catch (NoSuchElementException e) {
            ignore(e);
        }
    }

    @Test
    public void testIteratorRemove() {
        IQueue<String> queue = newQueue();
        for (int i = 0; i < 10; i++) {
            queue.offer("item" + i);
        }

        Iterator<String> iterator = queue.iterator();
        iterator.next();
        try {
            iterator.remove();
            fail();
        } catch (UnsupportedOperationException e) {
            ignore(e);
        }

        assertEquals(10, queue.size());
    }

    private IQueue<String> newQueue() {
        HazelcastInstance instance = createHazelcastInstance();
        return instance.getQueue(randomString());
    }
}
