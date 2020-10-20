package com.hazelcast.client.impl.client;

import com.hazelcast.client.impl.ClientDataSerializerHook;
import com.hazelcast.core.Offloadable;
import com.hazelcast.internal.serialization.SerializationService;
import com.hazelcast.internal.serialization.impl.HeapData;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.map.impl.LockAwareLazyMapEntry;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import io.grpc.StatusRuntimeException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;

public class PythonEntryProcessorSerializable implements IdentifiedDataSerializable, EntryProcessor, Offloadable, GrpcAware {
    private byte[] entryProcessor;
    private GrpcServiceImpl grpcService;
    private SerializationService serializationService;


    public PythonEntryProcessorSerializable() {
    }


    public static PythonEntryProcessorSerializable from(byte[] b) {
        PythonEntryProcessorSerializable serializable = new PythonEntryProcessorSerializable();
        serializable.entryProcessor = b;
        return serializable;
    }

    @Override
    public int getFactoryId() {
        return ClientDataSerializerHook.F_ID;
    }

    @Override
    public int getClassId() {
        return ClientDataSerializerHook.PYTHON_ENTRY_PROCESSOR_SERIALIZABLE;
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
        LockAwareLazyMapEntry e = (LockAwareLazyMapEntry) entry;
        Grpc.ProcessRequest request = grpcService.createProcessRequest(entryProcessor, e);
        try {
            Grpc.ProcessReply response = grpcService.getStub(ClientType.PYTHON).process(request);
            if (response.getMutate()) {
                HeapData data = new HeapData(response.getNewValueData().toByteArray());
                e.setValue(serializationService.toObject(data));
            }
            return new HeapData(response.getResultData().toByteArray());
        } catch (StatusRuntimeException ignored) {
        }
        return null;
    }

    @Override
    public String getExecutorName() {
        return ClientType.PYTHON.name();
    }

    @Override
    public void setGrpcService(GrpcServiceImpl service, SerializationService serializationService) {
        this.grpcService = service;
        this.serializationService = serializationService;
    }
}
