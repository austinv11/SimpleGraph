package com.austinv11.graphs.alg;

import com.austinv11.graphs.Edge;
import com.austinv11.graphs.Graph;
import com.austinv11.graphs.PruneStrategy;
import com.austinv11.graphs.Vertex;
import com.austinv11.graphs.impl.SimpleGraph;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * This implements Prim's algorithm for the creation of a minimum-spanning tree, given a starting vertex. This
 * algorithm is asymptotically O(|E| + |V|log|V|). This assumes weighted graphs, unweighted graphs will create
 * undefined results. Additionally, the graph should be fully connected, otherwise vertices will be pruned off.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Prim%27s_algorithm">Wikipedia page</a>
 * @see com.austinv11.graphs.alg.KruskalPruneStrategy
 * @see com.austinv11.graphs.alg.DijkstraPathfindStrategy
 */
public class PrimPruneStrategy<T, V extends Vertex<T>, E extends Edge<T, V>> implements PruneStrategy<T, V, E> {

    @Override
    @Nonnull
    public Graph<T, V, E> prune(@Nonnull V startVertex, @Nonnull Graph<T, V, E> graph) {
        return prune(startVertex, graph, SimpleGraph::new);
    }

    @Override
    @Nonnull
    public Graph<T, V, E> prune(@Nonnull V startVertex, @Nonnull Graph<T, V, E> graph, @Nonnull Supplier<? extends Graph<T, V, E>> newGraphSupplier) {
        Queue<E> pq = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));
        Graph<T, V, E> tree = newGraphSupplier.get();
        Set<V> visited = new HashSet<>();
        V currVertex = startVertex;
        while (currVertex != null) {
            visited.add(currVertex);
            final V finalCurrVertex = currVertex;
            pq.addAll(graph.getOutwardEdges(currVertex)
                    .stream()
                    .map(e -> (E) e.ensureOrder(finalCurrVertex))
                    .collect(Collectors.toSet()));
            pq.removeIf(e -> visited.contains(e.getSecondVertex()));

            if (pq.isEmpty()) {
                currVertex = null;
            } else {
                E lowest = pq.poll();
                currVertex = Objects.requireNonNull(lowest).getSecondVertex();
                tree.addEdge(lowest);
            }
        }
        return tree;
    }
}
