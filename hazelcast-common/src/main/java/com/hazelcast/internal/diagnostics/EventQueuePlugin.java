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

import com.hazelcast.internal.util.ItemCounter;
import com.hazelcast.internal.util.executor.StripedExecutor;
import com.hazelcast.logging.ILogger;
import com.hazelcast.spi.properties.HazelcastProperties;
import com.hazelcast.spi.properties.HazelcastProperty;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

import static java.lang.Math.min;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * The EventQueuePlugin checks the event queue and samples the event types if
 * the size is above a certain threshold.
 * <p>
 * This is very useful to figure out why the event queue is running full.
 */
public class EventQueuePlugin extends DiagnosticsPlugin {

    /**
     * The period in seconds this plugin runs.
     * <p>
     * With the EventQueuePlugin one can see what is going on inside the event
     * queue. It makes use of sampling to give some impression of the content.
     * <p>
     * If set to 0, the plugin is disabled.
     */
    public static final HazelcastProperty PERIOD_SECONDS
            = new HazelcastProperty("hazelcast.diagnostics.event.queue.period.seconds", 0, SECONDS);

    /**
     * The minimum number of events in the queue before it is being sampled.
     */
    public static final HazelcastProperty THRESHOLD
            = new HazelcastProperty("hazelcast.diagnostics.event.queue.threshold", 1000);

    /**
     * The number of samples to take from the event queue. Increasing the number
     * of samples gives more accuracy of the content, but it will come at greater
     * price.
     */
    public static final HazelcastProperty SAMPLES
            = new HazelcastProperty("hazelcast.diagnostics.event.queue.samples", 100);

    final ItemCounter<String> occurrenceMap = new ItemCounter<String>();
    private final Random random = new Random();
    private final NumberFormat defaultFormat = NumberFormat.getPercentInstance();

    private final StripedExecutor eventExecutor;
    private final long periodMillis;
    private final int threshold;
    private final int samples;

    private int eventCount;

    public EventQueuePlugin(ILogger logger, StripedExecutor eventExecutor, HazelcastProperties props) {
        super(logger);

        this.defaultFormat.setMinimumFractionDigits(3);
        this.eventExecutor = eventExecutor;

        this.periodMillis = props.getMillis(PERIOD_SECONDS);
        this.threshold = props.getInteger(THRESHOLD);
        this.samples = props.getInteger(SAMPLES);
    }

    @Override
    public long getPeriodMillis() {
        return periodMillis;
    }

    @Override
    public void onStart() {
        logger.info("Plugin:active, period-millis:" + periodMillis + " threshold:" + threshold + " samples:" + samples);
    }

    @Override
    public void run(DiagnosticsLogWriter writer) {
        writer.startSection("EventQueues");

        int index = 1;
        List<BlockingQueue<Runnable>> eventQueues = getEventQueues();
        for (BlockingQueue<Runnable> eventQueue : eventQueues) {
            scan(writer, eventQueue, index++);
        }

        writer.endSection();
    }

    // just for testing
    ItemCounter<String> getOccurrenceMap() {
        return occurrenceMap;
    }

    private List<BlockingQueue<Runnable>> getEventQueues() {
        return eventExecutor.getTaskQueues();
    }

    private void scan(DiagnosticsLogWriter writer, BlockingQueue<Runnable> eventQueue, int index) {
        int sampleCount = sample(eventQueue);
        if (sampleCount < 0) {
            return;
        }

        render(writer, sampleCount, index);
    }

    private void render(DiagnosticsLogWriter writer, int sampleCount, int index) {
        writer.startSection("worker=" + index);

        writer.writeKeyValueEntry("eventCount", eventCount);
        writer.writeKeyValueEntry("sampleCount", sampleCount);
        renderSamples(writer, sampleCount);

        writer.endSection();
    }

    private void renderSamples(DiagnosticsLogWriter writer, int sampleCount) {
        writer.startSection("samples");

        for (String key : occurrenceMap.keySet()) {
            long value = occurrenceMap.get(key);
            if (value == 0) {
                continue;
            }

            double percentage = (1d * value) / sampleCount;
            writer.writeEntry(key + " sampleCount=" + value + " " + defaultFormat.format(percentage));
        }
        occurrenceMap.reset();

        writer.endSection();
    }

    /**
     * Samples the queue.
     *
     * @param queue the queue to sample
     * @return the number of samples, or -1 if there were not sufficient samples
     */
    private int sample(BlockingQueue<Runnable> queue) {
        ArrayList<Runnable> events = new ArrayList<Runnable>(queue);
        eventCount = events.size();
        if (eventCount < threshold) {
            return -1;
        }

        int sampleCount = min(samples, eventCount);
        int actualSampleCount = 0;
        while (actualSampleCount < sampleCount) {
            Runnable runnable = events.get(random.nextInt(eventCount));
            actualSampleCount += sampleRunnable(runnable);
        }

        return actualSampleCount;
    }

    int sampleRunnable(Runnable runnable) {
        occurrenceMap.add(runnable.getClass().getName(), 1);
        return 1;
    }

}
