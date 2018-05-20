package com.austinv11.graphs.alg;

import com.austinv11.graphs.CycleDetectionStrategy;
import com.austinv11.graphs.Edge;
import com.austinv11.graphs.Graph;
import com.austinv11.graphs.Vertex;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Detects cycles by performing a depth first search and looking for "back edges". See this link for a simple
 * explanation: https://www.geeksforgeeks.org/detect-cycle-direct-graph-using-colors/
 *
 * This algorithm is O(|E| + |V|)
 */
public class ColoringCycleDetectionStrategy<T, V extends Vertex<T>, E extends Edge<T, V>, G extends Graph<T, V, E>> implements CycleDetectionStrategy<T, V, E, G> {

    @Override
    public boolean findCycle(@Nonnull G graph) {
        Map<V, Color> colorMap = new HashMap<>();
        Collection<V> vertices = graph.vertices();
        for (V v : vertices) {
            if (recursiveSearch(graph, colorMap, v))
                return true;
        }
        return false;
    }

    boolean recursiveSearch(G graph, Map<V, Color> colorMap, V vertex) {
        Color c = colorMap.getOrDefault(vertex, Color.WHITE);

        if (c == Color.BLACK)
            return false;

        colorMap.put(vertex, Color.GREY);

        for (E e : graph.getOutwardEdges(vertex)) {
            V other = e.getOther(vertex);
            Color otherColor = colorMap.getOrDefault(other, Color.WHITE);
            if (otherColor == Color.GREY)
                return true;

            if (otherColor == Color.BLACK)
                continue;

            if (recursiveSearch(graph, colorMap, other))
                return true;
        }

        colorMap.put(vertex, Color.BLACK);

        return false;
    }

    enum Color {
        WHITE, GREY, BLACK
    }
}
