package com.hazelcast.client.impl.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.ConcurrentHashMap;

public class GrpcServiceImpl {

    private final ConcurrentHashMap<ClientType, ProcessorGrpc.ProcessorBlockingStub> stubs;

    public GrpcServiceImpl() {
        this.stubs = new ConcurrentHashMap<>();
    }

    public ProcessorGrpc.ProcessorBlockingStub getStub(ClientType clientType) {
        return stubs.computeIfAbsent(clientType, this::createStub);
    }

    private ProcessorGrpc.ProcessorBlockingStub createStub(ClientType clientType) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(clientType.getAddress())
                .usePlaintext()
                .build();
        return ProcessorGrpc.newBlockingStub(channel);
    }

}
