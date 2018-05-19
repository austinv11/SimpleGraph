package com.austinv11.graphs;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.stream.Collectors;

public interface TraversalStrategy<T, V extends Vertex<T>, E extends Edge<T, V>, G extends Graph<T, V, E>> {

    @Nonnull
    Collection<V> traverseVertices(@Nonnull G graph);

    @Nonnull
    Collection<E> traverseEdges(@Nonnull G graph);

    @Nonnull
    default Collection<T> traverseValues(@Nonnull G graph) {
        return traverseVertices(graph).stream().map(V::get).collect(Collectors.toList());
    }
}
