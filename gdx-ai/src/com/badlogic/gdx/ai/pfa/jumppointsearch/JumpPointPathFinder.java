package com.badlogic.gdx.ai.pfa.jumppointsearch;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathFinder;

public interface JumpPointPathFinder<N> extends PathFinder<N> {

    //TODO: Docs
    public boolean searchJumpPoints (N startNode, N endNode, Heuristic<N> heuristic, GraphPath<N> outPath);

}
