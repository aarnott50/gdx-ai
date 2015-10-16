package com.badlogic.gdx.ai.pfa.jumppointsearch;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

/**
 * This neighbor pruning strategy follows all of the requirements for jump point search for pathfinding on
 * {@link TiledGraph} instances which allow diagonal movement in any situation where the diagonal
 * tile is unblocked.
 *
 * <p>For example, suppose you have the following map:</p>
 *
 * <pre>
 *      01234
 *    0 .#...
 *    1 ..#..
 *    2 ...#.
 * </pre>
 *
 * This pruning strategy considers movement from 1,1 to 2,0 to be a legal move.
 *
 * @param <N> Type of node extending {@link TiledNode}
 */
public class JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<N extends TiledNode<N>> implements JumpPointSearchNodeNeighborPruningStrategy<N> {

    private static final int MAX_GRID_NEIGHBORS = 8;

    @Override
    public Array<N> getPrunedNeighbors(
            final TiledGraph<N> graph,
            final N node,
            final N parentNode) {

        if (graph == null) {
            throw new IllegalArgumentException("graph cannot be null");
        }

        if (node == null) {
            throw new IllegalArgumentException("node cannot be null");
        }

        if (node == parentNode) {
            throw new IllegalArgumentException("parentNode must be different than node");
        }


        if (parentNode == null) {
            return getAllNeighbors(graph, node);
        }

        final int x = node.getX();
        final int y = node.getY();
        final int parentX = parentNode.getX();
        final int parentY = parentNode.getY();

        final int xDirection = (int)Math.signum(x - parentX);
        final int yDirection = (int)Math.signum(y - parentY);

        if (xDirection != 0 && yDirection != 0) {
            return getDiagonallyPrunedNeighbors(graph, node, x, y, xDirection, yDirection);
        } else if(xDirection != 0) {
            return getHorizontallyPrunedNeighbors(graph, node, x, y, xDirection, yDirection);
        } else if(yDirection != 0) {
            return getVerticallyPrunedNeighbors(graph, node, x, y, xDirection, yDirection);
        } else {
            throw new IllegalStateException("Expected parent node to have a different x or y position value");
        }
    }


    private Array<N> getAllNeighbors(final TiledGraph<N> graph, final N node) {
        final Array<Connection<N>> connections = graph.getConnections(node);

        final Array<N> neighbors = new Array<N>(connections.size);
        for (final Connection<N> connection : connections) {
            neighbors.add(connection.getToNode());
        }
        return neighbors;
    }


    private Array<N> getDiagonallyPrunedNeighbors(
            final TiledGraph<N> graph,
            final N node,
            final int x,
            final int y,
            final int xDirection,
            final int yDirection) {

        final Array<N> neighbors = new Array<N>(MAX_GRID_NEIGHBORS);

        final N verticallyAdjacentNode = graph.getNode(x, y + yDirection);
        final N horizontallyAdjacentNode = graph.getNode(x + xDirection, y);
        final N diagonallyAdjacentNode = graph.getNode(x + xDirection, y + yDirection);

        if (graph.hasConnection(node, verticallyAdjacentNode)) {
            neighbors.add(verticallyAdjacentNode);
        }

        if (graph.hasConnection(node, horizontallyAdjacentNode)) {
            neighbors.add(horizontallyAdjacentNode);
        }

        if (graph.hasConnection(node, diagonallyAdjacentNode)) {
            neighbors.add(diagonallyAdjacentNode);
        }

        if (!graph.hasConnection(node, x, y - yDirection) && graph.hasConnection(node, x + xDirection, y - yDirection)) {
            neighbors.add(graph.getNode(x + xDirection, y - yDirection));
        }

        if (!graph.hasConnection(node, x - xDirection, y) && graph.hasConnection(node, x - xDirection, y + yDirection)) {
            neighbors.add(graph.getNode(x - xDirection, y + yDirection));
        }

        return neighbors;
    }

    private Array<N> getHorizontallyPrunedNeighbors(
            final TiledGraph<N> graph,
            final N node,
            final int x,
            final int y,
            final int xDirection,
            final int yDirection) {

        final Array<N> neighbors = new Array<N>(MAX_GRID_NEIGHBORS);

        final N horizontallyAdjacentNode = graph.getNode(x + xDirection, y);
        final N topNode = graph.getNode(x, y + 1);
        final N bottomNode = graph.getNode(x, y - 1);

        final boolean isTopNodeConnected = graph.hasConnection(node, topNode);
        final boolean isBottomNodeConnected = graph.hasConnection(node, bottomNode);

        if (graph.hasConnection(node, horizontallyAdjacentNode)) {
            neighbors.add(horizontallyAdjacentNode);

            if (!isTopNodeConnected && graph.hasConnection(node, x + xDirection, y + 1)) {
                neighbors.add(graph.getNode(x + xDirection, y + 1));
            }

            if (!isBottomNodeConnected && graph.hasConnection(node, x + xDirection, y - 1)) {
                neighbors.add(graph.getNode(x + xDirection, y - 1));
            }
        }

        return neighbors;
    }

    private Array<N> getVerticallyPrunedNeighbors(
            final TiledGraph<N> graph,
            final N node,
            final int x,
            final int y,
            final int xDirection,
            final int yDirection) {

        final Array<N> neighbors = new Array<N>(MAX_GRID_NEIGHBORS);

        final N verticallyAdjacentNode = graph.getNode(x, y + yDirection);
        final N rightNode = graph.getNode(x + 1, y);
        final N leftNode = graph.getNode(x - 1, y);

        final boolean isRightNodeConnected = graph.hasConnection(node, rightNode);
        final boolean isLeftNodeConnected = graph.hasConnection(node, leftNode);

        if (graph.hasConnection(node, verticallyAdjacentNode)) {
            neighbors.add(verticallyAdjacentNode);

            if (!isRightNodeConnected && graph.hasConnection(node, x + 1, y + yDirection)) {
                neighbors.add(graph.getNode(x + 1, y + yDirection));
            }

            if (!isLeftNodeConnected && graph.hasConnection(node, x - 1, y + yDirection)) {
                neighbors.add(graph.getNode(x - 1, y + yDirection));
            }
        }

        return neighbors;
    }

}
