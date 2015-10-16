package com.badlogic.gdx.ai.pfa.jumppointsearch;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathFinderRequest;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BinaryHeap;

/**
 * A fully implemented {@code PathFinder} that will perform pathfinding using the jump point search pathfinding
 * algorithm.
 * <p>
 * This implementation provides pluggable strategies, {@code JumpPointSearchNodeNeighborPruningStrategy} and
 * {@code JumpPointSearchJumpingStrategy}, to determine how to prune neighbors and how to perform jumps.
 * <p>
 * Jump point search has been empirically shown to be very effective for maps with a grid-based layout with
 * open areas (such as dungeons with rooms). In many cases, it has been shown to perform at an order of magnitude
 * faster than traditional A* approaches. It is optimal, like A*, requires no pre-processing, and no significant
 * memory overhead.
 * <p>
 * <h3>Citation</h3>
 * Harabor, D. D., & Grastien, A. (2011, April). Online Graph Pruning for Pathfinding On Grid Maps. In AAAI.
 * <p>
 * Accessed at <a href="http://users.cecs.anu.edu.au/~dharabor/data/papers/harabor-grastien-aaai11.pdf">http://users.cecs.anu.edu.au/~dharabor/data/papers/harabor-grastien-aaai11.pdf</a>.
 *
 */
public class JumpPointSearchPathFinder<N extends TiledNode<N>> implements JumpPointPathFinder<N> {

    private final TiledGraph<N> graph;
    private final JumpPointSearchNodeNeighborPruningStrategy<N> neighborPruningStrategy;
    private final JumpPointSearchJumpingStrategy<N> jumpingStrategy;
    private final BinaryHeap<NodeRecord<N>> openList = new BinaryHeap<>();
    private final Map<N, NodeRecord<N>> nodeRecordPool = new HashMap<>();


    private int activeSearchId = 0;


    public JumpPointSearchPathFinder(
            final TiledGraph<N> graph,
            final JumpPointSearchNodeNeighborPruningStrategy<N> neighborPruningStrategy,
            final JumpPointSearchJumpingStrategy<N> jumpingStrategy) {
        this.graph = graph;
        this.neighborPruningStrategy = neighborPruningStrategy;
        this.jumpingStrategy = jumpingStrategy;
    }

    @Override
    public boolean searchConnectionPath(
            final N startNode,
            final N endNode,
            final Heuristic<N> heuristic,
            final GraphPath<Connection<N>> outPath) {

        initializeSearch(startNode, endNode, heuristic);
        search(startNode, endNode, heuristic);

        return backtrackConnectionPath(getNodeRecord(endNode), outPath);
    }

    @Override
    public boolean searchNodePath(
            final N startNode,
            final N endNode,
            final Heuristic<N> heuristic,
            final GraphPath<N> outPath) {

        initializeSearch(startNode, endNode, heuristic);
        search(startNode, endNode, heuristic);

        return backtrackNodePath(getNodeRecord(endNode), outPath);
    }

    @Override
    public boolean searchJumpPoints(
            final N startNode,
            final N endNode,
            final Heuristic<N> heuristic,
            final GraphPath<N> outPath) {

        initializeSearch(startNode, endNode, heuristic);
        search(startNode, endNode, heuristic);

        return backtrackJumpPoints(getNodeRecord(endNode), outPath);
    }

    @Override
    public boolean search(final PathFinderRequest<N> request, final long timeToRun) {
        // TODO Auto-generated method stub
        return false;
    }


    protected void initializeSearch(final N startNode, final N endNode, final Heuristic<N> heuristic) {
        openList.clear();
        activeSearchId++;

        final NodeRecord<N> startRecord = getNodeRecord(startNode);

        addToOpenList(startRecord, heuristic.estimate(startNode, endNode));
    }

    protected void search(final N startNode, final N endNode, final Heuristic<N> heuristic) {
        while (openList.size > 0) {
            final NodeRecord<N> activeNodeRecord = openList.pop();
            activeNodeRecord.category = NodeRecordCategory.CLOSED;

            if (activeNodeRecord.node == endNode) {
                return;
            }

            identifySuccessors(activeNodeRecord, heuristic, endNode);
        }
    }


    private void identifySuccessors(final NodeRecord<N> activeNodeRecord, final Heuristic<N> heuristic, final N endNode) {
        final Array<N> neighbors = neighborPruningStrategy.getPrunedNeighbors(graph, activeNodeRecord.node, activeNodeRecord.parentNode);

        for (final N neighbor : neighbors) {
            final N jumpPoint = jumpingStrategy.getJumpPoint(graph, activeNodeRecord.node, neighbor, endNode);
            if (jumpPoint != null) {
                final NodeRecord<N> jumpNodeRecord = getNodeRecord(jumpPoint);
                if (jumpNodeRecord.category != NodeRecordCategory.CLOSED) {
                    final float jumpNodeDistance = heuristic.estimate(activeNodeRecord.node, jumpPoint);
                    final float nextTotalCost = activeNodeRecord.costSoFar + jumpNodeDistance;

                    if (jumpNodeRecord.category == NodeRecordCategory.UNVISITED) {
                        jumpNodeRecord.costSoFar = nextTotalCost;
                        jumpNodeRecord.parentNode = activeNodeRecord.node;
                        addToOpenList(jumpNodeRecord, jumpNodeRecord.costSoFar + heuristic.estimate(jumpPoint, endNode));
                    } else if (jumpNodeRecord.category == NodeRecordCategory.OPEN && nextTotalCost < jumpNodeRecord.costSoFar) {
                        jumpNodeRecord.costSoFar = nextTotalCost;
                        jumpNodeRecord.parentNode = activeNodeRecord.node;
                        openList.setValue(jumpNodeRecord, jumpNodeRecord.costSoFar + heuristic.estimate(jumpPoint, endNode));
                    }
                }
            }
        }
    }

    protected void addToOpenList(final NodeRecord<N> nodeRecord, final float estimatedTotalCost) {
        openList.add(nodeRecord, estimatedTotalCost);
        nodeRecord.category = NodeRecordCategory.OPEN;
    }



    private boolean backtrackConnectionPath(
            final NodeRecord<N> endNodeRecord,
            final GraphPath<Connection<N>> outPath) {

        if (endNodeRecord.parentNode == null) {
            return false;
        }

        NodeRecord<N> nodeRecordToBacktrackFrom = endNodeRecord;

        final Deque<Connection<N>> backtrackedNodeConnections = new LinkedList<>();
        while (nodeRecordToBacktrackFrom.parentNode != null) {
            backtrackedNodeConnections.push(getConnectionIfExists(nodeRecordToBacktrackFrom.parentNode, nodeRecordToBacktrackFrom.node));
            nodeRecordToBacktrackFrom = getNodeRecord(nodeRecordToBacktrackFrom.parentNode);
        }

        for (final Connection<N> backtrackedNodeConnection : backtrackedNodeConnections) {
            outPath.add(backtrackedNodeConnection);
        }

        return true;
    }

    private Connection<N> getConnectionIfExists(final N sourceNode, final N destinationNode) {
        for (final Connection<N> connection : sourceNode.getConnections()) {
            if (connection.getToNode() == destinationNode) {
                return connection;
            }
        }
        return null;
    }

    private boolean backtrackNodePath(final NodeRecord<N> endNodeRecord, final GraphPath<N> outPath) {
        if (endNodeRecord.parentNode == null) {
            return false;
        }

        NodeRecord<N> nodeRecordToBacktrackFrom = endNodeRecord;

        final Deque<N> backtrackedNodes = new LinkedList<>();
        backtrackedNodes.push(nodeRecordToBacktrackFrom.node);
        while (nodeRecordToBacktrackFrom.parentNode != null) {
            for(final N intermediateNode : getIntermediateNodes(nodeRecordToBacktrackFrom.node, nodeRecordToBacktrackFrom.parentNode)) {
                backtrackedNodes.push(intermediateNode);
            }
            backtrackedNodes.push(nodeRecordToBacktrackFrom.parentNode);
            nodeRecordToBacktrackFrom = getNodeRecord(nodeRecordToBacktrackFrom.parentNode);
        }

        for (final N backtrackedNode : backtrackedNodes) {
            outPath.add(backtrackedNode);
        }

        return true;
    }

    private List<N> getIntermediateNodes(final N node, final N parentNode) {
        final List<N> intermediateNodes = new LinkedList<>();

        final int xDirection = (int)Math.signum(node.getX() - parentNode.getX());
        final int yDirection = (int)Math.signum(node.getY() - parentNode.getY());


        N intermediateNode = graph.getNode(node.getX() - xDirection, node.getY() - yDirection);
        while(intermediateNode != parentNode) {
            intermediateNodes.add(intermediateNode);
            intermediateNode = graph.getNode(intermediateNode.getX() - xDirection, intermediateNode.getY() - yDirection);
        }

        return intermediateNodes;
    }

    private boolean backtrackJumpPoints(final NodeRecord<N> endNodeRecord, final GraphPath<N> outPath) {
        if (endNodeRecord.parentNode == null) {
            return false;
        }

        NodeRecord<N> nodeRecordToBacktrackFrom = endNodeRecord;

        final Deque<N> backtrackedNodes = new LinkedList<>();
        backtrackedNodes.push(nodeRecordToBacktrackFrom.node);
        while (nodeRecordToBacktrackFrom.parentNode != null) {
            backtrackedNodes.push(nodeRecordToBacktrackFrom.parentNode);
            nodeRecordToBacktrackFrom = getNodeRecord(nodeRecordToBacktrackFrom.parentNode);
        }

        for (final N backtrackedNode : backtrackedNodes) {
            outPath.add(backtrackedNode);
        }

        return true;
    }




    private NodeRecord<N> getNodeRecord(final N node) {
        NodeRecord<N> nodeRecord = nodeRecordPool.get(node);

        if (nodeRecord == null) {
            nodeRecord = new NodeRecord<>(node);
            nodeRecord.searchId = activeSearchId;

            nodeRecordPool.put(node, nodeRecord);
        } else if (nodeRecord != null && nodeRecord.searchId != activeSearchId) {
            nodeRecord.reset();
            nodeRecord.searchId = activeSearchId;
        }

        return nodeRecord;
    }


    private static enum NodeRecordCategory {
        UNVISITED,
        OPEN,
        CLOSED
    }


    private static class NodeRecord<N extends TiledNode<N>> extends BinaryHeap.Node {
        int searchId;

        final N node;
        N parentNode;

        NodeRecordCategory category;
        float costSoFar;

        /** Creates a {@code NodeRecord}. */
        public NodeRecord (final N node) {
            super(0);
            this.node = node;
            this.reset();
        }

        public void reset() {
            parentNode = null;
            category = NodeRecordCategory.UNVISITED;
            costSoFar = 0f;
        }

        @Override
        public String toString() {
            return "NodeRecord [node=" + node +
                    ", parent=" + parentNode + ", category=" + category +
                    ", costSoFar=" + costSoFar + "]";
        }

    }


}
