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

package com.hazelcast.client.impl.clientside;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import com.hazelcast.partition.PartitionAware;
import com.hazelcast.scheduledexecutor.impl.ScheduledExecutorDataSerializerHook;

import java.io.IOException;
import java.util.concurrent.Callable;

public class ScheduledRunnableAdapter implements IdentifiedDataSerializable, PartitionAware, Callable {
    private Runnable task;

    public ScheduledRunnableAdapter(Runnable task) {
        this.task = task;
    }

    public Runnable getRunnable() {
        return task;
    }

    public void setRunnable(Runnable runnable) {
        task = runnable;
    }

    @Override
    public Object getPartitionKey() {
        if (task instanceof PartitionAware) {
            return ((PartitionAware) task).getPartitionKey();
        }
        return null;
    }

    @Override
    public void writeData(ObjectDataOutput out)
            throws IOException {
        out.writeObject(task);
    }

    @Override
    public void readData(ObjectDataInput in)
            throws IOException {
        task = in.readObject();
    }

    @Override
    public int getFactoryId() {
        return ScheduledExecutorDataSerializerHook.F_ID;
    }

    @Override
    public int getClassId() {
        return ScheduledExecutorDataSerializerHook.RUNNABLE_ADAPTER;
    }

    @Override
    public Object call() throws Exception {
        return null;
    }
}
