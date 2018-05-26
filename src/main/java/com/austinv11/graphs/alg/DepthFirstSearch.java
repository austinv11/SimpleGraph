package com.austinv11.graphs.alg;

import com.austinv11.graphs.Edge;
import com.austinv11.graphs.Graph;
import com.austinv11.graphs.PathfindStrategy;
import com.austinv11.graphs.Vertex;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * This implements a naive depth-first search strategy. This does not guarantee any properties regarding
 * the path found. This may have better performance than something like
 * {@link com.austinv11.graphs.alg.DijkstraPathfindStrategy} in some cases, however it is generally preferred
 * to use a more advanced strategy like the aforementioned {@link com.austinv11.graphs.alg.DijkstraPathfindStrategy}.
 */
public class DepthFirstSearch<T, V extends Vertex<T>, E extends Edge<T, V>> implements PathfindStrategy<T, V, E, Graph<T, V, E>> {

    @Override
    @Nonnull
    public List<E> pathfind(@Nonnull V vertex1, @Nonnull V vertex2, @Nonnull Graph<T, V, E> graph) {
        return pathfind(vertex1, vertex2, graph, new HashSet<>());
    }

    @Nonnull
    private List<E> pathfind(@Nonnull V vertex1, @Nonnull V vertex2, @Nonnull Graph<T, V, E> graph, Set<V> visited) {
        Collection<E> edges = graph.getOutwardEdges(vertex1);
        visited.add(vertex1);
        for (E e : edges) {
            E ordered = (E) e.ensureOrder(vertex1, vertex2);
            if (e.contains(vertex2)) {
                return Arrays.asList(ordered); // We need a mutable list, Collections.singletonList() returns an immutable one
            } else {
                List<E> subpath = pathfind(ordered.getSecondVertex(), vertex2, graph, visited);
                if (!subpath.isEmpty()) {
                    subpath.add(0, ordered);
                    return subpath;
                }
            }
        }
        return Collections.emptyList();
    }
}
