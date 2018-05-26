package com.austinv11.graphs.alg;

import com.austinv11.graphs.Edge;
import com.austinv11.graphs.Graph;
import com.austinv11.graphs.SortStrategy;
import com.austinv11.graphs.Vertex;
import com.austinv11.graphs.util.ComparableWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NaturalSortStrategy<T, V extends Vertex<T>, E extends Edge<T, V>> implements SortStrategy<T, V, E, Graph<T, V, E>> {

    @Override
    @Nonnull
    public List<V> sort(@Nonnull Graph<T, V, E> graph) {
        return sort(graph, null);
    }

    @Nonnull
    public List<V> sort(@Nonnull Graph<T, V, E> graph, @Nullable Comparator<T> comparator) {
        Comparator<V> vertexComparator = (o1, o2) -> {
            if (comparator == null) {
                return new ComparableWrapper<>(o1.get()).compareTo(o2.get());
            } else {
                return new ComparableWrapper<>(o1.get(), comparator).compareTo(o2.get());
            }
        };
        return graph.vertices().stream().sorted(vertexComparator).collect(Collectors.toList());
    }
}
