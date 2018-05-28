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

    @Nonnull
    Edge<T, V> reverse();

    @Nonnull
    default Edge<T, V> ensureOrder(@Nonnull V first) {
        if (!first.equals(getFirstVertex())) {
            throw new RuntimeException("Passed vertices are not present in this edge!");
        }

        if (getFirstVertex().equals(first)) {
            return this;
        } else {
            if (isDirected()) {
                throw new RuntimeException("Cannot flip a directed edge!");
            }
            return reverse();
        }
    }


    @Nonnull
    default Edge<T, V> ensureOrder(@Nonnull V first, @Nonnull V second) {
        if (!second.equals(getSecondVertex())) {
            throw new RuntimeException("Passed vertices are not present in this edge!");
        }
        return ensureOrder(first);
    }

    @Nonnull
    default V getOther(@Nonnull V original) {
        if (original.equals(getFirstVertex())) {
            return getSecondVertex();
        } else {
            return getFirstVertex();
        }
    }

    boolean isDirected();

    default boolean isLoop() {
        return getFirstVertex().equals(getSecondVertex());
    }

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
