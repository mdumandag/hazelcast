package com.hazelcast.spi.impl.operationservice.impl;

import com.hazelcast.internal.util.concurrent.BackoffIdleStrategy;
import com.hazelcast.internal.util.concurrent.BusySpinIdleStrategy;
import com.hazelcast.internal.util.concurrent.IdleStrategy;
import com.hazelcast.spi.properties.HazelcastProperties;
import com.hazelcast.spi.properties.HazelcastProperty;

import static com.hazelcast.internal.util.concurrent.BackoffIdleStrategy.createBackoffIdleStrategy;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class ResponseHandlerUtil {

    private ResponseHandlerUtil() {
    }

    private static final long IDLE_MAX_SPINS = 20;
    private static final long IDLE_MAX_YIELDS = 50;
    private static final long IDLE_MIN_PARK_NS = NANOSECONDS.toNanos(1);
    private static final long IDLE_MAX_PARK_NS = MICROSECONDS.toNanos(100);

    public static IdleStrategy getIdleStrategy(HazelcastProperties properties, HazelcastProperty property) {
        String idleStrategyString = properties.getString(property);
        if ("block".equals(idleStrategyString)) {
            return null;
        } else if ("busyspin".equals(idleStrategyString)) {
            return new BusySpinIdleStrategy();
        } else if ("backoff".equals(idleStrategyString)) {
            return new BackoffIdleStrategy(IDLE_MAX_SPINS, IDLE_MAX_YIELDS, IDLE_MIN_PARK_NS, IDLE_MAX_PARK_NS);
        } else if (idleStrategyString.startsWith("backoff,")) {
            return createBackoffIdleStrategy(idleStrategyString);
        } else {
            throw new IllegalStateException("Unrecognized " + property.getName() + " value=" + idleStrategyString);
        }
    }
}
