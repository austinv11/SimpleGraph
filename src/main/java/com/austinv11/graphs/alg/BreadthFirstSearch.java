package com.austinv11.graphs.alg;

import com.austinv11.graphs.Edge;
import com.austinv11.graphs.Graph;
import com.austinv11.graphs.PathfindStrategy;
import com.austinv11.graphs.Vertex;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * This implements a naive breadth-first search strategy. This does not guarantee any properties regarding
 * the path found. This may have better performance than something like
 * {@link com.austinv11.graphs.alg.DijkstraPathfindStrategy} in some cases, however it is generally preferred
 * to use a more advanced strategy like the aforementioned {@link com.austinv11.graphs.alg.DijkstraPathfindStrategy}.
 */
public class BreadthFirstSearch<T, V extends Vertex<T>, E extends Edge<T, V>> implements PathfindStrategy<T, V, E, Graph<T, V, E>> {

    @Override
    @Nonnull
    public List<E> pathfind(@Nonnull V vertex1, @Nonnull V vertex2, @Nonnull Graph<T, V, E> graph) {
        return pathfind(vertex1, vertex2, graph, new HashSet<>(), null);
    }

    @Nonnull
    private List<E> pathfind(@Nonnull V vertex1, @Nonnull V vertex2, @Nonnull Graph<T, V, E> graph, Set<V> visited, List<Collection<V>> levelTracker) {
        if (levelTracker == null) { // Generate levels until we visit the desired vertex
            levelTracker = levelize(vertex1, vertex2, graph, new HashSet<>(), null, 0);
        }

        //Now backtrack, since we know that the end vertex is on the last level, we reconstruct a path from it to the root
        //Which also means we can skip the last level
        int currLevel = levelTracker.size() - 2;
        List<E> path = new ArrayList<>();
        V currDest = vertex2;
        while (currLevel >= 0) {
            for (V vert : levelTracker.get(currLevel)) {
                E edge = graph.getConnections(vert, currDest).stream().min(Comparator.comparingDouble(Edge::getWeight)).orElse(null);
                if (edge != null) {
                    path.add(currLevel, (E) edge.ensureOrder(vert));
                    currLevel--;
                    currDest = vert;
                    break;
                }
            }
        }
        return path;
    }

    private List<Collection<V>> levelize(@Nonnull V root, @Nonnull V lastVertex, @Nonnull Graph<T, V, E> graph, Set<V> visited, List<Collection<V>> levelTracker, int currLevel) {
        if (levelTracker == null) {
            levelTracker = new ArrayList<>();
            levelTracker.add(currLevel, new HashSet<>());
            levelTracker.get(currLevel).add(root);
            visited.add(root);
            currLevel++;
        }

        Collection<V> toSearch = levelTracker.get(currLevel-1);
        Set<V> vertices = new HashSet<>();
        boolean requestedVertexPresent = false;
        for (V v : toSearch) {
            for (E e : graph.getOutwardEdges(v)) {
                E ordered = (E) e.ensureOrder(v);
                if (!visited.contains(ordered.getSecondVertex())) {
                    if (ordered.contains(lastVertex))
                        requestedVertexPresent = true;
                    vertices.add(ordered.getSecondVertex());
                }
            }
        }

        visited.addAll(vertices);

        if (vertices.isEmpty())
            throw new RuntimeException("Unable to reach requested vertex!");

        levelTracker.add(currLevel, vertices);
        if (requestedVertexPresent) {
            return levelTracker;
        } else {
            return levelize(root, lastVertex, graph, visited, levelTracker, currLevel+1);
        }
    }
}
