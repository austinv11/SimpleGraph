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

    @Override
    default Iterator<T> iterator() {
        return new VarArgIterator<>(getFirst(), getSecond());
    }
}
