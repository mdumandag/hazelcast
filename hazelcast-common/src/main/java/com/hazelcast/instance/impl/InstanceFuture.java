package com.hazelcast.instance.impl;

public class InstanceFuture<T> {
    private volatile T hz;
    private volatile Throwable throwable;

    public T get() {
        if (hz != null) {
            return hz;
        }

        boolean restoreInterrupt = false;
        synchronized (this) {
            while (hz == null && throwable == null) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                    restoreInterrupt = true;
                }
            }
        }

        if (restoreInterrupt) {
            Thread.currentThread().interrupt();
        }

        if (hz != null) {
            return hz;
        }

        throw new IllegalStateException(throwable);
    }

    public void set(T proxy) {
        synchronized (this) {
            this.hz = proxy;
            notifyAll();
        }
    }

    public void setFailure(Throwable throwable) {
        synchronized (this) {
            this.throwable = throwable;
            notifyAll();
        }
    }

    public boolean isSet() {
        return hz != null;
    }
}