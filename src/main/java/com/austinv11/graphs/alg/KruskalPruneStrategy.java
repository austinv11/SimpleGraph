package com.austinv11.graphs.alg;

import com.austinv11.graphs.Edge;
import com.austinv11.graphs.Graph;
import com.austinv11.graphs.PruneStrategy;
import com.austinv11.graphs.Vertex;
import com.austinv11.graphs.impl.SimpleGraph;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Supplier;

/**
 * This implements Kruskal's algorithm for the creation of a minimum-spanning tree. This algorithm is asymptotically
 * O(|E|log|V|). This assumes weighted graphs, unweighted graphs will create undefined results. Additionally,
 * the graph should be fully connected, otherwise the resultant tree is likely to be disjoint.
 *
 * Additionally, this supports being called with a starting vertex, however this parameter is totally ignored
 * {@link com.austinv11.graphs.alg.PrimPruneStrategy} is more suited to this use-case.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Kruskal%27s_algorithm">Wikipedia page</a>
 * @see com.austinv11.graphs.alg.PrimPruneStrategy
 * @see com.austinv11.graphs.alg.DijkstraPruneStrategy
 */
public class KruskalPruneStrategy<T, V extends Vertex<T>, E extends Edge<T, V>> implements PruneStrategy<T, V, E> {

    @Nonnull
    @Override
    public Graph<T, V, E> prune(@Nonnull Graph<T, V, E> graph) {
        return prune(graph, SimpleGraph::new);
    }

    @Nonnull
    @Override
    public Graph<T, V, E> prune(@Nonnull Graph<T, V, E> graph, @Nonnull Supplier<? extends Graph<T, V, E>> newGraphSupplier) {
        Queue<E> pq = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));
        Graph<T, V, E> tree = newGraphSupplier.get();
        Set<V> visited = new HashSet<>();
        pq.addAll(graph.edges());
        int vertexCount = graph.getVertexCount();
        while (visited.size() != vertexCount && !pq.isEmpty()) {
            E edge = pq.poll();
            if (!visited.contains(edge.getFirstVertex())
                    && !visited.contains(edge.getSecondVertex())
                    && !edge.isLoop()) {
                visited.add(edge.getFirstVertex());
                visited.add(edge.getSecondVertex());
                tree.addEdge(edge);
            }
        }
        return tree;
    }

    @Nonnull
    @Override
    public Graph<T, V, E> prune(@Nonnull V startVertex, @Nonnull Graph<T, V, E> graph) {
        return prune(graph);
    }

    @Nonnull
    @Override
    public Graph<T, V, E> prune(@Nonnull V startVertex, @Nonnull Graph<T, V, E> graph, @Nonnull Supplier<? extends Graph<T, V, E>> newGraphSupplier) {
        return prune(graph, newGraphSupplier);
    }
}
