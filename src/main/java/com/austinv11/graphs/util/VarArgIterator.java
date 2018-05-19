package com.austinv11.graphs.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

public class VarArgIterator<E> implements Iterator<E> {

    private final E[] elements;
    private final AtomicInteger i = new AtomicInteger(0);

    public VarArgIterator(@Nonnull E... elements) {
        this.elements = elements;
    }

    @Override
    public boolean hasNext() {
        return i.get() >= elements.length;
    }

    @Override
    @Nullable
    public E next() {
        if (!hasNext())
            throw new NoSuchElementException();
        return elements[i.getAndIncrement()];
    }
}
