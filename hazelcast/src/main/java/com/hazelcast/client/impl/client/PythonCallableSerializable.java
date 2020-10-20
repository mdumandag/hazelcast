package com.hazelcast.client.impl.client;

import com.hazelcast.client.impl.ClientDataSerializerHook;
import com.hazelcast.core.Offloadable;
import com.hazelcast.internal.serialization.SerializationService;
import com.hazelcast.internal.serialization.impl.HeapData;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import io.grpc.StatusRuntimeException;

import java.io.IOException;
import java.util.concurrent.Callable;

public class PythonCallableSerializable implements IdentifiedDataSerializable, Offloadable, GrpcAware, Callable {
    private byte[] callable;
    private GrpcServiceImpl grpcService;
    private SerializationService serializationService;


    public PythonCallableSerializable() {
    }

    public static PythonCallableSerializable from(byte[] b) {
        PythonCallableSerializable serializable = new PythonCallableSerializable();
        serializable.callable = b;
        return serializable;
    }

    @Override
    public int getFactoryId() {
        return ClientDataSerializerHook.F_ID;
    }

    @Override
    public int getClassId() {
        return ClientDataSerializerHook.PYTHON_CALLABLE_SERIALIZABLE;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeByteArray(callable);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        callable = in.readByteArray();
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

    @Override
    public Object call() throws Exception {
        Grpc.CallRequest request = grpcService.createCallRequest(callable);
        try {
            Grpc.CallReply reply = grpcService.getStub(ClientType.PYTHON).call(request);
            return serializationService.toObject(new HeapData(reply.getResultData().toByteArray()));
        } catch (StatusRuntimeException ignored) {
        }
        return null;
    }
}
