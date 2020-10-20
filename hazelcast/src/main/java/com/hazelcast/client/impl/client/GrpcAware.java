package com.hazelcast.client.impl.client;

import com.hazelcast.internal.serialization.SerializationService;

public interface GrpcAware {
    void setGrpcService(GrpcServiceImpl service, SerializationService serializationService);
}
