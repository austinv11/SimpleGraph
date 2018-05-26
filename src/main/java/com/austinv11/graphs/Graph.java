package com.austinv11.graphs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface Graph<T, V extends Vertex<T>, E extends Edge<T, V>> extends Iterable<T> {

    @Nonnull
    TraversalStrategy<T, V, E, Graph<T, V, E>> defaultTraversalStrategy();

    @Nonnull
    SortStrategy<T, V, E, Graph<T, V, E>> defaultSortStrategy();

    @Nonnull
    PathfindStrategy<T, V, E, Graph<T, V, E>> defaultPathfindStrategy();

    @Nonnull
    default Collection<V> vertices() {
        return vertices(defaultTraversalStrategy());
    }

    @Nonnull
    Collection<V> vertices(@Nonnull TraversalStrategy<T, V, E, Graph<T, V, E>> strategy);

    @Nonnull
    default Collection<E> edges() {
        return edges(defaultTraversalStrategy());
    }

    @Nonnull
    Collection<E> edges(@Nonnull TraversalStrategy<T, V, E, Graph<T, V, E>> strategy);

    @Nonnull
    default Collection<T> values() {
        return values(defaultTraversalStrategy());
    }

    @Nonnull
    Collection<T> values(@Nonnull TraversalStrategy<T, V, E, Graph<T, V, E>> strategy);

    @Nullable
    V findVertex(@Nullable T obj);

    boolean areConnected(@Nonnull V vert1, @Nonnull V vert2);

    boolean areConnected(@Nullable T obj1, @Nullable T obj2);

    @Nonnull
    Collection<E> getConnections(@Nonnull V vert1, @Nonnull V vert2);

    @Nonnull
    Collection<E> getConnections(@Nonnull T obj1, @Nonnull T obj2);

    @Nonnull
    List<V> sortVertices(@Nonnull SortStrategy<T, V, E, Graph<T, V, E>> strategy);

    @Nonnull
    default List<V> sortVertices() {
        return sortVertices(defaultSortStrategy());
    }

    @Nonnull
    List<E> pathfind(@Nonnull V vert1, @Nonnull V vert2, @Nonnull PathfindStrategy<T, V, E, Graph<T, V, E>> strategy);

    @Nonnull
    List<E> pathfind(@Nullable T obj1, @Nullable T obj2, @Nonnull PathfindStrategy<T, V, E, Graph<T, V, E>> strategy);

    @Nonnull
    default List<E> pathfind(@Nonnull V vert1, @Nonnull V vert2) {
        return pathfind(vert1, vert2, defaultPathfindStrategy());
    }

    @Nonnull
    List<E> pathfind(@Nullable T obj1, @Nullable T obj2);

    @Nonnull
    Collection<E> getConnectedEdges(@Nonnull V vertex);

    @Nonnull
    Collection<E> getOutwardEdges(@Nonnull V vertex);

    @Nonnull
    Collection<E> getInwardEdges(@Nonnull V vertex);

    void addVertex(@Nonnull V vertex);

    void removeVertex(@Nonnull V vertex);

    void addEdge(@Nonnull E edge);

    void removeEdge(@Nonnull E edge);

    int getVertexCount();

    int getEdgeCount();

    void clear();

    @Nonnull
    Graph<T, V, E> copy();

    @Override
    @Nonnull
    default Iterator<T> iterator() {
        return values().iterator();
    }
}
