package com.austinv11.graphs;

import javax.annotation.Nonnull;
import java.util.List;

public interface PathfindStrategy<T, V extends Vertex<T>, E extends Edge<T, V>, G extends Graph<T, V, E>> {

    @Nonnull
    List<E> pathfind(@Nonnull V vertex1, @Nonnull V vertex2, @Nonnull G graph);
}
