package com.hazelcast.client.impl.client;

import com.google.protobuf.ByteString;
import com.hazelcast.internal.serialization.Data;
import com.hazelcast.map.impl.LockAwareLazyMapEntry;
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

    public ProcessorGrpc.ProcessorBlockingStub createStub(ClientType clientType) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(clientType.getAddress())
                .usePlaintext()
                .build();
        return ProcessorGrpc.newBlockingStub(channel);
    }

    public Grpc.ProcessRequest createProcessRequest(byte[] entryProcessor, LockAwareLazyMapEntry entry) {
        return Grpc.ProcessRequest.newBuilder()
                .setProcessorData(ByteString.copyFrom(entryProcessor))
                .setKeyData(ByteString.copyFrom(entry.getKeyData().toByteArray()))
                .setValueData(ByteString.copyFrom(entry.getValueData().toByteArray()))
                .build();
    }

    public Grpc.CallRequest createCallRequest(byte[] callable) {
        return Grpc.CallRequest.newBuilder()
                .setCallableData(ByteString.copyFrom(callable))
                .build();
    }
}
