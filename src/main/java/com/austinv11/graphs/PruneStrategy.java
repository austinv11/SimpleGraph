package com.austinv11.graphs;

import javax.annotation.Nonnull;

public interface PruneStrategy<T, V extends Vertex<T>, E extends Edge<T, V>> {

    @Nonnull
    Graph<T, V, E> prune(@Nonnull V startVertex, @Nonnull Graph<T, V, E> graph);
}
