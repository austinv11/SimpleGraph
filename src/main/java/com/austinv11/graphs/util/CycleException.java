package com.austinv11.graphs.util;

/**
 * Thrown when a cycle is detected.
 */
public class CycleException extends RuntimeException {

    public CycleException() {
        super("Cycle detected!");
    }
}
