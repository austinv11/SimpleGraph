package com.austinv11.graphs.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;

/**
 * An object wrapper which centralizes comparable logic by using the object itself if it implements comparable,
 * else it uses a provided comparator.
 */
public class ComparableWrapper<T> implements Comparable<T> {

    private final T obj;
    private final Comparator<T> comparator;

    public ComparableWrapper(@Nullable T obj) {
        this(obj, new Comparator<T>() {

            private boolean isComparable = obj instanceof Comparable;

            @Override
            public int compare(T o1, T o2) {
                if ((o1 == null || o2 == null) || !isComparable)
                    return 0;

                return ((Comparable<T>) o1).compareTo(o1);
            }
        });
    }

    public ComparableWrapper(@Nullable T obj, @Nonnull Comparator<T> comparator) {
        this.obj = obj;
        this.comparator = comparator;
    }

    @Override
    public int compareTo(@Nullable T o) {
        if (obj == null || o == null)
            return 0;
        return comparator.compare(obj, o);
    }
}
