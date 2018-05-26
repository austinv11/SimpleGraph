package com.austinv11.graphs.impl;

import com.austinv11.graphs.*;
import com.austinv11.graphs.alg.AStarPathfindStrategy;
import com.austinv11.graphs.alg.NaturalSortStrategy;
import com.austinv11.graphs.util.InvalidGraphConfigurationException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
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
        return matrix.exchange(obj);
    }

    @Override
    public boolean areConnected(@Nonnull V vert1, @Nonnull V vert2) {
        return !matrix.edges(vert1, vert2).isEmpty();
    }

    @Override
    public boolean areConnected(@Nullable T obj1, @Nullable T obj2) {
        V vert1 = findVertex(obj1);
        V vert2 = findVertex(obj2);

        if (vert1 == null || vert2 == null)
            return false;

        return areConnected(vert1, vert2);
    }

    @Override
    @Nonnull
    public Collection<E> getConnections(@Nonnull V vert1, @Nonnull V vert2) {
        return matrix.edges(vert1, vert2);
    }

    @Override
    @Nonnull
    public Collection<E> getConnections(@Nullable T obj1, @Nullable T obj2) {
        V vert1 = findVertex(obj1);
        V vert2 = findVertex(obj2);

        if (vert1 == null || vert2 == null)
            return Collections.emptyList();

        return getConnections(vert1, vert2);
    }

    @Override
    @Nonnull
    public List<V> sortVertices(@Nonnull SortStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return strategy.sort(this);
    }

    @Override
    @Nonnull
    public List<V> sortVertices() {
        return sortVertices(defaultSort);
    }

    @Override
    @Nonnull
    public List<E> pathfind(@Nonnull V vert1, @Nonnull V vert2, @Nonnull PathfindStrategy<T, V, E, Graph<T, V, E>> strategy) {
        return strategy.pathfind(vert1, vert2, this);
    }

    @Override
    @Nonnull
    public List<E> pathfind(@Nullable T obj1, @Nullable T obj2, @Nonnull PathfindStrategy<T, V, E, Graph<T, V, E>> strategy) {
        V vert1 = findVertex(obj1);
        V vert2 = findVertex(obj2);

        if (vert1 == null || vert2 == null)
            return Collections.emptyList();

        return pathfind(vert1, vert2, strategy);
    }

    @Override
    @Nonnull
    public List<E> pathfind(@Nonnull V vert1, @Nonnull V vert2) {
        return pathfind(vert1, vert2, defaultPathfind);
    }

    @Override
    @Nonnull
    public List<E> pathfind(@Nullable T obj1, @Nullable T obj2) {
        return pathfind(obj1, obj2, defaultPathfind);
    }

    @Nonnull
    @Override
    public Collection<E> getConnectedEdges(@Nonnull V vertex) {
        return matrix.edges(vertex);
    }

    @Nonnull
    @Override
    public Collection<E> getOutwardEdges(@Nonnull V vertex) {
        return getConnectedEdges(vertex).stream().filter(e -> !e.isDirected() || e.getFirstVertex().equals(vertex)).collect(Collectors.toSet());
    }

    @Nonnull
    @Override
    public Collection<E> getInwardEdges(@Nonnull V vertex) {
        return getConnectedEdges(vertex).stream().filter(e -> !e.isDirected() || e.getSecondVertex().equals(vertex)).collect(Collectors.toSet());
    }

    @Override
    public void addVertex(@Nonnull V vertex) {
        matrix.add(vertex);
    }

    @Override
    public void removeVertex(@Nonnull V vertex) {
        matrix.delete(vertex);
    }

    @Override
    public void addEdge(@Nonnull E edge) {
        matrix.add(edge);
    }

    @Override
    public void removeEdge(@Nonnull E edge) {
        matrix.delete(edge);
    }

    @Override
    public int getVertexCount() {
        return matrix.matrix.size();
    }

    @Override
    public int getEdgeCount() {
        return (int) matrix.matrix.values().stream().mapToLong(Set::size).sum();
    }

    @Override
    public void clear() {
        matrix.clear();
    }

    @Override
    @Nonnull
    public Iterator<T> iterator() {
        return matrix.values().iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimpleGraph)) {
            return false;
        }
        SimpleGraph<?, ?, ?> that = (SimpleGraph<?, ?, ?>) o;
        return Objects.equals(matrix, that.matrix);
    }

    /**
     * A fairly simple adjacency matrix, it opts for quicker operation times as opposed to memory efficiency.
     */
    private final class AdjacencyMatrix {

        private final ReadWriteLock lock;
        private final Map<V, Set<E>> matrix;
        private final Map<T, V> vertexExchange;
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
            if (!matrix.containsKey(v1) || !matrix.containsKey(v2)) {
                readUnlock();
                return Collections.emptyList();
            }
            Collection<E> c = matrix.get(v1).stream().filter(e -> e.contains(v2)).collect(Collectors.toList());
            readUnlock();
            return c;
        }

        @Nonnull
        public Collection<E> edges(@Nonnull V vertex) {
            readLock();
            if (!matrix.containsKey(vertex)) {
                readUnlock();
                return Collections.emptyList();
            }
            Set<E> s = matrix.get(vertex);
            readUnlock();
            return Collections.unmodifiableSet(s);
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
            Set<V> c = matrix.keySet();
            readUnlock();
            return Collections.unmodifiableSet(c);
        }

        @Nonnull
        Collection<T> values() {
            readLock();
            Set<T> c = matrix.keySet().stream().map(Vertex::get).collect(Collectors.toSet());
            readUnlock();
            return Collections.unmodifiableSet(c);
        }

        void add(@Nonnull V v) {
            writeLock();
            matrix.putIfAbsent(v, setSupplier.get());
            vertexExchange.put(v.get(), v);
            writeUnlock();
        }

        void add(@Nonnull E e) {
            writeLock();
            matrix.putIfAbsent(e.getFirstVertex(), setSupplier.get());
            vertexExchange.putIfAbsent(e.getFirstVertex().get(), e.getFirstVertex());
            matrix.putIfAbsent(e.getSecondVertex(), setSupplier.get());
            vertexExchange.putIfAbsent(e.getSecondVertex().get(), e.getSecondVertex());
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

        @Nullable
        V exchange(@Nullable T vertex) {
            V val;
            readLock();
            val = vertexExchange.get(vertex);
            readUnlock();
            return val;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SimpleGraph.AdjacencyMatrix)) {
                return false;
            }
            AdjacencyMatrix that = (SimpleGraph.AdjacencyMatrix) o;
            return Objects.equals(matrix, that.matrix) &&
                    Objects.equals(vertexExchange, that.vertexExchange);
        }
    }

    // Internal strategies optimized for this SimpleGraph implementation

    private static void assertSimpleGraph(Graph g) {
        if (!(g instanceof SimpleGraph)) {
            throw new InvalidGraphConfigurationException();
        }
    }

    private static final class DefaultPathfindStrategy<T, V extends Vertex<T>, E extends Edge<T, V>> implements PathfindStrategy<T, V, E, Graph<T, V, E>> {

        //A* impl has internal optimizations
        private final AStarPathfindStrategy<T, V, E> delegate = new AStarPathfindStrategy<>();

        @Override
        @Nonnull
        public List<E> pathfind(@Nonnull V vertex1, @Nonnull V vertex2, @Nonnull Graph<T, V, E> graph) {
            return delegate.pathfind(vertex1, vertex2, graph);
        }
    }

    private static final class DefaultSortStrategy<T, V extends Vertex<T>, E extends Edge<T, V>> implements SortStrategy<T, V, E, Graph<T, V, E>> {

        //Natural sort impl has internal optimizations
        private final NaturalSortStrategy<T, V, E> delegate = new NaturalSortStrategy<>();

        @Override
        @Nonnull
        public List<V> sort(@Nonnull Graph<T, V, E> graph) {
            return delegate.sort(graph);
        }
    }

    //Directly interface with adjacency matrix for a quick, but undefined traversal
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
