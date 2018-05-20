package com.austinv11.graphs.impl;

import com.austinv11.graphs.*;
import com.austinv11.graphs.alg.ColoringCycleDetectionStrategy;
import com.austinv11.graphs.util.CycleException;
import com.austinv11.graphs.util.InvalidGraphConfigurationException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This is a {@link com.austinv11.graphs.Graph} wrapper which enforces directed, acyclic properties on a
 * backing graph. This is enforced via a {@link com.austinv11.graphs.CycleDetectionStrategy} which, by default,
 * is the {@link com.austinv11.graphs.alg.ColoringCycleDetectionStrategy} implementation. This is useful to ensure
 * that certain algorithms work correctly like {@link com.austinv11.graphs.alg.TopologicalSortStrategy}.
 */
public class DirectedAcyclicGraph<T, V extends Vertex<T>, E extends Edge<T,V>> implements Graph<T, V, E> {

    private final Graph<T, V, E> backing;
    private final CycleDetectionStrategy<T, V, E, Graph<T, V, E>> strategy;

    public DirectedAcyclicGraph(Graph<T, V, E> backing) {
        this(backing, new ColoringCycleDetectionStrategy<>());
    }

    public DirectedAcyclicGraph(Graph<T, V, E> backing,
                                CycleDetectionStrategy<T, V, E, Graph<T, V, E>> strategy) {
        this.backing = backing;
        this.strategy = strategy;
    }

    @Override
    @Nonnull
    public TraversalStrategy<T, V, E, Graph<T, V, E>> defaultTraversalStrategy() {
        return backing.defaultTraversalStrategy();
    }

    @Override
    @Nonnull
    public SortStrategy<T, V, E, Graph<T, V, E>> defaultSortStrategy() {
        return backing.defaultSortStrategy();
    }

    @Override
    @Nonnull
    public PathfindStrategy<T, V, E, Graph<T, V, E>> defaultPathfindStrategy() {
        return backing.defaultPathfindStrategy();
    }

    @Override
    @Nonnull
    public Collection<V> vertices() {
        return backing.vertices();
    }

    @Override
    @Nonnull
    public Collection<V> vertices(@Nonnull TraversalStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return backing.vertices(strategy);
    }

    @Override
    @Nonnull
    public Collection<E> edges() {
        return backing.edges();
    }

    @Override
    @Nonnull
    public Collection<E> edges(@Nonnull TraversalStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return backing.edges(strategy);
    }

    @Override
    @Nonnull
    public Collection<T> values() {
        return backing.values();
    }

    @Override
    @Nonnull
    public Collection<T> values(@Nonnull TraversalStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return backing.values(strategy);
    }

    @Override
    @Nullable
    public V findVertex(@Nullable T obj) {
        return backing.findVertex(obj);
    }

    @Override
    public boolean areConnected(@Nonnull V vert1, @Nonnull V vert2) {
        return backing.areConnected(vert1, vert2);
    }

    @Override
    public boolean areConnected(@Nullable T obj1, @Nullable T obj2) {
        return backing.areConnected(obj1, obj2);
    }

    @Override
    @Nonnull
    public Collection<E> getConnections(@Nonnull V vert1, @Nonnull V vert2) {
        return backing.getConnections(vert1, vert2);
    }

    @Override
    @Nonnull
    public Collection<E> getConnections(@Nonnull T obj1, @Nonnull T obj2) {
        return backing.getConnections(obj1, obj2);
    }

    @Override
    @Nonnull
    public List<V> sortVertices(@Nonnull SortStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return backing.sortVertices(strategy);
    }

    @Override
    @Nonnull
    public List<V> sortVertices() {
        return backing.sortVertices();
    }

    @Override
    @Nonnull
    public List<E> pathfind(@Nonnull V vert1, @Nonnull V vert2, @Nonnull PathfindStrategy<T, V, E, Graph<T, V, E>>
            strategy) {
        return backing.pathfind(vert1, vert2, strategy);
    }

    @Override
    @Nonnull
    public List<E> pathfind(@Nullable T obj1, @Nullable T obj2, @Nonnull PathfindStrategy<T, V, E, Graph<T, V, E>>
            strategy) {
        return backing.pathfind(obj1, obj2, strategy);
    }

    @Override
    @Nonnull
    public List<E> pathfind(@Nonnull V vert1, @Nonnull V vert2) {
        return backing.pathfind(vert1, vert2);
    }

    @Override
    @Nonnull
    public List<E> pathfind(@Nullable T obj1, @Nullable T obj2) {
        return backing.pathfind(obj1, obj2);
    }

    @Override
    public void addVertex(@Nonnull V vertex) {
        backing.addVertex(vertex);
    }

    @Override
    public void removeVertex(@Nonnull V vertex) {
        backing.removeVertex(vertex);
    }

    @Override
    public void addEdge(@Nonnull E edge) {
        if (!edge.isDirected())
            throw new InvalidGraphConfigurationException("Edges must be directed!");

        backing.addEdge(edge);
        if (strategy.findCycle(backing)) {
            backing.removeEdge(edge);
            throw new CycleException();
        }
    }

    @Override
    public void removeEdge(@Nonnull E edge) {
        backing.removeEdge(edge);
    }

    @Override
    public int getVertexCount() {
        return backing.getVertexCount();
    }

    @Override
    public int getEdgeCount() {
        return backing.getEdgeCount();
    }

    @Override
    public void clear() {
        backing.clear();
    }

    @Override
    @Nonnull
    public Iterator<T> iterator() {
        return backing.iterator();
    }

    @Override
    @Nonnull
    public Collection<E> getConnectedEdges(@Nonnull V vertex) {
        return backing.getConnectedEdges(vertex);
    }

    @Override
    @Nonnull
    public Collection<E> getOutwardEdges(@Nonnull V vertex) {
        return backing.getOutwardEdges(vertex);
    }

    @Override
    @Nonnull
    public Collection<E> getInwardEdges(@Nonnull V vertex) {
        return backing.getInwardEdges(vertex);
    }
}
