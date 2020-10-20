package com.hazelcast.client.impl.client;

public interface GrpcAware {
    void setGrpcService(GrpcServiceImpl service);
}
