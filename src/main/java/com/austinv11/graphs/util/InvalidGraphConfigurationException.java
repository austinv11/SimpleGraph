package com.austinv11.graphs.util;

/**
 * Thrown when an operation cannot be completed due to an incompatible configuration.
 */
public class InvalidGraphConfigurationException extends RuntimeException {

    public InvalidGraphConfigurationException() {
        super();
    }

    public InvalidGraphConfigurationException(String message) {
        super(message);
    }
}
