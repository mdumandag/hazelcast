package com.hazelcast.cp.internal.datastructures.atomicref;

/**
 * Used for specifying return value of the operation
 */
public enum ReturnValueType {
    /**
     * Returns no value after applying the given function
     */
    NO_RETURN_VALUE(0),
    /**
     * Returns the value which resides before the given function is applied
     */
    RETURN_OLD_VALUE(1),
    /**
     * Returns the value after the given function is applied
     */
    RETURN_NEW_VALUE(2);

    private final int value;

    ReturnValueType(int value) {
        this.value = value;
    }

    public static ReturnValueType fromValue(int value) {
        switch (value) {
            case 0:
                return NO_RETURN_VALUE;
            case 1:
                return RETURN_OLD_VALUE;
            case 2:
                return RETURN_NEW_VALUE;
            default:
                throw new IllegalArgumentException("No " + ReturnValueType.class + " for value: " + value);
        }
    }

    public int value() {
        return value;
    }
}
