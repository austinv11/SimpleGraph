package com.austinv11.graphs;

import javax.annotation.Nonnull;

/**
 * This interface represents an abstraction for a cycle detection algorithm.
 *
 * @see com.austinv11.graphs.alg.ColoringCycleDetectionStrategy
 */
public interface CycleDetectionStrategy<T, V extends Vertex<T>, E extends Edge<T, V>, G extends Graph<T, V, E>> {

    /**
     * Takes a graph and searches it for cycles.
     *
     * @param graph The graph to search.
     * @return True if cycles are present, false if otherwise.
     */
    boolean findCycle(@Nonnull G graph);
}
