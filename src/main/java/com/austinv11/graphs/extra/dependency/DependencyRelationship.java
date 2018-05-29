package com.austinv11.graphs.extra.dependency;

import com.austinv11.graphs.Edge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * An edge representing a relationship between two dependencies defined as Dependency -> Dependent.
 */
public class DependencyRelationship<D extends Dependency.Info> implements Edge<D, Dependency<D>> {

    private final Dependency<D> dependent, dependency;

    public DependencyRelationship(Dependency<D> dependent, Dependency<D> dependency) {
        this.dependent = dependent;
        this.dependency = dependency;
    }

    @Override
    public double getWeight() {
        return 0;
    }

    @Nonnull
    @Override
    public Dependency<D> getFirstVertex() {
        return getDependency();
    }

    @Nonnull
    @Override
    public Dependency<D> getSecondVertex() {
        return getDependent();
    }

    public Dependency<D> getDependent() {
        return dependent;
    }

    public Dependency<D> getDependency() {
        return dependency;
    }

    @Nullable
    @Override
    public D getFirst() {
        return getFirstVertex().get();
    }

    @Nullable
    @Override
    public D getSecond() {
        return getSecondVertex().get();
    }

    @Nonnull
    @Override
    public List<D> asList() {
        List<D> l = new ArrayList<>();
        l.add(getFirst());
        l.add(getSecond());
        return l;
    }

    @Nonnull
    @Override
    public Edge<D, Dependency<D>> reverse() {
        throw new RuntimeException("Cannot reverse!");
    }

    @Override
    public boolean isDirected() {
        return true;
    }
}
