package com.austinv11.graphs.alg;

import com.austinv11.graphs.Edge;
import com.austinv11.graphs.Graph;
import com.austinv11.graphs.PruneStrategy;
import com.austinv11.graphs.Vertex;
import com.austinv11.graphs.impl.DirectedAcyclicGraph;
import com.austinv11.graphs.impl.SimpleGraph;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Supplier;

/**
 * This implements an adaption of Dijkstra's greedy shortest path algorithm to create a shortest path tree from the
 * specified starting vertex. The adaption is based off of the code in
 * {@link com.austinv11.graphs.alg.DijkstraPathfindStrategy} so refer to those docs for specific implementation details.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm">Wikipedia page</a>
 * @see <a href="https://github.com/trudeau/fibonacci-heap">Java Fibonacci Heap implementation</a>
 * @see com.austinv11.graphs.alg.DijkstraPathfindStrategy
 * @see com.austinv11.graphs.alg.KruskalPruneStrategy
 * @see com.austinv11.graphs.alg.PrimPruneStrategy
 */
public class DijkstraPruneStrategy<T, V extends Vertex<T>, E extends Edge<T, V>> implements PruneStrategy<T, V, E> {

    @Override
    @Nonnull
    public Graph<T, V, E> prune(@Nonnull V startVertex, @Nonnull Graph<T, V, E> graph) {
        return prune(startVertex, graph, PriorityQueue::new);
    }

    @Nonnull
    public Graph<T, V, E> prune(@Nonnull V startVertex, @Nonnull Graph<T, V, E> graph, @Nonnull Supplier<Queue<? extends Comparable<?>>> queueSupplier) {
        Map<V, DijkstraNode<V>> nodeMap = new HashMap<>();
        Set<V> visited = new HashSet<>();

        DijkstraNode<V> currNode = new DijkstraNode<>(startVertex, 0D);
        nodeMap.put(startVertex, currNode);

        Queue<DijkstraNode<V>> pq = (Queue<DijkstraNode<V>>) queueSupplier.get();

        while (Double.isFinite(currNode.tenativeDistance)) {
            visited.add(currNode.vertex);

            Collection<E> edges = graph.getOutwardEdges(currNode.vertex);
            for (E edge : edges) {
                edge = (E) edge.ensureOrder(currNode.vertex);
                if (!visited.contains(edge.getSecondVertex())) {
                    DijkstraNode<V> neighbor = nodeMap.computeIfAbsent(edge.getSecondVertex(), DijkstraNode::new);
                    if (currNode.tenativeDistance + edge.getWeight() < neighbor.tenativeDistance) {
                        neighbor.tenativeDistance = currNode.tenativeDistance + edge.getWeight();
                        if (currNode.getHint() == null || currNode.getHint().tenativeDistance > neighbor.tenativeDistance) {
                            currNode.hint(neighbor, edge);
                        }
                        pq.remove(neighbor);  //Required to ensure the pq updates the order correctly
                        pq.add(neighbor);
                    }
                }
            }

            if (pq.isEmpty())
                currNode = null;
            else
                currNode = pq.poll();

            if (currNode == null)
                break;
        }

        DirectedAcyclicGraph<T, V, E> newGraph = new DirectedAcyclicGraph<>(new SimpleGraph<>(false));
        boolean origCycleDetect = newGraph.isDetectingCycles();
        newGraph.setCycleDetection(false); //Improve performance during graph construction as this should output a DAG

        for (DijkstraNode<V> node : nodeMap.values()) {
            if (Double.isFinite(node.getTenativeDistance())) {
                newGraph.addEdge((E) node.getHintEdge());
            }
        }

        newGraph.setCycleDetection(origCycleDetect);
        return newGraph;
    }
}
