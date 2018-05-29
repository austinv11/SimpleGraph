package com.austinv11.graphs.extra.dependency;

import com.austinv11.graphs.Graph;
import com.austinv11.graphs.alg.TopologicalSortStrategy;
import com.austinv11.graphs.impl.DirectedAcyclicGraph;
import com.austinv11.graphs.impl.SimpleGraph;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a dependency "graph" (this doesn't actually implement {@link com.austinv11.graphs.Graph}).
 *
 * It is backed by a {@link com.austinv11.graphs.impl.DirectedAcyclicGraph} and
 * {@link com.austinv11.graphs.impl.SimpleGraph} and will attempt to resolve dependencies in a logical manner.
 */
public class DependencyGraph<D extends Dependency.Info> {

    private final Graph<D, Dependency<D>, DependencyRelationship<D>> graph = new DirectedAcyclicGraph<>(new SimpleGraph<>());

    /**
     * Adds a dependency to the graph, and recursively resolves all its transitive dependencies.
     *
     * @param dependency The dependency to add.
     */
    public void addAndResolve(Dependency<D> dependency) {
        addAndResolve(dependency, new HashSet<>());
    }

    private void addAndResolve(Dependency<D> dependency, Set<Dependency<D>> progress) {
        if (progress.contains(dependency))
            return;

        addOrReplace(dependency);
        progress.add(dependency);

        for (Dependency<D> dep : dependency.getDirectDependencies()) {
            addOrReplace(dep);
            graph.addEdge(new DependencyRelationship<>(dependency, dep));
            progress.add(dep);
            addAndResolve(dep, progress);
        }
    }

    private void addOrReplace(Dependency<D> dep) {
        Dependency<D> dep2 = graph.vertices().stream()
                .filter(d -> d.get().getName().equals(dep.get().getName()))
                .filter(d -> !d.get().getVersion().equals(dep.get().getVersion()))
                .findFirst().orElse(null);
        if (dep2 != null) {
            if (dep.get().getVersion().isHigherThan(dep2.get().getVersion())) {
                Collection<DependencyRelationship<D>> edges = graph.getConnectedEdges(dep2);
                graph.removeVertex(dep2);
                graph.addVertex(dep);
                for (DependencyRelationship<D> edge : edges) {
                    if (edge.getFirstVertex().equals(dep2)) {
                        graph.addEdge(new DependencyRelationship<>(dep, edge.getSecondVertex()));
                    } else {
                        graph.addEdge(new DependencyRelationship<>(edge.getFirstVertex(), dep));
                    }
                }
            }
        } else {
            graph.addVertex(dep);
        }
    }

    /**
     * Attempts to install all dependencies.
     *
     * @return True if successful, false if at least one dependency failed.
     */
    public boolean installAll() {
        List<Dependency<D>> dependencies = graph.sortVertices(new TopologicalSortStrategy<>());
        for (Dependency<D> dep : dependencies) {
            if (!dep.install()) {
                return false;
            }
        }
        return true;
    }


}
