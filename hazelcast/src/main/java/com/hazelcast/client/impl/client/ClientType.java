package com.hazelcast.client.impl.client;

public enum ClientType {
    CSHARP("localhost:50051"),
    PYTHON("localhost:50052"),
    CPP("localhost:50053"),
    NODEJS("localhost:50054"),
    GOLANG("localhost:50055");

    private final String address;

    ClientType(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
