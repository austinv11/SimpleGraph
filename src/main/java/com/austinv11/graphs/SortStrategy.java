package com.austinv11.graphs;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * This interface represents an abstraction for a graph sorting algorithm.
 *
 * @see com.austinv11.graphs.alg.NaturalSortStrategy
 * @see com.austinv11.graphs.alg.TopologicalSortStrategy
 */
public interface SortStrategy<T, V extends Vertex<T>, E extends Edge<T, V>, G extends Graph<T, V, E>> {

    /**
     * Sorts the vertices in a graph.
     *
     * @param graph The graph to sort.
     * @return The vertices in a sorted order.
     */
    @Nonnull
    List<V> sort(@Nonnull G graph);
}
