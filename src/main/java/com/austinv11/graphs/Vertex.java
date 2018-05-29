package com.austinv11.graphs;

import javax.annotation.Nullable;

/**
 * This represents a vertex in a graph.
 */
public interface Vertex<T> {

    /**
     * Gets the value associated with this vertex.
     *
     * @return The value held in this vertex.
     */
    @Nullable
    T get();
}
