package com.badlogic.gdx.ai.pfa.jumppointsearch;

public class JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<N extends TiledNode<N>> implements JumpPointSearchJumpingStrategy<N> {

    @Override
    public N getJumpPoint(final TiledGraph<N> graph, final N sourceNode, final N jumpNode, final N destinationNode) {
        if (graph == null) {
            throw new IllegalArgumentException("graph cannot be null");
        }

        if (sourceNode == null) {
            throw new IllegalArgumentException("sourceNode cannot be null");
        }

        if (jumpNode == null) {
            throw new IllegalArgumentException("jumpNode cannot be null");
        }

        if (destinationNode == null) {
            throw new IllegalArgumentException("destinationNode cannot be null");
        }

        if (sourceNode == jumpNode) {
            throw new IllegalArgumentException("sourceNode must be different than jumpNode");
        }

        if (!graph.hasConnection(sourceNode, jumpNode)) {
            throw new IllegalArgumentException("jumpNode must be a neighbor of sourceNode");
        }

        return findNextJumpPoint(graph, sourceNode, jumpNode, destinationNode);
    }

    private N findNextJumpPoint(final TiledGraph<N> graph, final N sourceNode, final N jumpNode, final N destinationNode) {
        if (jumpNode == null || !graph.hasConnection(sourceNode, jumpNode)) {
            return null;
        } else if (shouldEndJump(graph, sourceNode, jumpNode, destinationNode)) {
            return jumpNode;
        } else {
            final N nextJumpPointToCheck = getNextJumpPointToCheck(graph, sourceNode, jumpNode, destinationNode);
            return findNextJumpPoint(graph, jumpNode, nextJumpPointToCheck, destinationNode);
        }
    }

    private boolean shouldEndJump(final TiledGraph<N> graph, final N sourceNode, final N jumpNode, final N destinationNode) {
        if (jumpNode == destinationNode) {
            return true;
        }

        final int sourceNodeX = sourceNode.getX();
        final int sourceNodeY = sourceNode.getY();
        final int jumpNodeX = jumpNode.getX();
        final int jumpNodeY = jumpNode.getY();

        final int xDirection = (int)Math.signum(jumpNodeX - sourceNodeX);
        final int yDirection = (int)Math.signum(jumpNodeY - sourceNodeY);

        if (jumpNode == destinationNode) {
            return true;
        } else if (isJumpingDiagonally(xDirection, yDirection) && hasForcedNeighborsAlongDiagonal(graph, sourceNode, jumpNode, destinationNode, xDirection, yDirection)) {
            return true;
        } else if (isJumpingHorizontally(xDirection, yDirection) && hasForcedNeighborsAlongHorizontal(graph, jumpNode, xDirection)) {
            return true;
        } else if (isJumpingVertically(xDirection, yDirection) && hasForcedNeighborsAlongVertical(graph, sourceNode, jumpNode, destinationNode, yDirection)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isJumpingDiagonally(
            final int xDirection,
            final int yDirection) {
        return xDirection != 0 && yDirection != 0;
    }

    private boolean isJumpingHorizontally(
            final int xDirection,
            final int yDirection) {
        return xDirection != 0 && yDirection == 0;
    }

    private boolean isJumpingVertically(
            final int xDirection,
            final int yDirection) {
        return xDirection == 0 && yDirection != 0;
    }

    private boolean hasForcedNeighborsAlongDiagonal(
            final TiledGraph<N> graph,
            final N sourceNode,
            final N jumpNode,
            final N destinationNode,
            final int xDirection,
            final int yDirection) {

        final int x = jumpNode.getX();
        final int y = jumpNode.getY();

        if (doesGraphHaveConnectionIfInBounds(graph, jumpNode, x - xDirection, y + yDirection)
                && !doesGraphHaveConnectionIfInBounds(graph, jumpNode, x - xDirection, y)) {
            return true;
        } else if (doesGraphHaveConnectionIfInBounds(graph, jumpNode, x + xDirection, y - yDirection)
                && !doesGraphHaveConnectionIfInBounds(graph, jumpNode, x, y - yDirection)) {
            return true;
        }

        final N horizontalAdjacentNode = graph.getNode(x + xDirection, y);
        final N horizontalJumpPoint = findNextJumpPoint(graph, jumpNode, horizontalAdjacentNode, destinationNode);
        if (horizontalJumpPoint != null) {
            return true;
        }

        final N verticalAdjacentNode = graph.getNode(x, y + yDirection);
        final N verticalJumpPoint = findNextJumpPoint(graph, jumpNode, verticalAdjacentNode, destinationNode);
        if (verticalJumpPoint != null) {
            return true;
        }

        return false;
    }

    private boolean hasForcedNeighborsAlongHorizontal(
            final TiledGraph<N> graph,
            final N jumpNode,
            final int xDirection) {

        final int x = jumpNode.getX();
        final int y = jumpNode.getY();

        return hasForcedNeighborsAlongHorizontalAboveJumpNode(graph, jumpNode, xDirection, x, y)
                || hasForcedNeighborsAlongHorizontalBelowJumpNode(graph, jumpNode, xDirection, x, y);
    }

    private boolean hasForcedNeighborsAlongHorizontalAboveJumpNode(
            final TiledGraph<N> graph,
            final N jumpNode,
            final int xDirection,
            final int x,
            final int y) {

        return doesGraphHaveConnectionIfInBounds(graph, jumpNode, x + xDirection, y + 1)
                && !doesGraphHaveConnectionIfInBounds(graph, jumpNode, x, y + 1);
    }

    private boolean hasForcedNeighborsAlongHorizontalBelowJumpNode(
            final TiledGraph<N> graph,
            final N jumpNode,
            final int xDirection,
            final int x,
            final int y) {

        return doesGraphHaveConnectionIfInBounds(graph, jumpNode, x + xDirection, y - 1)
                && !doesGraphHaveConnectionIfInBounds(graph, jumpNode, x, y - 1);
    }

    private boolean hasForcedNeighborsAlongVertical(
            final TiledGraph<N> graph,
            final N sourceNode,
            final N jumpNode,
            final N destinationNode,
            final int yDirection) {

        final int x = jumpNode.getX();
        final int y = jumpNode.getY();

        return hasForcedNeighborsAlongVerticalRightOfJumpNode(graph, jumpNode, yDirection, x, y)
                || hasForcedNeighborsAlongVerticalLeftOfJumpNode(graph, jumpNode, yDirection, x, y);
    }

    private boolean hasForcedNeighborsAlongVerticalRightOfJumpNode(
            final TiledGraph<N> graph,
            final N jumpNode,
            final int yDirection,
            final int x,
            final int y) {

        return doesGraphHaveConnectionIfInBounds(graph, jumpNode, x + 1, y + yDirection)
                && !doesGraphHaveConnectionIfInBounds(graph, jumpNode, x + 1, y);
    }

    private boolean hasForcedNeighborsAlongVerticalLeftOfJumpNode(
            final TiledGraph<N> graph,
            final N jumpNode,
            final int yDirection,
            final int x,
            final int y) {

        return doesGraphHaveConnectionIfInBounds(graph, jumpNode, x - 1, y + yDirection)
                && !doesGraphHaveConnectionIfInBounds(graph, jumpNode, x - 1, y);
    }

    private N getNextJumpPointToCheck(
            final TiledGraph<N> graph,
            final N sourceNode,
            final N jumpNode,
            final N destinationNode) {

        final int sourceNodeX = sourceNode.getX();
        final int sourceNodeY = sourceNode.getY();
        final int jumpNodeX = jumpNode.getX();
        final int jumpNodeY = jumpNode.getY();

        final int xDirection = (int)Math.signum(jumpNodeX - sourceNodeX);
        final int yDirection = (int)Math.signum(jumpNodeY - sourceNodeY);

        if (graph.isInBounds(jumpNodeX + xDirection, jumpNodeY + yDirection)) {
            return graph.getNode(jumpNodeX + xDirection, jumpNodeY + yDirection);
        } else {
            return null;
        }
    }

    private boolean doesGraphHaveConnectionIfInBounds(
            final TiledGraph<N> graph,
            final N node,
            final int x,
            final int y) {
        return graph.isInBounds(x, y) && graph.hasConnection(node, x, y);
    }

}
