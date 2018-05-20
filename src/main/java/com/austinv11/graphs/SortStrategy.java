package com.austinv11.graphs;

import javax.annotation.Nonnull;
import java.util.List;

public interface SortStrategy<T, V extends Vertex<T>, E extends Edge<T, V>, G extends Graph<T, V, E>> {

    @Nonnull
    List<V> sort(@Nonnull G graph);
}
