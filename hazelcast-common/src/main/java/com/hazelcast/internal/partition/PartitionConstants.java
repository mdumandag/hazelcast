package com.hazelcast.internal.partition;

public interface PartitionConstants {

    /**
     * The maximum number of backups.
     */
    int MAX_BACKUP_COUNT = 6;

    int MAX_REPLICA_COUNT = MAX_BACKUP_COUNT + 1;
}
