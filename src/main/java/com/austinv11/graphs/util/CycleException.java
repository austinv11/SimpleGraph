package com.austinv11.graphs.util;

public class CycleException extends RuntimeException {

    public CycleException() {
        super("Cycle detected!");
    }
}
