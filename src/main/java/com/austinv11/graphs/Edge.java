package com.austinv11.graphs;

import com.austinv11.graphs.util.VarArgIterator;

import java.util.Iterator;
import java.util.List;

public interface Edge<T, V extends Vertex<T>> extends Iterable<T> {

    double getWeight();

    V getFirstVertex();

    V getSecondVertex();

    T getFirst();

    T getSecond();

    List<T> asList();

    boolean isDirected();

    default boolean contains(V vertex) {
        return getFirstVertex().equals(vertex) || getSecondVertex().equals(vertex);
    }

    default boolean contains(T obj) {
       return getFirst().equals(obj) || getSecond().equals(obj);
    }

    @Override
    default Iterator<T> iterator() {
        return new VarArgIterator<>(getFirst(), getSecond());
    }
}
