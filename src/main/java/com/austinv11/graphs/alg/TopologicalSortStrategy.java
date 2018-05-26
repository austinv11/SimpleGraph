package com.austinv11.graphs.alg;

import com.austinv11.graphs.Edge;
import com.austinv11.graphs.Graph;
import com.austinv11.graphs.SortStrategy;
import com.austinv11.graphs.Vertex;
import com.austinv11.graphs.util.CycleException;
import com.austinv11.graphs.util.InvalidGraphConfigurationException;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Implements a topological sort (see: https://en.wikipedia.org/wiki/Topological_sorting). This is useful
 * for dealing with dependency graphs. But it should be noted that this expects a Directed Acyclic Graph
 * (DAG). Use {@link com.austinv11.graphs.impl.DirectedAcyclicGraph} to ensure your graph is compatible
 * with this sort strategy.
 */
public class TopologicalSortStrategy<T, V extends Vertex<T>, E extends Edge<T, V>> implements SortStrategy<T, V, E, Graph<T, V, E>> {

    @Override
    @Nonnull
    public List<V> sort(@Nonnull Graph<T, V, E> graph) {
        Map<V, VertexWrapper> wrapperMap = new HashMap<>();
        for (E e : graph.edges()) {
            if (!e.isDirected()) {
                throw new InvalidGraphConfigurationException("Undirected edge detected!");
            }

            VertexWrapper fromWrapper = wrapperMap.computeIfAbsent(e.getFirstVertex(), VertexWrapper::new);
            VertexWrapper toWrapper = wrapperMap.computeIfAbsent(e.getSecondVertex(), VertexWrapper::new);
            fromWrapper.outEdges.add(e);
            toWrapper.inEdges.add(e);
        }
        List<V> eliminatedTracker = new LinkedList<>();
        Queue<VertexWrapper> noIncomingEdges = new LinkedList<>();
        Set<VertexWrapper> vertexPool = new HashSet<>(wrapperMap.values());
        Set<E> eliminatedEdges = new HashSet<>();
        for (VertexWrapper wrapper : vertexPool) {
            if (wrapper.inEdges.isEmpty()) {
                noIncomingEdges.add(wrapper);
                vertexPool.remove(wrapper);
            }
        }

        if (noIncomingEdges.isEmpty()) {
            throw new CycleException();
        }

        while (!noIncomingEdges.isEmpty()) {
            VertexWrapper curr = noIncomingEdges.remove();
            eliminatedTracker.add(curr.vertex);
            for (E out : curr.outEdges) {
                eliminatedEdges.add(out);
                VertexWrapper toWrapper = wrapperMap.get(out.getSecondVertex());
                if (eliminatedEdges.containsAll(toWrapper.inEdges)) {
                    noIncomingEdges.add(toWrapper);
                    vertexPool.remove(toWrapper);
                }
            }
        }

        if (!vertexPool.isEmpty()) {
            throw new InvalidGraphConfigurationException("Could not sort all vertices!");
        }

        return eliminatedTracker;
    }

    private class VertexWrapper {

        private final V vertex;

        private final List<E> inEdges;
        private final List<E> outEdges;

        private VertexWrapper(V vertex) {
            this.vertex = vertex;
            this.inEdges = new LinkedList<>();
            this.outEdges = new LinkedList<>();
        }
    }
}
