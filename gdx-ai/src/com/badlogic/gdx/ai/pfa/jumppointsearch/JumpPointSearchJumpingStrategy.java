package com.badlogic.gdx.ai.pfa.jumppointsearch;

/**
 * Search for a jump point looking in the direction from the source node to the jump node. This process should check
 * recursively in this direction until a proper jump point is found or if the destination node is found first. The
 * strategy should implement the jump portion as described in the jump point search algorithm.
 */
public interface JumpPointSearchJumpingStrategy<N extends TiledNode<N>> {

    /**
     * Recursively finds the next jump point when starting at the provided source node, moving in the direction of
     * the jump node, and attempting to end up at the destination node.
     */
    N getJumpPoint(TiledGraph<N> graph, N sourceNode, N jumpNode, N destinationNode);

}
