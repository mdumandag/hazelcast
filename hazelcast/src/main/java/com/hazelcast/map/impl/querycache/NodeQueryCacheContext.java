package com.hazelcast.map.impl.querycache;

import com.hazelcast.map.impl.querycache.publisher.PublisherContext;

public interface NodeQueryCacheContext extends QueryCacheContext {

    /**
     * Returns {@link PublisherContext} for this context.
     *
     * @return {@link PublisherContext}
     * @see PublisherContext
     */
    PublisherContext getPublisherContext();

}
