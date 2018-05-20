package com.austinv11.graphs.impl;

import com.austinv11.graphs.Vertex;

import javax.annotation.Nullable;
import java.util.Objects;

public class SimpleVertex<T> implements Vertex<T> {

    private final T obj;

    public SimpleVertex(@Nullable T obj) {
        this.obj = obj;
    }

    @Override
    @Nullable
    public T get() {
        return obj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimpleVertex)) {
            return false;
        }
        SimpleVertex<?> that = (SimpleVertex<?>) o;
        return Objects.equals(obj, that.obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(obj);
    }
}
