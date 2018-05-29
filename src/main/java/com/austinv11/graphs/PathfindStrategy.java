package com.austinv11.graphs;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * This interface represents an abstraction for a pathfinding algorithm.
 *
 * @see com.austinv11.graphs.alg.DijkstraPathfindStrategy
 * @see com.austinv11.graphs.alg.BreadthFirstSearch
 * @see com.austinv11.graphs.alg.DepthFirstSearch
 */
public interface PathfindStrategy<T, V extends Vertex<T>, E extends Edge<T, V>, G extends Graph<T, V, E>> {

    /**
     * Finds a path between two vertices in a graph.
     *
     * @param vertex1 The starting vertex.
     * @param vertex2 The destination vertex.
     * @param graph The graph to search.
     * @return The path, or empty if not possible.
     */
    @Nonnull
    List<E> pathfind(@Nonnull V vertex1, @Nonnull V vertex2, @Nonnull G graph);
}
