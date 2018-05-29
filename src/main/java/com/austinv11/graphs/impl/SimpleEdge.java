package com.austinv11.graphs.impl;

import com.austinv11.graphs.Edge;
import com.austinv11.graphs.Vertex;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * A basic implementation of an edge. It can be configured to be weighted, unweighted, directed,
 * undirected and any combination of these parameters.
 *
 * @see #builder(com.austinv11.graphs.Vertex, com.austinv11.graphs.Vertex)
 */
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

    /**
     * Creates an edge builder.
     *
     * @param first The "first" vertex.
     * @param second The "second" vertex.
     * @return The builder instance.
     */
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

    /**
     * An edge builder.
     */
    public static class Builder<T, V extends Vertex<T>> {

        private double weight;
        private final V first, second;
        private boolean directed;

        public Builder(V first, V second) {
            this.first = first;
            this.second = second;
        }

        /**
         * Sets the weight of the produced edge.
         *
         * @param weight The weight (default is 0.0).
         * @return Same builder instance for chaining
         */
        public Builder<T, V> setWeight(double weight) {
            this.weight = weight;
            return this;
        }

        /**
         * Configures whether the "first" and "second" vertices refer to a directed path (first = root,
         * second = destination).
         *
         * @param directed Whether the edge is directed (default is false).
         * @return Same builder instance for chaining.
         */
        public Builder<T, V> setDirected(boolean directed) {
            this.directed = directed;
            return this;
        }

        /**
         * Builds a new edge instance from the provided settings.
         *
         * @return The new edge.
         */
        public SimpleEdge<T, V> build() {
            return new SimpleEdge<>(first, second, weight, directed);
        }
    }
}
