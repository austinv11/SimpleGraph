package com.austinv11.graphs.impl;

import com.austinv11.graphs.Edge;
import com.austinv11.graphs.Vertex;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class SimpleEdge<T, V extends Vertex<T>> implements Edge<T, V> {

    private final double weight;
    private final V first, second;
    private final boolean directed;

    public SimpleEdge(V first, V second, double weight, boolean directed) {
        this.first = first;
        this.second = second;
        this.weight = weight;
        this.directed = directed;
    }

    public static <T, V extends Vertex<T>> Builder<T, V> builder(V first, V second) {
        return new Builder<>(first, second);
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    @Nonnull
    public V getFirstVertex() {
        return first;
    }

    @Override
    @Nonnull
    public V getSecondVertex() {
        return second;
    }

    @Override
    @Nullable
    public T getFirst() {
        return first.get();
    }

    @Override
    @Nullable
    public T getSecond() {
        return second.get();
    }

    @Override
    @Nonnull
    public List<T> asList() {
        return Arrays.asList(first.get(), second.get());
    }

    @Override
    @Nonnull
    public Edge<T, V> reverse() {
        return new SimpleEdge<>(second, first, weight, directed);
    }

    @Override
    public boolean isDirected() {
        return directed;
    }

    public static class Builder<T, V extends Vertex<T>> {

        private double weight;
        private final V first, second;
        private boolean directed;

        public Builder(V first, V second) {
            this.first = first;
            this.second = second;
        }

        public Builder<T, V> setWeight(double weight) {
            this.weight = weight;
            return this;
        }

        public Builder<T, V> setDirected(boolean directed) {
            this.directed = directed;
            return this;
        }

        public SimpleEdge<T, V> build() {
            return new SimpleEdge<>(first, second, weight, directed);
        }
    }
}
