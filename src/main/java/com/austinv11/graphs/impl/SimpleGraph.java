package com.austinv11.graphs.impl;

import com.austinv11.graphs.*;
import com.austinv11.graphs.util.InvalidGraphConfigurationException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

    public SimpleGraph(boolean concurrent) {
        this(new DefaultTraversalStrategy<>(),
                new DefaultSortStrategy<>(),
                new DefaultPathfindStrategy<>(),
                concurrent);
    }

    public SimpleGraph(@Nonnull TraversalStrategy<T, V, E, Graph<T, V, E>> defaultTraversal,
                       @Nonnull SortStrategy<T, V, E, Graph<T, V, E>> defaultSort,
                       @Nonnull PathfindStrategy<T, V, E, Graph<T, V, E>> defaultPathfind,
                       boolean concurrent) {
        this.defaultTraversal = defaultTraversal;
        this.defaultSort = defaultSort;
        this.defaultPathfind = defaultPathfind;

        matrix = new AdjacencyMatrix(concurrent);
    }

    @Override
    @Nonnull
    public TraversalStrategy<T, V, E, Graph<T, V, E>> defaultTraversalStrategy() {
        return defaultTraversal;
    }

    @Override
    @Nonnull
    public SortStrategy<T, V, E, Graph<T, V, E>> defaultSortStrategy() {
        return defaultSort;
    }

    @Override
    @Nonnull
    public PathfindStrategy<T, V, E, Graph<T, V, E>> defaultPathfindStrategy() {
        return defaultPathfind;
    }

    @Override
    @Nonnull
    public Collection<V> vertices() {
        return vertices(defaultTraversal);
    }

    @Override
    @Nonnull
    public Collection<V> vertices(@Nonnull TraversalStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return strategy.traverseVertices(this);
    }

    @Override
    @Nonnull
    public Collection<E> edges() {
        return edges(defaultTraversal);
    }

    @Override
    @Nonnull
    public Collection<E> edges(@Nonnull TraversalStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return strategy.traverseEdges(this);
    }

    @Override
    @Nonnull
    public Collection<T> values() {
        return values(defaultTraversal);
    }

    @Override
    @Nonnull
    public Collection<T> values(@Nonnull TraversalStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return strategy.traverseValues(this);
    }

    @Override
    @Nullable
    public V findVertex(@Nullable T obj) {
        return null;
    }

    @Override
    public boolean areConnected(@Nonnull V vert1, @Nonnull V vert2) {
        return false;
    }

    @Override
    public boolean areConnected(@Nullable T obj1, @Nullable T obj2) {
        return false;
    }

    @Override
    @Nonnull
    public Collection<E> getConnections(@Nonnull V vert1, @Nonnull V vert2) {
        return null;
    }

    @Override
    @Nonnull
    public Collection<E> getConnections(@Nullable T obj1, @Nullable T obj2) {
        return null;
    }

    @Override
    @Nonnull
    public List<V> sortVertices(@Nonnull SortStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return null;
    }

    @Override
    @Nonnull
    public List<V> sortVertices() {
        return null;
    }

    @Override
    @Nonnull
    public List<E> pathfind(@Nonnull V vert1, @Nonnull V vert2, @Nonnull PathfindStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return null;
    }

    @Override
    @Nonnull
    public List<E> pathfind(@Nullable T obj1, @Nullable T obj2, @Nonnull PathfindStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return null;
    }

    @Override
    @Nonnull
    public List<E> pathfind(@Nonnull V vert1, @Nonnull V vert2) {
        return null;
    }

    @Override
    @Nonnull
    public List<E> pathfind(@Nullable T obj1, @Nullable T obj2) {
        return null;
    }

    @Override
    public void addVertex(@Nonnull V vertex) {

    }

    @Override
    public void removeVertex(@Nonnull V vertex) {

    }

    @Override
    public void addEdge(@Nonnull E edge) {

    }

    @Override
    public void removeEdge(@Nonnull E edge) {

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
    public void clear() {
        matrix.clear();
    }

    @Override
    @Nonnull
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public void forEach(@Nonnull Consumer<? super T> action) {

    }

    @Override
    @Nonnull
    public Spliterator<T> spliterator() {
        return null;
    }

    /**
     * A fairly simple adjacency matrix, however it opts for quicker operation times as opposed to memory efficiency.
     */
    private final class AdjacencyMatrix {

        private final ReadWriteLock lock;
        private final Map<V, Set<E>> matrix;
        private final Map<V, T> vertexExchange;
        private final Supplier<Set<E>> setSupplier;

        AdjacencyMatrix(boolean concurrent) {
            if (concurrent) {
                lock = new ReentrantReadWriteLock();
                matrix = new ConcurrentHashMap<>();
                vertexExchange = new ConcurrentHashMap<>();
                setSupplier = ConcurrentHashMap::newKeySet;
            } else {
                lock = null;
                matrix = new HashMap<>();
                vertexExchange = new HashMap<>();
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

        @Nonnull
        Collection<E> edges(@Nonnull V v1, @Nonnull V v2) {
            readLock();
            if (!matrix.containsKey(v1) || !matrix.containsKey(v2))
                return Collections.emptyList();
            Collection<E> c = matrix.get(v1).stream().filter(e -> e.contains(v2)).collect(Collectors.toList());
            readUnlock();
            return c;
        }

        @Nonnull
        Collection<E> edges() {
            readLock();
            Collection<E> c = matrix.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
            readUnlock();
            return c;
        }

        @Nonnull
        Collection<V> vertices() {
            readLock();
            Collection<V> c = matrix.keySet();
            readUnlock();
            return c;
        }

        @Nonnull
        Collection<T> values() {
            readLock();
            Collection<T> c = matrix.keySet().stream().map(Vertex::get).collect(Collectors.toSet());
            readUnlock();
            return c;
        }

        void add(@Nonnull V v) {
            writeLock();
            matrix.putIfAbsent(v, setSupplier.get());
            vertexExchange.put(v, v.get());
            writeUnlock();
        }

        void add(@Nonnull E e) {
            writeLock();
            matrix.putIfAbsent(e.getFirstVertex(), setSupplier.get());
            vertexExchange.putIfAbsent(e.getFirstVertex(), e.getFirstVertex().get());
            matrix.putIfAbsent(e.getSecondVertex(), setSupplier.get());
            vertexExchange.putIfAbsent(e.getSecondVertex(), e.getSecondVertex().get());
            matrix.get(e.getFirstVertex()).add(e);
            matrix.get(e.getSecondVertex()).add(e);
            writeUnlock();
        }

        void delete(@Nonnull V v) {
            writeLock();
            Collection<E> edges = matrix.remove(v);
            if (edges != null) {
                edges.forEach(e -> {
                    V other = e.getFirstVertex().equals(v) ? e.getSecondVertex() : e.getFirstVertex();
                    matrix.get(other).removeIf(edge -> edge.contains(other));
                });
            }
            vertexExchange.remove(v);
            writeUnlock();
        }

        void delete(@Nonnull E e) {
            writeLock();
            matrix.get(e.getFirstVertex()).remove(e);
            matrix.get(e.getSecondVertex()).remove(e);
            writeUnlock();
        }

        void clear() {
            writeLock();
            matrix.clear();
            vertexExchange.clear();
            writeUnlock();
        }
    }

    // Internal strategies optimized for this SimpleGraph implementation

    private static void assertSimpleGraph(Graph g) {
        if (!(g instanceof SimpleGraph)) {
            throw new InvalidGraphConfigurationException();
        }
    }

    private static final class DefaultPathfindStrategy<T, V extends Vertex<T>, E extends Edge<T, V>> implements PathfindStrategy<T, V, E, Graph<T, V, E>> {

    }

    private static final class DefaultSortStrategy<T, V extends Vertex<T>, E extends Edge<T, V>> implements SortStrategy<T, V, E, Graph<T, V, E>> {

    }

    private static final class DefaultTraversalStrategy<T, V extends Vertex<T>, E extends Edge<T, V>> implements TraversalStrategy<T, V, E, Graph<T, V, E>> {

        @Override
        @Nonnull
        public Collection<V> traverseVertices(@Nonnull Graph<T, V, E> graph) {
            assertSimpleGraph(graph);
            SimpleGraph<T, V, E> g = (SimpleGraph<T, V, E>) graph;
            return g.matrix.vertices();
        }

        @Override
        @Nonnull
        public Collection<E> traverseEdges(@Nonnull Graph<T, V, E> graph) {
            assertSimpleGraph(graph);
            SimpleGraph<T, V, E> g = (SimpleGraph<T, V, E>) graph;
            return g.matrix.edges();
        }

        @Override
        @Nonnull
        public Collection<T> traverseValues(@Nonnull Graph<T, V, E> graph) {
            return traverseVertices(graph).stream().map(Vertex::get).collect(Collectors.toSet());
        }
    }
}
