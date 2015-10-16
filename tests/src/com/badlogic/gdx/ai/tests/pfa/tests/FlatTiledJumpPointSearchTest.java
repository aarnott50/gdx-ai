package com.badlogic.gdx.ai.tests.pfa.tests;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.pfa.jumppointsearch.JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy;
import com.badlogic.gdx.ai.pfa.jumppointsearch.JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy;
import com.badlogic.gdx.ai.pfa.jumppointsearch.JumpPointSearchJumpingStrategy;
import com.badlogic.gdx.ai.pfa.jumppointsearch.JumpPointSearchNodeNeighborPruningStrategy;
import com.badlogic.gdx.ai.pfa.jumppointsearch.JumpPointSearchPathFinder;
import com.badlogic.gdx.ai.tests.PathFinderTests;
import com.badlogic.gdx.ai.tests.pfa.PathFinderTestBase;
import com.badlogic.gdx.ai.tests.pfa.tests.tiled.TiledManhattanDistance;
import com.badlogic.gdx.ai.tests.pfa.tests.tiled.TiledSmoothableGraphPath;
import com.badlogic.gdx.ai.tests.pfa.tests.tiled.flat.DiagonallyConnectedFlatTiledGraph;
import com.badlogic.gdx.ai.tests.pfa.tests.tiled.flat.FlatTiledGraph;
import com.badlogic.gdx.ai.tests.pfa.tests.tiled.flat.FlatTiledNode;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class FlatTiledJumpPointSearchTest extends PathFinderTestBase {

    final static float width = 8; // 5; // 10;

    private ShapeRenderer renderer;
    private Vector3 tmpUnprojection = new Vector3();

    private int lastScreenX;
    private int lastScreenY;
    private int lastEndTileX;
    private int lastEndTileY;
    private int startTileX;
    private int startTileY;

    private DiagonallyConnectedFlatTiledGraph worldMap;
    private TiledSmoothableGraphPath<FlatTiledNode> path;
    private TiledSmoothableGraphPath<FlatTiledNode> jumpPointPath;
    private TiledManhattanDistance<FlatTiledNode> heuristic;
    private JumpPointSearchNodeNeighborPruningStrategy<FlatTiledNode> neighborPruningStrategy;
    private JumpPointSearchJumpingStrategy<FlatTiledNode> jumpingStrategy;
    private JumpPointSearchPathFinder<FlatTiledNode> pathFinder;

    private CheckBox checkMetrics;

    public FlatTiledJumpPointSearchTest (final PathFinderTests container) {
        super(container, "Flat Tiled Jump Point Search");
    }

    @Override
    public void create (final Table table) {
        lastEndTileX = -1;
        lastEndTileY = -1;
        startTileX = 1;
        startTileY = 1;

        // Create the map
        worldMap = new DiagonallyConnectedFlatTiledGraph();
        final int roomCount = MathUtils.random(80, 150);// 100, 260);//70, 120);
        final int roomMinSize = 3;
        final int roomMaxSize = 15;
        final int squashIterations = 100;
        worldMap.init(roomCount, roomMinSize, roomMaxSize, squashIterations);

        path = new TiledSmoothableGraphPath<>();
        jumpPointPath = new TiledSmoothableGraphPath<>();
        heuristic = new TiledManhattanDistance<>();
        neighborPruningStrategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();
        jumpingStrategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();
        pathFinder = new JumpPointSearchPathFinder<>(worldMap, neighborPruningStrategy, jumpingStrategy);

        renderer = new ShapeRenderer();
        inputProcessor = new TiledAStarInputProcessor(this);

        final Table detailTable = new Table(container.skin);

//        detailTable.row();
//        checkMetrics = new CheckBox("Calculate [RED]M[]etrics", container.skin);
//        checkMetrics.setChecked(pathFinder.metrics != null);
//        checkMetrics.addListener(new ChangeListener() {
//            @Override
//            public void changed (final ChangeEvent event, final Actor actor) {
//                final CheckBox checkBox = (CheckBox)event.getListenerActor();
//                pathFinder.metrics = checkBox.isChecked() ? new Metrics() : null;
//                updatePath(true);
//            }
//        });
//        detailTable.add(checkMetrics);

        detailWindow = createDetailWindow(detailTable);
    }

    @Override
    public void render () {
        renderer.begin(ShapeType.Filled);
        for (int x = 0; x < FlatTiledGraph.sizeX; x++) {
            for (int y = 0; y < FlatTiledGraph.sizeY; y++) {
                switch (worldMap.getNode(x, y).type) {
                case FlatTiledNode.TILE_FLOOR:
                    renderer.setColor(Color.WHITE);
                    break;
                case FlatTiledNode.TILE_WALL:
                    renderer.setColor(Color.GRAY);
                    break;
                default:
                    renderer.setColor(Color.BLACK);
                    break;
                }
                renderer.rect(x * width, y * width, width, width);
            }
        }

        renderer.setColor(Color.RED);
        final int nodeCount = path.getCount();
        for (int i = 0; i < nodeCount; i++) {
            final FlatTiledNode node = path.nodes.get(i);
            renderer.rect(node.x * width, node.y * width, width, width);
        }

        renderer.setColor(Color.GREEN);
        final int jumpNodeCount = jumpPointPath.getCount();
        for (int i = 0; i < jumpNodeCount; i++) {
            final FlatTiledNode jumpPointNode = jumpPointPath.nodes.get(i);
            renderer.rect(jumpPointNode.x * width, jumpPointNode.y * width, width, width);
        }

        renderer.end();
    }

    @Override
    public void dispose () {
        renderer.dispose();

        worldMap = null;
        path = null;
        jumpPointPath = null;
        heuristic = null;
        pathFinder = null;
    }

    public Camera getCamera () {
        return container.stage.getViewport().getCamera();
    }

    private void updatePath (final boolean forceUpdate) {
        getCamera().unproject(tmpUnprojection.set(lastScreenX, lastScreenY, 0));
        final int tileX = (int)(tmpUnprojection.x / width);
        final int tileY = (int)(tmpUnprojection.y / width);
        if (forceUpdate || tileX != lastEndTileX || tileY != lastEndTileY) {
            final FlatTiledNode startNode = worldMap.getNode(startTileX, startTileY);
            FlatTiledNode endNode = worldMap.getNode(tileX, tileY);
            if (forceUpdate || endNode.type == FlatTiledNode.TILE_FLOOR) {
                if (endNode.type == FlatTiledNode.TILE_FLOOR) {
                    lastEndTileX = tileX;
                    lastEndTileY = tileY;
                } else {
                    endNode = worldMap.getNode(lastEndTileX, lastEndTileY);
                }
                path.clear();
                jumpPointPath.clear();
                worldMap.startNode = startNode;
//                final long startTime = nanoTime();
                pathFinder.searchNodePath(startNode, endNode, heuristic, path);
                pathFinder.searchJumpPoints(startNode, endNode, heuristic, jumpPointPath);
//                if (pathFinder.metrics != null) {
//                    final float elapsed = (TimeUtils.nanoTime() - startTime) / 1000000f;
//                    System.out.println("----------------- Indexed A* Path Finder Metrics -----------------");
//                    System.out.println("Visited nodes................... = " + pathFinder.metrics.visitedNodes);
//                    System.out.println("Open list additions............. = " + pathFinder.metrics.openListAdditions);
//                    System.out.println("Open list peak.................. = " + pathFinder.metrics.openListPeak);
//                    System.out.println("Path finding elapsed time (ms).. = " + elapsed);
//                }
            }
        }
    }

//    private long nanoTime () {
//        return pathFinder.metrics == null ? 0 : TimeUtils.nanoTime();
//    }

    /** An {@link InputProcessor} that allows you to define a path to find.
     *
     * @autor davebaol */
    static class TiledAStarInputProcessor extends InputAdapter {
        FlatTiledJumpPointSearchTest test;

        public TiledAStarInputProcessor (final FlatTiledJumpPointSearchTest test) {
            this.test = test;
        }

        @Override
        public boolean keyTyped (final char character) {
            switch (character) {
            case 'm':
            case 'M':
                test.checkMetrics.toggle();
                break;
            }
            return true;
        }

        @Override
        public boolean touchUp (final int screenX, final int screenY, final int pointer, final int button) {
            test.getCamera().unproject(test.tmpUnprojection.set(screenX, screenY, 0));
            final int tileX = (int)(test.tmpUnprojection.x / width);
            final int tileY = (int)(test.tmpUnprojection.y / width);
            final FlatTiledNode startNode = test.worldMap.getNode(tileX, tileY);
            if (startNode.type == FlatTiledNode.TILE_FLOOR) {
                test.startTileX = tileX;
                test.startTileY = tileY;
                test.updatePath(true);
            }
            return true;
        }

        @Override
        public boolean mouseMoved (final int screenX, final int screenY) {
            test.lastScreenX = screenX;
            test.lastScreenY = screenY;
            test.updatePath(false);
            return true;
        }
    }

}
