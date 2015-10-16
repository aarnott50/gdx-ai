package com.badlogic.gdx.ai.pfa.jumppointsearch;

import com.badlogic.gdx.utils.Array;

/**
 * Classes implementing this interface determine what neighbor nodes are pruned when considering a jump from a parent
 * node to a jump node.
 *
 * Any implementation of this interface should follow the pruning requirements defined by the jump point search
 * algorithm description.
 *
 * @param <N> Type of node extending {@code GridNode}
 */
public interface JumpPointSearchNodeNeighborPruningStrategy<N extends TiledNode<N>> {

    /**
     * Returns a list of pruned neighbors for a node, given its parent node (if any).
     */
    Array<N> getPrunedNeighbors(TiledGraph<N> graph, N node, N parentNode);

}
