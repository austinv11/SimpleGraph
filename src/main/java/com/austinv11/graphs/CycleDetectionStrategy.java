package com.austinv11.graphs;

import javax.annotation.Nonnull;

public interface CycleDetectionStrategy<T, V extends Vertex<T>, E extends Edge<T, V>, G extends Graph<T, V, E>> {

    boolean findCycle(@Nonnull G graph);
}
