package com.hazelcast.instance.impl;

import com.hazelcast.core.HazelcastInstance;

import static com.hazelcast.internal.util.EmptyStatement.ignore;

public class ServerOutOfMemoryHandler extends DefaultOutOfMemoryHandler {

    @Override
    public void onOutOfMemory(OutOfMemoryError oome, HazelcastInstance[] hazelcastInstances) {
        for (HazelcastInstance instance : hazelcastInstances) {
            OutOfMemoryHandlerHelper.tryCloseConnections(instance);
            OutOfMemoryHandlerHelper.tryShutdown(instance);
        }
        try {
            oome.printStackTrace(System.err);
        } catch (Throwable ignored) {
            ignore(ignored);
        }
    }
}
