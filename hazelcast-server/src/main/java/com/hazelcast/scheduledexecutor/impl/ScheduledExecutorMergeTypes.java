package com.hazelcast.scheduledexecutor.impl;

import com.hazelcast.spi.merge.MergingEntry;

/**
 * Provided merge types of {@link com.hazelcast.scheduledexecutor.IScheduledExecutorService}.
 *
 * @since 3.10
 * See {@link com.hazelcast.spi.merge.SplitBrainMergeTypes}
 */
public interface ScheduledExecutorMergeTypes extends MergingEntry<String, ScheduledTaskDescriptor>  {
}
