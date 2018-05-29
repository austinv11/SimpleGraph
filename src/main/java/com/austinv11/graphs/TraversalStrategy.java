package com.austinv11.graphs;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This interface represents an abstraction for a graph traversal algorithm.
 */
public interface TraversalStrategy<T, V extends Vertex<T>, E extends Edge<T, V>, G extends Graph<T, V, E>> {

    /**
     * Traverses a graph.
     *
     * @param graph The graph to traverse.
     * @return The vertices visited in the graph.
     */
    @Nonnull
    Collection<V> traverseVertices(@Nonnull G graph);

    /**
     * Traverses a graph.
     *
     * @param graph The graph to traverse.
     * @return The edges visited in the graph.
     */
    @Nonnull
    Collection<E> traverseEdges(@Nonnull G graph);

    /**
     * Traverses a graph.
     *
     * @param graph The graph to traverse.
     * @return The values visited in the graph.
     */
    @Nonnull
    default Collection<T> traverseValues(@Nonnull G graph) {
        return traverseVertices(graph).stream().map(V::get).collect(Collectors.toList());
    }
}
