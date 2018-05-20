package com.austinv11.graphs;

import javax.annotation.Nonnull;

public interface PruneStrategy<T, V extends Vertex<T>, E extends Edge<T, V>, G extends Graph<T, V, E>> {

    @Nonnull
    G prune(@Nonnull G graph);
}
