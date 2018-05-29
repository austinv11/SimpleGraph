package com.austinv11.graphs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This represents a graph data structure.
 *
 * @param <T> The object type held in vertices.
 * @param <V> The vertex type.
 * @param <E> The edge type.
 */
public interface Graph<T, V extends Vertex<T>, E extends Edge<T, V>> extends Iterable<T> {

    /**
     * Gets the default traversal strategy of this graph.
     *
     * @return The default traversal strategy.
     */
    @Nonnull
    TraversalStrategy<T, V, E, Graph<T, V, E>> defaultTraversalStrategy();

    /**
     * Gets the default sort strategy of this graph.
     *
     * @return The default sort strategy.
     */
    @Nonnull
    SortStrategy<T, V, E, Graph<T, V, E>> defaultSortStrategy();

    /**
     * Gets the default path finding strategy of this graph.
     *
     * @return The default path finding strategy.
     */
    @Nonnull
    PathfindStrategy<T, V, E, Graph<T, V, E>> defaultPathfindStrategy();

    /**
     * Gets all the vertices in the graph.
     *
     * @return The collection of vertices in the graph.
     */
    @Nonnull
    default Collection<V> vertices() {
        return vertices(defaultTraversalStrategy());
    }

    /**
     * Collects vertices in the graph using the specified {@link com.austinv11.graphs.TraversalStrategy}.
     *
     * @param strategy The traversal to use.
     * @return The collection of vertices in the graph.
     */
    @Nonnull
    Collection<V> vertices(@Nonnull TraversalStrategy<T, V, E, Graph<T, V, E>> strategy);

    /**
     * Gets all the edges in the graph.
     *
     * @return The collection of edges in the graph.
     */
    @Nonnull
    default Collection<E> edges() {
        return edges(defaultTraversalStrategy());
    }

    /**
     * Collects edges in the graph using the specified {@link com.austinv11.graphs.TraversalStrategy}.
     *
     * @param strategy The traversal to use.
     * @return The collection of edges in the graph.
     */
    @Nonnull
    Collection<E> edges(@Nonnull TraversalStrategy<T, V, E, Graph<T, V, E>> strategy);

    /**
     * Gets all the values in the graph.
     *
     * @return The collection of values in the graph.
     */
    @Nonnull
    default Collection<T> values() {
        return values(defaultTraversalStrategy());
    }

    /**
     * Collects values in the graph using the specified {@link com.austinv11.graphs.TraversalStrategy}.
     *
     * @param strategy The traversal to use.
     * @return The collection of values in the graph.
     */
    @Nonnull
    Collection<T> values(@Nonnull TraversalStrategy<T, V, E, Graph<T, V, E>> strategy);

    /**
     * Attempts to find the vertex holding the specified object.
     *
     * @param obj The object which a vertex is holding.
     * @return The vertex, or null if not found.
     */
    @Nullable
    V findVertex(@Nullable T obj);

    /**
     * Checks if two vertices are connected via a single edge.
     *
     * @param vert1 The first vertex.
     * @param vert2 The second vertex.
     * @return True if directly connected, false if otherwise.
     */
    boolean areConnected(@Nonnull V vert1, @Nonnull V vert2);

    /**
     * Checks if two values are connected via a single edge.
     *
     * @param obj1 The first value.
     * @param obj2 The second value.
     * @return True if directly connected, false if otherwise.
     */
    boolean areConnected(@Nullable T obj1, @Nullable T obj2);

    /**
     * Gets all the edges directly connecting two vertices.
     *
     * @param vert1 The first vertex.
     * @param vert2 The second vertex.
     * @return The collection of edges connecting the two vertices.
     */
    @Nonnull
    Collection<E> getConnections(@Nonnull V vert1, @Nonnull V vert2);

    /**
     * Gets all the edges directly connecting two values.
     *
     * @param obj1 The first value.
     * @param obj2 The second value.
     * @return The collection of edges connecting the two values.
     */
    @Nonnull
    Collection<E> getConnections(@Nonnull T obj1, @Nonnull T obj2);

    /**
     * Sorts the vertices on the graph according to a specified strategy.
     *
     * @param strategy The sorting strategy to use.
     * @return The sorted list of vertices according to the given strategy.
     */
    @Nonnull
    List<V> sortVertices(@Nonnull SortStrategy<T, V, E, Graph<T, V, E>> strategy);

    /**
     * Sorts the vertices on the graph according to the default strategy.
     *
     * @return The sorted list of vertices according to the default strategy.
     */
    @Nonnull
    default List<V> sortVertices() {
        return sortVertices(defaultSortStrategy());
    }

    /**
     * Generates a path (or empty list if not possible) between two vertices.
     *
     * @param vert1 The first vertex (starting point).
     * @param vert2 The second vertex (destination).
     * @param strategy The strategy to pathfind with.
     * @return The list of edges defining a path (or empty if not possible).
     */
    @Nonnull
    List<E> pathfind(@Nonnull V vert1, @Nonnull V vert2, @Nonnull PathfindStrategy<T, V, E, Graph<T, V, E>> strategy);

    /**
     * Generates a path (or empty list if not possible) between two values.
     *
     * @param obj1 The first value (starting point).
     * @param obj2 The second value (destination).
     * @param strategy The strategy to pathfind with.
     * @return The list of edges defining a path (or empty if not possible).
     */
    @Nonnull
    List<E> pathfind(@Nullable T obj1, @Nullable T obj2, @Nonnull PathfindStrategy<T, V, E, Graph<T, V, E>> strategy);

    /**
     * Generates a path (or empty list if not possible) between two vertices using the default
     * {@link com.austinv11.graphs.PathfindStrategy}.
     *
     * @param vert1 The first vertex (starting point).
     * @param vert2 The second vertex (destination).
     * @return The list of edges defining a path (or empty if not possible).
     */
    @Nonnull
    default List<E> pathfind(@Nonnull V vert1, @Nonnull V vert2) {
        return pathfind(vert1, vert2, defaultPathfindStrategy());
    }

    /**
     * Generates a path (or empty list if not possible) between two values using the default
     * {@link com.austinv11.graphs.PathfindStrategy}.
     *
     * @param obj1 The first value (starting point).
     * @param obj2 The second value (destination).
     * @return The list of edges defining a path (or empty if not possible).
     */
    @Nonnull
    List<E> pathfind(@Nullable T obj1, @Nullable T obj2);

    /**
     * Gets all the edges connecting to/from the specified vertex.
     *
     * @param vertex The vertex.
     * @return The edges connected to/from it.
     */
    @Nonnull
    Collection<E> getConnectedEdges(@Nonnull V vertex);

    /**
     * Gets all the edges going from this vertex to another (this includes undirected edges as they are
     * considered bidirectional).
     *
     * @param vertex The vertex.
     * @return The edges connected from it.
     */
    @Nonnull
    Collection<E> getOutwardEdges(@Nonnull V vertex);

    /**
     * Gets all the edges going to this vertex from another (this includes undirected edges as they are
     * considered bidirectional).
     *
     * @param vertex The vertex.
     * @return The edges connected to it.
     */
    @Nonnull
    Collection<E> getInwardEdges(@Nonnull V vertex);

    /**
     * Adds a vertex to the graph.
     *
     * @param vertex The vertex to add.
     */
    void addVertex(@Nonnull V vertex);

    /**
     * Removes a vertex from the graph.
     *
     * @param vertex The vertex to remove.
     */
    void removeVertex(@Nonnull V vertex);

    /**
     * Adds an edge to the graph.
     *
     * @param edge The edge to add.
     */
    void addEdge(@Nonnull E edge);

    /**
     * Removes an edge from the graph.
     *
     * @param edge The edge to remove.
     */
    void removeEdge(@Nonnull E edge);

    /**
     * Gets the number of vertices present in the graph.
     *
     * @return The number of vertices present.
     */
    int getVertexCount();

    /**
     * Gets the number of edges present in the graph.
     *
     * @return The number of edges present.
     */
    int getEdgeCount();

    /**
     * Clears all the edges and vertices from the graph.
     */
    void clear();

    /**
     * Copies the contents of this graph to a new instance.
     *
     * @return A copy of this graph instance.
     */
    @Nonnull
    Graph<T, V, E> copy();

    /**
     * Gets an iterator of the values in the graph using the default traversal strategy.
     *
     * @return The iterator of graph values.
     */
    @Override
    @Nonnull
    default Iterator<T> iterator() {
        return values().iterator();
    }
}
