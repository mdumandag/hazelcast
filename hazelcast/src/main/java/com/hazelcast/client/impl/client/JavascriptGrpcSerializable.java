package com.hazelcast.client.impl.client;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;

public class JavascriptGrpcSerializable implements IdentifiedDataSerializable {
    @Override
    public int getFactoryId() {
        return 0;
    }

    @Override
    public int getClassId() {
        return 0;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {

    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {

    }
}
