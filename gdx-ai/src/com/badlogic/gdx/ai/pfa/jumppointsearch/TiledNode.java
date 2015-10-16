package com.badlogic.gdx.ai.pfa.jumppointsearch;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

public interface TiledNode<N extends TiledNode<N>> {

    int getX();
    int getY();

    /** Returns an array of {@link Connection connections} outgoing from this {@code IndexedNode}. */
    Array<Connection<N>> getConnections();

}
