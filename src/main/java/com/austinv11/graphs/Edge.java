package com.austinv11.graphs;

import com.austinv11.graphs.util.VarArgIterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * This represents an abstraction for a graph edge.
 */
public interface Edge<T, V extends Vertex<T>> extends Iterable<T> {

    /**
     * Gets the weight of this edge. By convention, this should be 0 if unweighted. Additionally,
     * negative values are allowed, but many algorithms (in general) expect positive values.
     *
     * @return The edge's weight.
     */
    double getWeight();

    /**
     * Gets the first vertex in this edge.
     *
     * @return The first vertex.
     */
    @Nonnull
    V getFirstVertex();

    /**
     * Gets the second vertex in this edge.
     *
     * @return The second vertex.
     */
    @Nonnull
    V getSecondVertex();

    /**
     * Gets the value in the first vertex.
     *
     * @return The first value.
     */
    @Nullable
    T getFirst();

    /**
     * Gets the value in the second vertex.
     *
     * @return The second value.
     */
    @Nullable
    T getSecond();

    /**
     * Gets the edge in the form of a list.
     *
     * @return The edge as a list (first vertex at index 0, second at index 1).
     */
    @Nonnull
    List<T> asList();

    /**
     * Reverses the order of this edge. (first -> second, second -> first).
     *
     * @return The edge with reversed order.
     */
    @Nonnull
    Edge<T, V> reverse();

    /**
     * Checks if the requested first vertex is actually first, if not the edge is reversed (assuming order
     * is not significant).
     *
     * @param first The required first vertex.
     * @return The edge in the requested order.
     */
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

    /**
     * Checks if the requested first and second vertex is actually first and second, respectively. If not,
     * the edge is reversed (assuming order is not significant).
     *
     * @param first The required first vertex.
     * @param second The required second vertex.
     * @return The edge in the requested order.
     */
    @Nonnull
    default Edge<T, V> ensureOrder(@Nonnull V first, @Nonnull V second) {
        if (!second.equals(getSecondVertex())) {
            throw new RuntimeException("Passed vertices are not present in this edge!");
        }
        return ensureOrder(first);
    }

    /**
     * Gets the vertex not equal to the given one.
     *
     * @param original The given vertex.
     * @return The "other" vertex.
     */
    @Nonnull
    default V getOther(@Nonnull V original) {
        if (original.equals(getFirstVertex())) {
            return getSecondVertex();
        } else {
            return getFirstVertex();
        }
    }

    /**
     * Checks if this edge is directed. If directed, the "first" vertex is equivalent to the root and the
     * "second" vertex is equivalent to the destination. Otherwise, the connotation of "first"/"second" is
     * irrelevant.
     *
     * @return True if directed, false if otherwise.
     */
    boolean isDirected();

    /**
     * Checks if this edge is (by itself) a loop.
     *
     * @return True if a loop, else false.
     */
    default boolean isLoop() {
        return getFirstVertex().equals(getSecondVertex());
    }

    /**
     * Checks if this edge contains a given vertex.
     *
     * @param vertex The vertex.
     * @return True if this edge contains the vertex, else false.
     */
    default boolean contains(@Nonnull V vertex) {
        return getFirstVertex().equals(vertex) || getSecondVertex().equals(vertex);
    }

    /**
     * Checks if this edge contains a given value.
     *
     * @param obj The value.
     * @return True if this edge contains the value, else false.
     */
    default boolean contains(@Nullable T obj) {
       return Objects.equals(getFirst(), obj) || Objects.equals(getSecond(), obj);
    }

    @Override
    @Nonnull
    default Iterator<T> iterator() {
        return new VarArgIterator<>(getFirst(), getSecond());
    }
}
