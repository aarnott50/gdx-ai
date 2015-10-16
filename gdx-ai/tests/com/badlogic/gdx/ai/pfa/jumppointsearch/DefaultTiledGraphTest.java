package com.badlogic.gdx.ai.pfa.jumppointsearch;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.ai.pfa.jumppointsearch.JumpPointSearchTestUtils.GridNodeFake;

public class DefaultTiledGraphTest {

    private static GridNodeFake[][] TEST_GRID;

    private static final int TEST_GRID_WIDTH = 5;
    private static final int TEST_GRID_HEIGHT = 5;
    private static final boolean[][] TEST_GRID_PASSABLE_TILES =
            JumpPointSearchTestUtils.buildPassableTilesFromString(
                    ".....\n" +
                    "..#..\n" +
                    "..#..\n" +
                    "###..\n" +
                    ".....\n");

    @BeforeClass
    public static void initializeTestGrids() {
        TEST_GRID = JumpPointSearchTestUtils.buildTestGrid(TEST_GRID_WIDTH, TEST_GRID_HEIGHT, TEST_GRID_PASSABLE_TILES);
    }



    @Test(expected = IllegalArgumentException.class)
    public void createDefaultGridFormattedGraph_WithZeroWidth_ExpectedIllegalArgumentException() {
        new DefaultTiledGraph<GridNodeFake>(0, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createDefaultGridFormattedGraph_WithNegativeWidth_ExpectedIllegalArgumentException() {
        new DefaultTiledGraph<GridNodeFake>(-1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createDefaultGridFormattedGraph_WithZeroHeight_ExpectedIllegalArgumentException() {
        new DefaultTiledGraph<GridNodeFake>(10, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createDefaultGridFormattedGraph_WithNegativeHeight_ExpectedIllegalArgumentException() {
        new DefaultTiledGraph<GridNodeFake>(10, -1);
    }



    @Test(expected = IndexOutOfBoundsException.class)
    public void setNode_WithNegativeXCoordinate_ExpectedIndexOutOfBoundsException() {
        final DefaultTiledGraph<GridNodeFake> graph = new DefaultTiledGraph<>(10, 10);
        graph.setNode(-1, 0, new GridNodeFake());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void setNode_WithXCoordinatePastWidth_ExpectedIndexOutOfBoundsException() {
        final DefaultTiledGraph<GridNodeFake> graph = new DefaultTiledGraph<>(10, 10);
        graph.setNode(10, 0, new GridNodeFake());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void setNode_WithNegativeYCoordinate_ExpectedIndexOutOfBoundsException() {
        final DefaultTiledGraph<GridNodeFake> graph = new DefaultTiledGraph<>(10, 10);
        graph.setNode(0, -1, new GridNodeFake());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void setNode_WithYCoordinatePastHeight_ExpectedIndexOutOfBoundsException() {
        final DefaultTiledGraph<GridNodeFake> graph = new DefaultTiledGraph<>(10, 10);
        graph.setNode(0, 10, new GridNodeFake());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNode_WithNullNode_ExpectedIllegalArgumentException() {
        final DefaultTiledGraph<GridNodeFake> graph = new DefaultTiledGraph<>(10, 10);
        graph.setNode(0, 0, null);
    }



    @Test(expected = IndexOutOfBoundsException.class)
    public void getNode_WithNegativeXCoordinate_ExpectedIndexOutOfBoundsException() {
        final DefaultTiledGraph<GridNodeFake> graph = new DefaultTiledGraph<>(10, 10);
        graph.getNode(-1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getNode_WithXCoordinatePastWidth_ExpectedIndexOutOfBoundsException() {
        final DefaultTiledGraph<GridNodeFake> graph = new DefaultTiledGraph<>(10, 10);
        graph.getNode(10, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getNode_WithNegativeYCoordinate_ExpectedIndexOutOfBoundsException() {
        final DefaultTiledGraph<GridNodeFake> graph = new DefaultTiledGraph<>(10, 10);
        graph.getNode(0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getNode_WithYCoordinatePastHeight_ExpectedIndexOutOfBoundsException() {
        final DefaultTiledGraph<GridNodeFake> graph = new DefaultTiledGraph<>(10, 10);
        graph.getNode(0, 10);
    }



    @Test(expected = IndexOutOfBoundsException.class)
    public void hasConnection_WithNegativeXCoordinate_ExpectedIndexOutOfBoundsException() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        graph.hasConnection(new GridNodeFake(), -1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void hasConnection_WithXCoordinatePastWidth_ExpectedIndexOutOfBoundsException() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        graph.hasConnection(new GridNodeFake(), TEST_GRID_WIDTH, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void hasConnection_WithNegativeYCoordinate_ExpectedIndexOutOfBoundsException() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        graph.hasConnection(new GridNodeFake(), 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void hasConnection_WithYCoordinatePastHeight_ExpectedIndexOutOfBoundsException() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        graph.hasConnection(new GridNodeFake(), 0, TEST_GRID_HEIGHT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hasConnection_WithNullSourceNode_ExpectedIllegalArgumentException() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        graph.hasConnection(null, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hasConnection_WithNullDestinationNode_ExpectedIllegalArgumentException() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        graph.hasConnection(TEST_GRID[0][0], null);
    }



    @Test
    public void hasConnection_WhenConnectionExists_ExpectedTrue() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        Assert.assertTrue(graph.hasConnection(TEST_GRID[0][0], TEST_GRID[0][1]));
        Assert.assertTrue(graph.hasConnection(TEST_GRID[0][0], TEST_GRID[1][0]));
        Assert.assertTrue(graph.hasConnection(TEST_GRID[0][0], TEST_GRID[1][1]));

        Assert.assertTrue(graph.hasConnection(TEST_GRID[0][1], TEST_GRID[0][0]));
        Assert.assertTrue(graph.hasConnection(TEST_GRID[1][0], TEST_GRID[0][0]));
        Assert.assertTrue(graph.hasConnection(TEST_GRID[1][1], TEST_GRID[0][0]));
    }

    @Test
    public void hasConnection_WhenConnectionDoesNotExist_ExpectedFalse() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        Assert.assertFalse(graph.hasConnection(TEST_GRID[1][1], TEST_GRID[2][1]));
        Assert.assertFalse(graph.hasConnection(TEST_GRID[2][1], TEST_GRID[1][1]));
        Assert.assertFalse(graph.hasConnection(TEST_GRID[0][0], TEST_GRID[2][2]));
    }

    @Test
    public void hasConnection_WhenRequestingSameConnectionMultipleTimes_ExpectedConsistentResults() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        Assert.assertTrue(graph.hasConnection(TEST_GRID[0][0], TEST_GRID[0][1]));
        Assert.assertTrue(graph.hasConnection(TEST_GRID[0][0], TEST_GRID[0][1]));
        Assert.assertTrue(graph.hasConnection(TEST_GRID[0][0], TEST_GRID[0][1]));

        Assert.assertFalse(graph.hasConnection(TEST_GRID[0][0], TEST_GRID[2][2]));
        Assert.assertFalse(graph.hasConnection(TEST_GRID[0][0], TEST_GRID[2][2]));
        Assert.assertFalse(graph.hasConnection(TEST_GRID[0][0], TEST_GRID[2][2]));
    }

}
