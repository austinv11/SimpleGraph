package com.austinv11.graphs;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface Graph<T, V extends Vertex<T>, E extends Edge<T, V>> extends Iterable<T> {

    TraversalStrategy<T, V, E, Graph<T, V, E>> defaultTraversalStrategy();

    SortStrategy<T, V, E, Graph<T, V, E>> defaultSortStrategy();

    PathfindStrategy<T, V, E, Graph<T, V, E>> defaultPathfindStrategy();

    default Collection<V> vertices() {
        return vertices(defaultTraversalStrategy());
    }

    Collection<V> vertices(TraversalStrategy<T, V, E, Graph<T, V, E>> strategy);

    default Collection<E> edges() {
        return edges(defaultTraversalStrategy());
    }

    Collection<E> edges(TraversalStrategy<T, V, E, Graph<T, V, E>> strategy);

    default Collection<T> values() {
        return values(defaultTraversalStrategy());
    }

    Collection<T> values(TraversalStrategy<T, V, E, Graph<T, V, E>> strategy);

    V findVertex(T obj);

    boolean areConnected(V vert1, V vert2);

    default boolean areConnected(T obj1, T obj2) {
        return areConnected(findVertex(obj1), findVertex(obj2));
    }

    Collection<E> getConnections(V vert1, V vert2);

    default Collection<E> getConnections(T obj1, T obj2) {
        return getConnections(findVertex(obj1), findVertex(obj2));
    }

    List<V> sortVertices(SortStrategy<T, V, E, Graph<T, V, E>> strategy);

    default List<V> sortVertices() {
        return sortVertices(defaultSortStrategy());
    }

    List<E> pathfind(V vert1, V vert2, PathfindStrategy<T, V, E, Graph<T, V, E>> strategy);

    default List<E> pathfind(T obj1, T obj2, PathfindStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return pathfind(findVertex(obj1), findVertex(obj2), strategy);
    }

    default List<E> pathfind(V vert1, V vert2) {
        return pathfind(vert1, vert2, defaultPathfindStrategy());
    }

    default List<E> pathfind(T obj1, T obj2) {
        return pathfind(findVertex(obj1), findVertex(obj2), defaultPathfindStrategy());
    }

    void addVertex(V vertex);

    void removeVertex(V vertex);

    void addEdge(E edge);

    void removeEdge(E edge);

    int getVertexCount();

    int getEdgeCount();

    @Override
    default Iterator<T> iterator() {
        return values().iterator();
    }
}
