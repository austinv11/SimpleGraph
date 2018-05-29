package com.austinv11.graphs;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * This interface represents an abstraction for a pruning algorithm.
 *
 * @see com.austinv11.graphs.alg.DijkstraPruneStrategy
 * @see com.austinv11.graphs.alg.KruskalPruneStrategy
 * @see com.austinv11.graphs.alg.PrimPruneStrategy
 */
public interface PruneStrategy<T, V extends Vertex<T>, E extends Edge<T, V>> {

    /**
     * Creates a new, pruned graph from the provided one.
     *
     * @param graph The graph to prune.
     * @return The pruned graph.
     */
    @Nonnull
    Graph<T, V, E> prune(@Nonnull Graph<T, V, E> graph);

    /**
     * Creates a new, pruned graph from the provided one.
     *
     * @param graph The graph to prune.
     * @param newGraphSupplier A supplier to create a new, blank instance.
     * @return The pruned graph.
     */
    @Nonnull
    Graph<T, V, E> prune(@Nonnull Graph<T, V, E> graph, @Nonnull Supplier<? extends Graph<T, V, E>> newGraphSupplier);

    /**
     * Creates a new, pruned graph from the provided one using a specific starting vertex to start from.
     *
     * @param startVertex The starting vertex to being the algorithm from.
     * @param graph The graph to prune.
     * @return The pruned graph.
     */
    @Nonnull
    Graph<T, V, E> prune(@Nonnull V startVertex, @Nonnull Graph<T, V, E> graph);

    /**
     * Creates a new, pruned graph from the provided one using a specific starting vertex to start from.
     *
     * @param startVertex The starting vertex to being the algorithm from.
     * @param graph The graph to prune.
     * @param newGraphSupplier A supplier to create a new, blank instance.
     * @return The pruned graph.
     */
    @Nonnull
    Graph<T, V, E> prune(@Nonnull V startVertex, @Nonnull Graph<T, V, E> graph, @Nonnull Supplier<? extends Graph<T, V, E>> newGraphSupplier);
}
