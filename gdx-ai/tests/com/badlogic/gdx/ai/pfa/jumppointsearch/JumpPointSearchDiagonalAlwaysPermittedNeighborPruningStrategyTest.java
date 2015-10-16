package com.badlogic.gdx.ai.pfa.jumppointsearch;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.ai.pfa.jumppointsearch.JumpPointSearchTestUtils.GridNodeFake;
import com.badlogic.gdx.utils.Array;

public class JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategyTest {

    private static GridNodeFake[][] TEST_GRID;

    private static final int TEST_GRID_WIDTH = 15;
    private static final int TEST_GRID_HEIGHT = 6;
    private static final boolean[][] TEST_GRID_PASSABLE_TILES =
            JumpPointSearchTestUtils.buildPassableTilesFromString(
                    "...............\n" +
                    "..#.......###..\n" +
                    "...............\n" +
                    "#.#...#...###.#\n" +
                    "............#.#\n" +
                    "............#.#\n");


    private static GridNodeFake[][] DIAGONAL_TEST_GRID;

    private static final int DIAGONAL_TEST_GRID_WIDTH = 16;
    private static final int DIAGONAL_TEST_GRID_HEIGHT = 7;
    private static final boolean[][] DIAGONAL_TEST_GRID_PASSABLE_TILES =
            JumpPointSearchTestUtils.buildPassableTilesFromString(
                    "................\n" +
                    "..........#..#..\n" +
                    "..#...#...#..#..\n" +
                    ".....#.#...##...\n" +
                    "......#...#..#..\n" +
                    "..........#..#..\n" +
                    "................\n");

    @BeforeClass
    public static void initializeTestGrids() {
        TEST_GRID = JumpPointSearchTestUtils.buildTestGrid(TEST_GRID_WIDTH, TEST_GRID_HEIGHT, TEST_GRID_PASSABLE_TILES);
        DIAGONAL_TEST_GRID = JumpPointSearchTestUtils.buildTestGrid(DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT, DIAGONAL_TEST_GRID_PASSABLE_TILES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPrunedNeighbors_whenGraphIsNull_ExpectedIllegalArgumentException() {
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        strategy.getPrunedNeighbors(null, TEST_GRID[0][0], TEST_GRID[0][0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPrunedNeighbors_whenParentNodeIsSameAsNodeToCheck_ExpectedIllegalArgumentException() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        strategy.getPrunedNeighbors(graph, TEST_GRID[0][0], TEST_GRID[0][0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPrunedNeighbors_whenNodeIsNull_ExpectedIllegalArgumentException() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        strategy.getPrunedNeighbors(graph, null, null);
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsNull_ExpectedAllNeighborsReturned() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        final Array<GridNodeFake> neighbors_0_0 = strategy.getPrunedNeighbors(graph, TEST_GRID[0][0], null);
        Assert.assertEquals(3, neighbors_0_0.size);
        Assert.assertTrue(neighbors_0_0.contains(TEST_GRID[0][1], true));
        Assert.assertTrue(neighbors_0_0.contains(TEST_GRID[1][0], true));
        Assert.assertTrue(neighbors_0_0.contains(TEST_GRID[1][1], true));

        final Array<GridNodeFake> neighbors_1_2 = strategy.getPrunedNeighbors(graph, TEST_GRID[1][2], null);
        Assert.assertEquals(5, neighbors_1_2.size);
        Assert.assertTrue(neighbors_1_2.contains(TEST_GRID[0][2], true));
        Assert.assertTrue(neighbors_1_2.contains(TEST_GRID[0][1], true));
        Assert.assertTrue(neighbors_1_2.contains(TEST_GRID[1][1], true));
        Assert.assertTrue(neighbors_1_2.contains(TEST_GRID[2][2], true));
        Assert.assertTrue(neighbors_1_2.contains(TEST_GRID[1][3], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsAdjacentOnLeft_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * ...............0
         * ..#..PN...###..1
         * ...............2
         * #.#...#...###.#3
         * ............#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[6][1], TEST_GRID[5][1]);
        Assert.assertEquals(1, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[7][1], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsOnLeft_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * ...............0
         * ..#P..N...###..1
         * ...............2
         * #.#...#...###.#3
         * ............#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[6][1], TEST_GRID[3][1]);
        Assert.assertEquals(1, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[7][1], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsOnLeft_WithBlockedTileAbove_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * ...............0
         * ..#.......###..1
         * ...............2
         * #.#...#...###.#3
         * P.....N.....#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[6][4], TEST_GRID[0][4]);
        Assert.assertEquals(2, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[7][4], true));
        Assert.assertTrue(neighbors.contains(TEST_GRID[7][3], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsOnLeft_WithBlockedTileBelow_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * ...............0
         * ..#.......###..1
         * ...P..N........2
         * #.#...#...###.#3
         * ............#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[6][2], TEST_GRID[3][2]);
        Assert.assertEquals(2, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[7][2], true));
        Assert.assertTrue(neighbors.contains(TEST_GRID[7][3], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsOnLeft_WithBlockedTileAboveAndBelow_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * ...............0
         * ..#.......###..1
         * P.N............2
         * #.#...#...###.#3
         * ............#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[2][2], TEST_GRID[0][2]);
        Assert.assertEquals(3, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[3][1], true));
        Assert.assertTrue(neighbors.contains(TEST_GRID[3][2], true));
        Assert.assertTrue(neighbors.contains(TEST_GRID[3][3], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsOnLeft_WithNodeInCorridor_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X1234
         * ...............0
         * ..#.......###..1
         * P..........N...2
         * #.#...#...###.#3
         * ............#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[11][2], TEST_GRID[0][2]);
        Assert.assertEquals(1, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[12][2], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsAdjacentOnRight_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * ...............0
         * ..#...NP..###..1
         * ...............2
         * #.#...#...###.#3
         * ............#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[6][1], TEST_GRID[7][1]);
        Assert.assertEquals(1, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[5][1], true));
    }


    @Test
    public void getPrunedNeighbors_whenParentNodeIsOnRight_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * ...............0
         * ..#...N..P###..1
         * ...............2
         * #.#...#...###.#3
         * ............#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[6][1], TEST_GRID[9][1]);
        Assert.assertEquals(1, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[5][1], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsOnRight_WithBlockedTileAbove_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * ...............0
         * ..#.......###..1
         * ...............2
         * #.#...#...###.#3
         * ......N..P..#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[6][4], TEST_GRID[9][4]);
        Assert.assertEquals(2, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[5][4], true));
        Assert.assertTrue(neighbors.contains(TEST_GRID[5][3], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsOnRight_WithBlockedTileBelow_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * ...............0
         * ..#.......###..1
         * ......N..P.....2
         * #.#...#...###.#3
         * ............#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[6][2], TEST_GRID[9][2]);
        Assert.assertEquals(2, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[5][2], true));
        Assert.assertTrue(neighbors.contains(TEST_GRID[5][3], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsOnRight_WithBlockedTileAboveAndBelow_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * ...............0
         * ..#.......###..1
         * ..N......P.....2
         * #.#...#...###.#3
         * ............#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[2][2], TEST_GRID[9][2]);
        Assert.assertEquals(3, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[1][1], true));
        Assert.assertTrue(neighbors.contains(TEST_GRID[1][2], true));
        Assert.assertTrue(neighbors.contains(TEST_GRID[1][3], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsOnRight_WithNodeInCorridor_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X1234
         * ...............0
         * ..#.......###..1
         * ...........N..P2
         * #.#...#...###.#3
         * ............#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[11][2], TEST_GRID[14][2]);
        Assert.assertEquals(1, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[10][2], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsAdjacentAbove_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * ...............0
         * ..#.....P.###..1
         * ........N......2
         * #.#...#...###.#3
         * ............#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[8][2], TEST_GRID[8][1]);
        Assert.assertEquals(1, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[8][3], true));
    }


    @Test
    public void getPrunedNeighbors_whenParentNodeIsAbove_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * ........P......0
         * ..#.......###..1
         * ........N......2
         * #.#...#...###.#3
         * ............#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[8][2], TEST_GRID[8][0]);
        Assert.assertEquals(1, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[8][3], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsAbove_WithBlockedTileLeft_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * .......P.......0
         * ..#.......###..1
         * ...............2
         * #.#...#N..###.#3
         * ............#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[7][3], TEST_GRID[7][0]);
        Assert.assertEquals(2, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[6][4], true));
        Assert.assertTrue(neighbors.contains(TEST_GRID[7][4], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsAbove_WithBlockedTileRight_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * .....P.........0
         * ..#.......###..1
         * ...............2
         * #.#..N#...###.#3
         * ............#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[5][3], TEST_GRID[5][0]);
        Assert.assertEquals(2, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[5][4], true));
        Assert.assertTrue(neighbors.contains(TEST_GRID[6][4], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsAbove_WithBlockedTilesLeftAndRight_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * .P.............0
         * ..#.......###..1
         * ...............2
         * #N#...#...###.#3
         * ............#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[1][3], TEST_GRID[1][0]);
        Assert.assertEquals(3, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[0][4], true));
        Assert.assertTrue(neighbors.contains(TEST_GRID[1][4], true));
        Assert.assertTrue(neighbors.contains(TEST_GRID[2][4], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsAbove_WithNodeInCorridor_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X1234
         * .............P.0
         * ..#.......###..1
         * ...............2
         * #.#...#...###.#3
         * ............#N#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[13][4], TEST_GRID[13][0]);
        Assert.assertEquals(1, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[13][5], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsAdjacentBelow_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * ...............0
         * ..#.......###..1
         * ........N......2
         * #.#...#.P.###.#3
         * ............#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[8][2], TEST_GRID[8][3]);
        Assert.assertEquals(1, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[8][1], true));
    }


    @Test
    public void getPrunedNeighbors_whenParentNodeIsBelow_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * ...............0
         * ..#.......###..1
         * ........N......2
         * #.#...#...###.#3
         * ........P...#.#4
         * ............#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[8][2], TEST_GRID[8][4]);
        Assert.assertEquals(1, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[8][1], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsBelow_WithBlockedTileLeft_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * ...............0
         * ..#.......###..1
         * ...............2
         * #.#...#N..###.#3
         * ............#.#4
         * .......P....#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[7][3], TEST_GRID[7][5]);
        Assert.assertEquals(2, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[6][2], true));
        Assert.assertTrue(neighbors.contains(TEST_GRID[7][2], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsBelow_WithBlockedTileRight_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * ...............0
         * ..#.......###..1
         * ...............2
         * #.#..N#...###.#3
         * ............#.#4
         * .....P......#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[5][3], TEST_GRID[5][5]);
        Assert.assertEquals(2, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[5][2], true));
        Assert.assertTrue(neighbors.contains(TEST_GRID[6][2], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsBelow_WithBlockedTilesLeftAndRight_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789
         * ...............0
         * ..#.......###..1
         * ...............2
         * #N#...#...###.#3
         * ............#.#4
         * .P..........#.#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[1][3], TEST_GRID[1][5]);
        Assert.assertEquals(3, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[0][2], true));
        Assert.assertTrue(neighbors.contains(TEST_GRID[1][2], true));
        Assert.assertTrue(neighbors.contains(TEST_GRID[2][2], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsBelow_WithNodeInCorridor_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X1234
         * ...............0
         * ..#.......###..1
         * ...............2
         * #.#...#...###.#3
         * ............#N#4
         * ............#P#5
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, TEST_GRID[13][4], TEST_GRID[13][5]);
        Assert.assertEquals(1, neighbors.size);
        Assert.assertTrue(neighbors.contains(TEST_GRID[13][3], true));
    }



    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomLeft_Adjacent_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..........#..#..1
         * ..#...#...#..#..2
         * .....#.#...##...3
         * ......#...#..#..4
         * .N........#..#..5
         * P...............6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[1][5], DIAGONAL_TEST_GRID[0][6]);
        Assert.assertEquals(3, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[1][4], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[2][4], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[2][5], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomLeft_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..........#..#..1
         * ..#...#...#..#..2
         * .....#.#...##...3
         * ..N...#...#..#..4
         * ..........#..#..5
         * P...............6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[2][4], DIAGONAL_TEST_GRID[0][6]);
        Assert.assertEquals(3, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[2][3], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[3][3], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[3][4], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomLeft_RightBlocked_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..........#..#..1
         * .N#...#...#..#..2
         * .....#.#...##...3
         * ......#...#..#..4
         * ..........#..#..5
         * P...............6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[1][2], DIAGONAL_TEST_GRID[0][6]);
        Assert.assertEquals(2, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[1][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[2][1], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomLeft_TopBlocked_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..........#..#..1
         * ..#...#...#..#..2
         * ..N..#.#...##...3
         * ......#...#..#..4
         * ..........#..#..5
         * P...............6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[2][3], DIAGONAL_TEST_GRID[0][6]);
        Assert.assertEquals(2, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[3][2], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[3][3], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomLeft_TopAndRightBlocked_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..........#..#..1
         * ..#...#...#..#..2
         * .....#.#...##...3
         * .....N#...#..#..4
         * ..........#..#..5
         * P...............6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[5][4], DIAGONAL_TEST_GRID[0][6]);
        Assert.assertEquals(1, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[6][3], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomLeft_BottomBlocked_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..N.......#..#..1
         * ..#...#...#..#..2
         * .....#.#...##...3
         * ......#...#..#..4
         * ..........#..#..5
         * P...............6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[2][1], DIAGONAL_TEST_GRID[0][6]);
        Assert.assertEquals(4, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[2][0], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[3][0], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[3][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[3][2], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomLeft_LeftBlocked_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..........#..#..1
         * ..#N..#...#..#..2
         * .....#.#...##...3
         * ......#...#..#..4
         * ..........#..#..5
         * P...............6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[3][2], DIAGONAL_TEST_GRID[0][6]);
        Assert.assertEquals(4, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[2][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[3][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[4][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[4][2], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomLeft_BottomAndLeftBlocked_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..........#..#..1
         * ..#...#N..#..#..2
         * .....#.#...##...3
         * ......#...#..#..4
         * ..........#..#..5
         * P...............6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[7][2], DIAGONAL_TEST_GRID[0][6]);
        Assert.assertEquals(5, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[6][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[7][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[8][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[8][2], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[8][3], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomLeft_BottomAndLeftBlocked_ExtraLocationsAlsoBlocked_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..........#..#..1
         * ..#...#...#N.#..2
         * .....#.#...##...3
         * ......#...#..#..4
         * ..........#..#..5
         * P...............6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[11][2], DIAGONAL_TEST_GRID[0][6]);
        Assert.assertEquals(3, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[11][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[12][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[12][2], true));
    }




    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomRight_Adjacent_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..........#..#..1
         * ..#...#...#..#..2
         * .....#.#...##...3
         * ......#...#..#..4
         * .N........#..#..5
         * ..P.............6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[1][5], DIAGONAL_TEST_GRID[2][6]);
        Assert.assertEquals(3, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[1][4], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[0][4], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[0][5], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomRight_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..........#..#..1
         * ..#...#...#..#..2
         * .....#.#...##...3
         * ..N...#...#..#..4
         * ..........#..#..5
         * ...............P6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[2][4], DIAGONAL_TEST_GRID[15][6]);
        Assert.assertEquals(3, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[2][3], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[1][3], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[1][4], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomRight_RightBlocked_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..........#..#..1
         * .N#...#...#..#..2
         * .....#.#...##...3
         * ......#...#..#..4
         * ..........#..#..5
         * ...............P6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[1][2], DIAGONAL_TEST_GRID[15][6]);
        Assert.assertEquals(4, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[1][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[2][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[0][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[0][2], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomRight_TopBlocked_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..........#..#..1
         * ..#...#...#..#..2
         * ..N..#.#...##...3
         * ......#...#..#..4
         * ..........#..#..5
         * ...............P6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[2][3], DIAGONAL_TEST_GRID[15][6]);
        Assert.assertEquals(2, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[1][2], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[1][3], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomRight_TopAndLeftBlocked_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..........#..#..1
         * ..#...#...#..#..2
         * .....#.#...##...3
         * ......#N..#..#..4
         * ..........#..#..5
         * ...............P6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[7][4], DIAGONAL_TEST_GRID[15][6]);
        Assert.assertEquals(1, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[6][3], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomRight_BottomBlocked_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..N.......#..#..1
         * ..#...#...#..#..2
         * .....#.#...##...3
         * ......#...#..#..4
         * ..........#..#..5
         * ...............P6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[2][1], DIAGONAL_TEST_GRID[15][6]);
        Assert.assertEquals(4, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[2][0], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[1][0], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[1][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[1][2], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomRight_LeftBlocked_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..........#..#..1
         * ..#N..#...#..#..2
         * .....#.#...##...3
         * ......#...#..#..4
         * ..........#..#..5
         * ...............P6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[3][2], DIAGONAL_TEST_GRID[15][6]);
        Assert.assertEquals(2, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[2][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[3][1], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomRight_BottomAndRightBlocked_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..........#..#..1
         * ..#..N#...#..#..2
         * .....#.#...##...3
         * ......#...#..#..4
         * ..........#..#..5
         * ...............P6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[5][2], DIAGONAL_TEST_GRID[15][6]);
        Assert.assertEquals(5, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[4][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[5][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[6][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[4][2], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[4][3], true));
    }

    @Test
    public void getPrunedNeighbors_whenParentNodeIsDiagonallyLocated_BottomRight_BottomAndLeftBlocked_ExtraLocationsAlsoBlocked_ExpectedNeighborsPrunedCorrectly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(DIAGONAL_TEST_GRID, DIAGONAL_TEST_GRID_WIDTH, DIAGONAL_TEST_GRID_HEIGHT);
        final JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedNeighborPruningStrategy<>();

        /*
         * 0123456789X12345
         * ................0
         * ..........#..#..1
         * ..#...#...#.N#..2
         * .....#.#...##...3
         * ......#...#..#..4
         * ..........#..#..5
         * ...............P6
         */
        final Array<GridNodeFake> neighbors = strategy.getPrunedNeighbors(graph, DIAGONAL_TEST_GRID[12][2], DIAGONAL_TEST_GRID[15][6]);
        Assert.assertEquals(3, neighbors.size);
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[11][1], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[11][2], true));
        Assert.assertTrue(neighbors.contains(DIAGONAL_TEST_GRID[12][1], true));
    }

}
