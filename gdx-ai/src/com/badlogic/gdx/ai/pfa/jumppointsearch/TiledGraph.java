package com.badlogic.gdx.ai.pfa.jumppointsearch;

import com.badlogic.gdx.ai.pfa.Graph;

public interface TiledGraph<N extends TiledNode<N>> extends Graph<N> {

    N getNode(int x, int y);

    boolean hasConnection(N sourceNode, N destinationNode);
    boolean hasConnection(N sourceNode, int x, int y);

    boolean isInBounds(int x, int y);

    int getWidth();
    int getHeight();

}
