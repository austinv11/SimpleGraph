package com.austinv11.graphs.alg;

import com.austinv11.graphs.Edge;
import com.austinv11.graphs.Graph;
import com.austinv11.graphs.PathfindStrategy;
import com.austinv11.graphs.Vertex;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Supplier;

/**
 * This implements Dijkstra's greedy shortest path algorithm. This works best with weighted graphs, but can also work
 * on unweighted graphs. This algorithm is asymptotically O(|E| + |V|log|V|). By default it uses the jdk's
 * {@link java.util.PriorityQueue}, however it supports the usage of fibonacci heaps which may improve performance on
 * large graphs as long as the implementation implements {@link java.util.Queue}.
 *
 * @implNote *Technically* this is not the "official" Dijkstra's algorithm, instead it more closely resembles a
 * "uniform cost search". In essence, this means that the queue used to sort distances are populated as the algorithm
 * runs instead of starting fully populated, improving the speed significantly on large graphs.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm">Wikipedia page</a>
 * @see <a href="https://github.com/trudeau/fibonacci-heap">Java Fibonacci Heap implementation</a>
 * @see com.austinv11.graphs.alg.DijkstraPruneStrategy
 */
public class DijkstraPathfindStrategy<T, V extends Vertex<T>, E extends Edge<T, V>> implements PathfindStrategy<T, V, E, Graph<T, V, E>>  {

    private final Supplier<Queue<? extends Comparable<?>>> queueSupplier;

    public DijkstraPathfindStrategy() {
        this(PriorityQueue::new);
    }

    public DijkstraPathfindStrategy(Supplier<Queue<? extends Comparable<?>>> queueSupplier) {
        this.queueSupplier = queueSupplier;
    }

    @Override
    @Nonnull
    public List<E> pathfind(@Nonnull V vertex1, @Nonnull V vertex2, @Nonnull Graph<T, V, E> graph) {
        if (vertex1.equals(vertex2))
            return Collections.emptyList();

        Map<V, DijkstraNode<V>> nodeMap = new HashMap<>();
        Set<V> visited = new HashSet<>();

        DijkstraNode<V> currNode = new DijkstraNode<>(vertex1, 0D);
        nodeMap.put(vertex1, currNode);

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
                        neighbor.hint(currNode, edge); //Mark previous for backtracking
                        pq.remove(neighbor);  //Required to ensure the pq updates the order correctly
                        pq.add(neighbor);
                    }
                }
            }

            if (pq.isEmpty())
                currNode = null;
            else
                currNode = pq.poll();

            if (currNode == null || currNode.vertex.equals(vertex2))
                break;
        }

        if (currNode != null && currNode.vertex.equals(vertex2)) { //Successful! Reconstruct the path.
            LinkedList<E> path = new LinkedList<>();
            DijkstraNode<V> root = nodeMap.get(vertex1);
            while (!currNode.hint.equals(root)) {
                path.addFirst((E) currNode.getHintEdge());
                currNode = currNode.getHint();
            }
            return path;
        } else { //Unsuccessful :(
            return Collections.emptyList();
        }
    }
}
