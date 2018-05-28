package com.austinv11.graphs;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public interface PruneStrategy<T, V extends Vertex<T>, E extends Edge<T, V>> {

    @Nonnull
    Graph<T, V, E> prune(@Nonnull Graph<T, V, E> graph);

    @Nonnull
    Graph<T, V, E> prune(@Nonnull Graph<T, V, E> graph, @Nonnull Supplier<? extends Graph<T, V, E>> newGraphSupplier);

    @Nonnull
    Graph<T, V, E> prune(@Nonnull V startVertex, @Nonnull Graph<T, V, E> graph);

    @Nonnull
    Graph<T, V, E> prune(@Nonnull V startVertex, @Nonnull Graph<T, V, E> graph, @Nonnull Supplier<? extends Graph<T, V, E>> newGraphSupplier);
}
