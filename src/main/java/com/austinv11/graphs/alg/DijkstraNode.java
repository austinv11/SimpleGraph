package com.austinv11.graphs.alg;

import com.austinv11.graphs.Edge;
import com.austinv11.graphs.Vertex;

final class DijkstraNode<V extends Vertex<?>> implements Comparable<DijkstraNode> {

    final V vertex;
    double tenativeDistance = Double.POSITIVE_INFINITY;
    boolean visited = false;
    DijkstraNode<V> hint = null;
    Edge<?, V> hintEdge = null;

    DijkstraNode(V vertex) {
        this.vertex = vertex;
    }

    DijkstraNode(V vertex, double dist) {
        this.vertex = vertex;
        this.tenativeDistance = dist;
    }

    public V getVertex() {
        return vertex;
    }

    public double getTenativeDistance() {
        return tenativeDistance;
    }

    public void updateTenativeDistance(double newDist) {
        tenativeDistance = newDist;
    }

    public boolean isVisited() {
        return visited;
    }

    public void markVisited() {
        visited = true;
    }

    public DijkstraNode<V> getHint() {
        return hint;
    }

    public Edge<?, V> getHintEdge() {
        return hintEdge;
    }

    public void hint(DijkstraNode<V> hint, Edge<?, V> edge) {
        this.hint = hint;
        this.hintEdge = edge;
    }

    @Override
    public int compareTo(DijkstraNode o) {
        return Double.compare(tenativeDistance, o.tenativeDistance);
    }
}
