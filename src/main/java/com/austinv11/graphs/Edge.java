package com.austinv11.graphs;

import com.austinv11.graphs.util.VarArgIterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public interface Edge<T, V extends Vertex<T>> extends Iterable<T> {

    double getWeight();

    @Nonnull
    V getFirstVertex();

    @Nonnull
    V getSecondVertex();

    @Nullable
    T getFirst();

    @Nullable
    T getSecond();

    @Nonnull
    List<T> asList();

    boolean isDirected();

    default boolean contains(@Nonnull V vertex) {
        return getFirstVertex().equals(vertex) || getSecondVertex().equals(vertex);
    }

    default boolean contains(@Nullable T obj) {
       return Objects.equals(getFirst(), obj) || Objects.equals(getSecond(), obj);
    }

    @Override
    @Nonnull
    default Iterator<T> iterator() {
        return new VarArgIterator<>(getFirst(), getSecond());
    }
}
