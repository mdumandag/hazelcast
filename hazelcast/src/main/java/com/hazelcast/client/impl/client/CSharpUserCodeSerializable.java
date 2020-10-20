package com.hazelcast.client.impl.client;

import com.google.protobuf.ByteString;
import com.hazelcast.client.impl.ClientDataSerializerHook;
import com.hazelcast.core.Offloadable;
import com.hazelcast.internal.serialization.impl.HeapData;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.map.impl.LockAwareLazyMapEntry;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import io.grpc.StatusRuntimeException;

import java.io.IOException;
import java.util.Map;

public class CSharpUserCodeSerializable implements IdentifiedDataSerializable, EntryProcessor, Offloadable, GrpcAware {
    private byte[] entryProcessor;
    private GrpcServiceImpl grpcService;


    public CSharpUserCodeSerializable() {
    }


    public static CSharpUserCodeSerializable from(byte[] b) {
        CSharpUserCodeSerializable serializable = new CSharpUserCodeSerializable();
        serializable.entryProcessor = b;
        return serializable;
    }

    @Override
    public int getFactoryId() {
        return ClientDataSerializerHook.F_ID;
    }

    @Override
    public int getClassId() {
        return ClientDataSerializerHook.CSHARP_USER_CODE_SERIALIZABLE;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeByteArray(entryProcessor);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        entryProcessor = in.readByteArray();
    }

    @Override
    public Object process(Map.Entry entry) {
        Grpc.ProcessRequest request = createRequest((LockAwareLazyMapEntry) entry);
        Grpc.ProcessReply response;
        try {
            response = grpcService.getStub(ClientType.CSHARP).process(request);
            return new HeapData(response.getResultData().toByteArray());
        } catch (StatusRuntimeException ignored) {
        }
        return null;
    }

    @Override
    public String getExecutorName() {
        return ClientType.CSHARP.name();
    }

    @Override
    public void setGrpcService(GrpcServiceImpl service) {
        this.grpcService = service;
    }

    private Grpc.ProcessRequest createRequest(LockAwareLazyMapEntry entry) {
        return Grpc.ProcessRequest.newBuilder()
                .setProcessorData(ByteString.copyFrom(entryProcessor))
                .setKeyData(ByteString.copyFrom(entry.getKeyData().toByteArray()))
                .setValueData(ByteString.copyFrom(entry.getValueData().toByteArray()))
                .build();
    }
}
