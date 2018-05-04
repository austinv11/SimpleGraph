package com.austinv11.graphs.impl;

import com.austinv11.graphs.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SimpleGraph<T, V extends Vertex<T>, E extends Edge<T,V>> implements Graph<T, V, E> {

    private final TraversalStrategy<T, V, E, Graph<T, V, E>> defaultTraversal;
    private final SortStrategy<T, V, E, Graph<T, V, E>> defaultSort;
    private final PathfindStrategy<T, V, E, Graph<T, V, E>> defaultPathfind;

    private final AdjacencyMatrix matrix;

    public SimpleGraph(TraversalStrategy<T, V, E, Graph<T, V, E>> defaultTraversal,
                       SortStrategy<T, V, E, Graph<T, V, E>> defaultSort,
                       PathfindStrategy<T, V, E, Graph<T, V, E>> defaultPathfind,
                       boolean concurrent) {
        this.defaultTraversal = defaultTraversal;
        this.defaultSort = defaultSort;
        this.defaultPathfind = defaultPathfind;

        matrix = new AdjacencyMatrix(concurrent);
    }

    @Override
    public TraversalStrategy<T, V, E, Graph<T, V, E>> defaultTraversalStrategy() {
        return defaultTraversal;
    }

    @Override
    public SortStrategy<T, V, E, Graph<T, V, E>> defaultSortStrategy() {
        return defaultSort;
    }

    @Override
    public PathfindStrategy<T, V, E, Graph<T, V, E>> defaultPathfindStrategy() {
        return defaultPathfind;
    }

    @Override
    public Collection<V> vertices() {
        return matrix.vertices();
    }

    @Override
    public Collection<V> vertices(TraversalStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return null;
    }

    @Override
    public Collection<E> edges() {
        return null;
    }

    @Override
    public Collection<E> edges(TraversalStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return null;
    }

    @Override
    public Collection<T> values() {
        return null;
    }

    @Override
    public Collection<T> values(TraversalStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return null;
    }

    @Override
    public V findVertex(T obj) {
        return null;
    }

    @Override
    public boolean areConnected(V vert1, V vert2) {
        return false;
    }

    @Override
    public boolean areConnected(T obj1, T obj2) {
        return false;
    }

    @Override
    public Collection<E> getConnections(V vert1, V vert2) {
        return null;
    }

    @Override
    public Collection<E> getConnections(T obj1, T obj2) {
        return null;
    }

    @Override
    public List<V> sortVertices(SortStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return null;
    }

    @Override
    public List<V> sortVertices() {
        return null;
    }

    @Override
    public List<E> pathfind(V vert1, V vert2, PathfindStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return null;
    }

    @Override
    public List<E> pathfind(T obj1, T obj2, PathfindStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return null;
    }

    @Override
    public List<E> pathfind(V vert1, V vert2) {
        return null;
    }

    @Override
    public List<E> pathfind(T obj1, T obj2) {
        return null;
    }

    @Override
    public void addVertex(V vertex) {

    }

    @Override
    public void removeVertex(V vertex) {

    }

    @Override
    public void addEdge(E edge) {

    }

    @Override
    public void removeEdge(E edge) {

    }

    @Override
    public int getVertexCount() {
        return 0;
    }

    @Override
    public int getEdgeCount() {
        return 0;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super T> action) {

    }

    @Override
    public Spliterator<T> spliterator() {
        return null;
    }

    /**
     * A fairly simple adjacency matrix, however it opts for quicker operation times as opposed to memory efficiency.
     */
    private final class AdjacencyMatrix {

        private final ReadWriteLock lock;
        private final Map<V, Set<E>> matrix;
        private final Supplier<Set<E>> setSupplier;

        AdjacencyMatrix(boolean concurrent) {
            if (concurrent) {
                lock = new ReentrantReadWriteLock();
                matrix = new ConcurrentHashMap<>();
                setSupplier = ConcurrentHashMap::newKeySet;
            } else {
                lock = null;
                matrix = new HashMap<>();
                setSupplier = HashSet::new;
            }
        }

        private void writeLock() {
            if (lock != null)
                lock.writeLock().lock();
        }

        private void writeUnlock() {
            if (lock != null)
                lock.writeLock().unlock();
        }

        private void readLock() {
            if (lock != null)
                lock.readLock().lock();
        }

        private void readUnlock() {
            if (lock != null)
                lock.readLock().unlock();
        }

        Collection<E> edges(V v1, V v2) {
            readLock();
            if (!matrix.containsKey(v1) || !matrix.containsKey(v2))
                return Collections.emptyList();
            Collection<E> c = matrix.get(v1).stream().filter(e -> e.contains(v2)).collect(Collectors.toList());
            readUnlock();
            return c;
        }

        Collection<E> edges() {
            readLock();
            Collection<E> c = matrix.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
            readUnlock();
            return c;
        }

        Collection<V> vertices() {
            readLock();
            Collection<V> c = matrix.keySet();
            readUnlock();
            return c;
        }

        Collection<T> values() {
            readLock();
            Collection<T> c = matrix.keySet().stream().map(Vertex::get).collect(Collectors.toSet());
            readUnlock();
            return c;
        }

        void add(V v) {
            writeLock();
            matrix.putIfAbsent(v, setSupplier.get());
            writeUnlock();
        }

        void add(E e) {
            writeLock();
            matrix.putIfAbsent(e.getFirstVertex(), setSupplier.get());
            matrix.putIfAbsent(e.getSecondVertex(), setSupplier.get());
            matrix.get(e.getFirstVertex()).add(e);
            matrix.get(e.getSecondVertex()).add(e);
            writeUnlock();
        }

        void delete(V v) {
            writeLock();
            Collection<E> edges = matrix.remove(v);
            if (edges != null) {
                edges.forEach(e -> {
                    V other = e.getFirstVertex().equals(v) ? e.getSecondVertex() : e.getFirstVertex();
                    matrix.get(other).removeIf(edge -> edge.contains(other));
                });
            }
            writeUnlock();
        }

        void delete(E e) {
            writeLock();
            matrix.get(e.getFirstVertex()).remove(e);
            matrix.get(e.getSecondVertex()).remove(e);
            writeUnlock();
        }
    }
}
