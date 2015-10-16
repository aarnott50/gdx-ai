package com.badlogic.gdx.ai.pfa.jumppointsearch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

public class DefaultTiledGraph<N extends TiledNode<N>> implements TiledGraph<N> {

    private final int width;
    private final int height;

    /* Using arrays of generics is ugly, but I don't see an alternative here. The type-safety provided by generics is useful,
     * even if it is ignored for retrieval. It is honored for insertion, so the array should be type-safe.
     *
     * A more elegant alternative would be to make an object to represent a 2d array, but I like the fast lookup time that
     * using a native array offers.
     */
    private final TiledNode<N>[][] nodes;

    private final Set<N> nodesProcessedForCachedConnections = new HashSet<>();
    private final Map<NodePair<N>, Boolean> cachedConnections = new HashMap<>();

    @SuppressWarnings("unchecked")
    public DefaultTiledGraph(final int width, final int height) {
        if (width <= 0) {
            throw new IllegalArgumentException("width must be positive");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("height must be positive");
        }

        this.width = width;
        this.height = height;

        nodes =  new TiledNode[width][height];
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Array<Connection<N>> getConnections(final N fromNode) {
        return fromNode.getConnections();
    }

    public void setNode(final int x, final int y, final N node) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException("Coordinates [" + x + ", " + y + "] are outside the grid boundaries");
        }
        if (node == null) {
            throw new IllegalArgumentException("node cannot be null");
        }

        nodes[x][y] = node;
    }

    @SuppressWarnings("unchecked")
    @Override
    public N getNode(final int x, final int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException("Coordinates [" + x + ", " + y + "] are outside the grid boundaries");
        }

        return (N)nodes[x][y];
    }

    @Override
    public boolean isInBounds(final int x, final int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    @Override
    public boolean hasConnection(final N sourceNode, final int x, final int y) {
        return hasConnection(sourceNode, getNode(x, y));
    }

    @Override
    public boolean hasConnection(final N sourceNode, final N destinationNode) {
        if (sourceNode == null) {
            throw new IllegalArgumentException("sourceNode cannot be null");
        }
        if (destinationNode == null) {
            throw new IllegalArgumentException("destinationNode cannot be null");
        }

        if (nodesProcessedForCachedConnections.contains(sourceNode)) {
            final NodePair<N> nodePair = new NodePair<>(sourceNode, destinationNode);

            final Boolean cachedConnectionValue = cachedConnections.get(nodePair);
            if (cachedConnectionValue != null && cachedConnectionValue == true) {
                return true;
            } else {
                return false;
            }
        } else {
            buildCachedConnectionsForNode(sourceNode);
            return hasConnection(sourceNode, destinationNode);
        }
    }

    private void buildCachedConnectionsForNode(final N node) {
        for (final Connection<N> connection : getConnections(node)) {
            final NodePair<N> connectionNodePair = new NodePair<N>(node, connection.getToNode());
            cachedConnections.put(connectionNodePair, true);
        }
        nodesProcessedForCachedConnections.add(node);
    }

    private static class NodePair<N> {
        public final N firstNode;
        public final N secondNode;

        public NodePair(final N firstNode, final N secondNode) {
            this.firstNode = firstNode;
            this.secondNode = secondNode;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + firstNode.hashCode();
            result = prime * result + secondNode.hashCode();
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (obj == null) {
                return false;
            } else if (getClass() != obj.getClass()) {
                return false;
            }

            final NodePair<?> other = (NodePair<?>) obj;
            if (firstNode == null && other.firstNode != null) {
                return false;
            } else if (!firstNode.equals(other.firstNode)) {
                return false;
            } else if (secondNode == null && other.secondNode != null) {
                return false;
            } else if (!secondNode.equals(other.secondNode)) {
                return false;
            } else {
                return true;
            }
        }
    }

}
