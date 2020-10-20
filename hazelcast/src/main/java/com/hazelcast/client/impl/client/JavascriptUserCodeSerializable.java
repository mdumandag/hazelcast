package com.hazelcast.client.impl.client;

import com.hazelcast.client.impl.ClientDataSerializerHook;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IFunction;
import com.hazelcast.core.Offloadable;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import jdk.nashorn.api.scripting.JSObject;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;

public class JavascriptUserCodeSerializable implements IdentifiedDataSerializable, Callable, Runnable,
        HazelcastInstanceAware, EntryProcessor, IFunction, Offloadable {
    private HazelcastInstance member;
    private String functionDefinition;
    private JSObject function;

    public JavascriptUserCodeSerializable(){
    }

    public static JavascriptUserCodeSerializable from(String script) {
        JavascriptUserCodeSerializable serializable = new JavascriptUserCodeSerializable();
        serializable.functionDefinition = script;
        return serializable;
    }

    @Override
    public int getFactoryId() {
        return ClientDataSerializerHook.F_ID;
    }

    @Override
    public int getClassId() {
        return ClientDataSerializerHook.JAVASCRIPT_USER_CODE_SERIALIZABLE;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(functionDefinition);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        functionDefinition = in.readUTF();
        eval();
    }

    @Override
    public Object call() throws Exception {
        return function.call(member);
    }

    @Override
    public void run() {
        function.call(member);
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.member = hazelcastInstance;
    }

    @Override
    public Object process(Map.Entry entry) {
        return function.call(member, entry);
    }

    @Override
    public Object apply(Object input) {
        return function.call(member, input);
    }

    private void eval() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        try {
            function = (JSObject) engine.eval(functionDefinition);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getExecutorName() {
        return "javascript";
    }
}
