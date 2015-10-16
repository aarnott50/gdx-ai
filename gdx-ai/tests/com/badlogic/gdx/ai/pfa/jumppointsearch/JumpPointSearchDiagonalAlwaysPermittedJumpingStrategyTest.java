package com.badlogic.gdx.ai.pfa.jumppointsearch;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.ai.pfa.jumppointsearch.JumpPointSearchTestUtils.GridNodeFake;

public class JumpPointSearchDiagonalAlwaysPermittedJumpingStrategyTest {

    private static GridNodeFake[][] TEST_GRID;

    private static final int TEST_GRID_WIDTH = 24;
    private static final int TEST_GRID_HEIGHT = 15;
    /*
     * Comment template:
     *
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#.............. 0
     *  .........#..#.#......... 1
     *  .........#............#. 2
     *  .........#.............. 3
     *  .........#............#. 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     *
     *  In the comments for the tests:
     *
     *  S = source node
     *  J = initial jump node
     *  D = destination node
     *  X = expected resulting jump node
     *  F = forced neighbor
     *
     */
    private static final boolean[][] TEST_GRID_PASSABLE_TILES =
            JumpPointSearchTestUtils.buildPassableTilesFromString(
                    ".........#..............\n" +
                    ".........#..#.#.........\n" +
                    ".........#............#.\n" +
                    ".........#..............\n" +
                    ".........#............#.\n" +
                    "##########.......#.#....\n" +
                    "........................\n" +
                    "........................\n" +
                    "..#.....................\n" +
                    "..#.....................\n" +
                    "........................\n" +
                    "........................\n" +
                    "........................\n" +
                    "...####.................\n" +
                    "........................\n");

    @BeforeClass
    public static void initializeTestGrids() {
        TEST_GRID = JumpPointSearchTestUtils.buildTestGrid(TEST_GRID_WIDTH, TEST_GRID_HEIGHT, TEST_GRID_PASSABLE_TILES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getJumpPoint_whenGraphIsNull_ExpectedIllegalArgumentException() {
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        strategy.getJumpPoint(null, TEST_GRID[0][0], TEST_GRID[0][1], TEST_GRID[0][2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getJumpPoint_whenParentNodeIsSameAsJumpPoint_ExpectedIllegalArgumentException() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        strategy.getJumpPoint(graph, TEST_GRID[0][0], TEST_GRID[0][0], TEST_GRID[0][2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getJumpPoint_whenParentNodeIsNull_ExpectedIllegalArgumentException() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        strategy.getJumpPoint(graph, null, TEST_GRID[0][1], TEST_GRID[0][2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getJumpPoint_whenJumpNodeIsNull_ExpectedIllegalArgumentException() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        strategy.getJumpPoint(graph, TEST_GRID[0][0], null, TEST_GRID[0][2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getJumpPoint_whenDestinationNodeIsNull_ExpectedIllegalArgumentException() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        strategy.getJumpPoint(graph, TEST_GRID[0][0], TEST_GRID[0][1], null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getJumpPoint_whenInitialJumpPointIsNotNeighborOfStartNode_ExpectedIllegalArgumentException() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        strategy.getJumpPoint(graph, TEST_GRID[0][0], TEST_GRID[0][2], TEST_GRID[0][3]);
    }


    /*
     * -----------------------------------------------------
     * Horizontal tests
     * -----------------------------------------------------
     */

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  SD.......#.............. 0
     *  .........#..#.#......... 1
     *  .........#............#. 2
     *  .........#.............. 3
     *  .........#............#. 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointEqualsDestination_AdjacentHorizontally_ExpectedDestinationReturned() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[0][0], TEST_GRID[1][0], TEST_GRID[1][0]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[1][0], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  SJ......D#.............. 0
     *  .........#..#.#......... 1
     *  .........#............#. 2
     *  .........#.............. 3
     *  .........#............#. 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetHorizontally_AndDestinationOffsetHorizontally_AndNoForcedNeighborsExist_ExpectedDestinationReturned() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[0][0], TEST_GRID[1][0], TEST_GRID[8][0]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[8][0], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  SJ.......#.............. 0
     *  .........#..#.#......... 1
     *  ........D#............#. 2
     *  .........#.............. 3
     *  .........#............#. 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetHorizontally_AndDestinationOffsetDiagonally_AndNoForcedNeighborsExist_ExpectedNoJumpPointFound() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[0][0], TEST_GRID[1][0], TEST_GRID[8][2]);
        Assert.assertNull("Unexpected jump point returned", jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#.............. 0
     *  .........#..#F#......... 1
     *  .........#D...X...JS..#. 2
     *  .........#.............. 3
     *  .........#............#. 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetHorizontally_AndDestinationOffsetHorizontally_WithForcedNeighborAbove_ExpectedJumpPointFound() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[19][2], TEST_GRID[18][2], TEST_GRID[10][2]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[14][2], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#.............. 0
     *  .........#..#.#......... 1
     *  .........#............#. 2
     *  .........#.............. 3
     *  .........#.SJ....X...D#. 4
     *  ##########.......#F#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetHorizontally_AndDestinationOffsetHorizontally_WithForcedNeighborBelow_ExpectedJumpPointFound() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[11][4], TEST_GRID[12][4], TEST_GRID[21][4]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[17][4], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#.............. 0
     *  .........#..#.#......... 1
     *  .........#............#F 2
     *  .........#.SJ.........XD 3
     *  .........#............#F 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetHorizontally_AndDestinationOffsetHorizontally_WithForcedNeighborAboveAndBelow_ExpectedJumpPointFound() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[11][3], TEST_GRID[12][3], TEST_GRID[23][3]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[22][3], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#.............. 0
     *  .........#D.#F#......... 1
     *  .........#....X...JS..#. 2
     *  .........#.............. 3
     *  .........#............#. 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetHorizontally_AndDestinationOffsetDiagonally_WithForcedNeighborAbove_ExpectedJumpPointFound() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[19][2], TEST_GRID[18][2], TEST_GRID[10][1]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[14][2], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#.............. 0
     *  .........#..#.#......... 1
     *  .........#............#. 2
     *  .........#.............. 3
     *  .........#.SJ....X....#. 4
     *  ##########.......#F#.D.. 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetHorizontally_AndDestinationOffsetDiagonally_WithForcedNeighborBelow_ExpectedJumpPointFound() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[11][4], TEST_GRID[12][4], TEST_GRID[21][5]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[17][4], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#.............. 0
     *  .........#..#.#......... 1
     *  .........#............#F 2
     *  .........#.SJ.........X. 3
     *  .........#............#F 4
     *  ##########.......#D#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetHorizontally_AndDestinationOffsetDiagonally_WithForcedNeighborAboveAndBelow_ExpectedJumpPointFound() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[11][3], TEST_GRID[12][3], TEST_GRID[18][5]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[22][3], jumpPoint);
    }

    /*
     * -----------------------------------------------------
     * Vertical tests
     * -----------------------------------------------------
     */


    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  S........#.............. 0
     *  D........#..#.#......... 1
     *  .........#............#. 2
     *  .........#.............. 3
     *  .........#............#. 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointEqualsDestination_AdjacentVertically_ExpectedDestinationReturned() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[0][0], TEST_GRID[0][1], TEST_GRID[0][1]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[0][1], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  S........#.............. 0
     *  J........#..#.#......... 1
     *  .........#............#. 2
     *  .........#.............. 3
     *  D........#............#. 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetVertically_AndDestinationOffsetHorizontally_AndNoForcedNeighborsExist_ExpectedDestinationReturned() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[0][0], TEST_GRID[0][1], TEST_GRID[0][4]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[0][4], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  S........#.............. 0
     *  J........#..#.#......... 1
     *  ........D#............#. 2
     *  .........#.............. 3
     *  .........#............#. 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetVertically_AndDestinationOffsetDiagonally_AndNoForcedNeighborsExist_ExpectedNoJumpPointFound() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[0][0], TEST_GRID[0][1], TEST_GRID[8][2]);
        Assert.assertNull("Unexpected jump point returned", jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#....FD........ 0
     *  .........#..#.#X........ 1
     *  .........#............#. 2
     *  .........#.............. 3
     *  .........#.....J......#. 4
     *  ##########.....S.#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetVertically_AndDestinationOffsetVertically_WithForcedNeighborOnLeft_ExpectedJumpPointFound() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[15][5], TEST_GRID[15][4], TEST_GRID[15][0]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[15][1], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#.DF........... 0
     *  .........#.X#.#......... 1
     *  .........#............#. 2
     *  .........#.............. 3
     *  .........#.J..........#. 4
     *  ##########.S.....#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetVertically_AndDestinationOffsetVertically_WithForcedNeighborOnRight_ExpectedJumpPointFound() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[11][5], TEST_GRID[11][4], TEST_GRID[11][0]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[11][1], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#..FDF......... 0
     *  .........#..#X#......... 1
     *  .........#............#. 2
     *  .........#.............. 3
     *  .........#...J........#. 4
     *  ##########...S...#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetVertically_AndDestinationOffsetVertically_WithForcedNeighborOnRightAndLeft_ExpectedJumpPointFound() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[13][5], TEST_GRID[13][4], TEST_GRID[13][0]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[13][1], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#..........S... 0
     *  .........#..#.#.....J... 1
     *  .........#............#. 2
     *  .........#.............D 3
     *  .........#............#. 4
     *  ##########.......#.#X... 5
     *  ...................F.... 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetVertically_AndDestinationOffsetDiagonally_WithForcedNeighborOnLeft_ExpectedJumpPointFound() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[20][0], TEST_GRID[20][1], TEST_GRID[23][3]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[20][5], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#......S....... 0
     *  .........#..#.#.J....... 1
     *  .........#............#. 2
     *  .........#.............D 3
     *  .........#............#. 4
     *  ##########......X#.#.... 5
     *  .................F...... 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetVertically_AndDestinationOffsetDiagonally_WithForcedNeighborOnRight_ExpectedJumpPointFound() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[16][0], TEST_GRID[16][1], TEST_GRID[23][3]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[16][5], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#........S..... 0
     *  .........#..#.#...J..... 1
     *  .........#............#. 2
     *  .........#..D........... 3
     *  .........#............#. 4
     *  ##########.......#X#.... 5
     *  .................F.F.... 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetVertically_AndDestinationOffsetDiagonally_WithForcedNeighborOnRightAndLeft_ExpectedJumpPointFound() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[18][0], TEST_GRID[18][1], TEST_GRID[12][3]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[18][5], jumpPoint);
    }


    /*
     * -----------------------------------------------------
     * Diagonal tests
     * -----------------------------------------------------
     */


    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  S........#.............. 0
     *  .D.......#..#.#......... 1
     *  .........#............#. 2
     *  .........#.............. 3
     *  .........#............#. 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointEqualsDestination_AdjacentDiagonally_ExpectedDestinationReturned() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[0][0], TEST_GRID[1][1], TEST_GRID[1][1]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[1][1], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  S........#.............. 0
     *  .J.......#..#.#......... 1
     *  .........#............#. 2
     *  .........#.............. 3
     *  ....D....#............#. 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetDiagonally_AndDestinationOffsetDirectlyDiagonally_ExpectedDestinationReturned() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[0][0], TEST_GRID[1][1], TEST_GRID[4][4]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[4][4], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  S........#.............. 0
     *  .J.......#..#.#......... 1
     *  .........#............#. 2
     *  ...X....D#.............. 3
     *  .........#............#. 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetDiagonally_AndDestinationAlongIntermediateHorizontalJumpPoint_AndNoForcedNeighborsExist_ExpectedJumpPointFoundOnDiagonal() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[0][0], TEST_GRID[1][1], TEST_GRID[8][3]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[3][3], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  S........#.............. 0
     *  .J.......#..#.#......... 1
     *  .........#............#. 2
     *  ...X.....#.............. 3
     *  ...D.....#............#. 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetDiagonally_AndDestinationAlongIntermediateVerticalJumpPoint_AndNoForcedNeighborsExist_ExpectedJumpPointFoundOnDiagonal() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[0][0], TEST_GRID[1][1], TEST_GRID[3][4]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[3][3], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#.............. 0
     *  .........#..#.#......... 1
     *  .........#............#. 2
     *  .........#.............. 3
     *  .........#............#. 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  ....X................... 7
     *  DF#..J.................. 8
     *  ..#...S................. 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetDiagonally_AndForcedNeighborAlongIntermediateHorizontalJumpPoint_ExpectedJumpPointFoundOnDiagonal() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[7][9], TEST_GRID[6][8], TEST_GRID[0][8]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[5][7], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#.............. 0
     *  .........#..#.#......... 1
     *  .........#............#. 2
     *  .........#.............. 3
     *  .........#............#. 4
     *  ##########.......#.#.... 5
     *  ......D................. 6
     *  ........................ 7
     *  ..#.J................... 8
     *  ..#S.................... 9
     *  ........................ 10
     *  ........................ 11
     *  ........................ 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetDiagonally_AndForcedNeighborAlongIntermediateHorizontalJumpPointInOppositeDirection_ExpectedJumpPointNotEndedEarly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[3][9], TEST_GRID[4][8], TEST_GRID[6][6]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[6][6], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#.............. 0
     *  .........#..#.#......... 1
     *  .........#............#. 2
     *  .........#.............. 3
     *  .........#............#. 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  ...S.................... 7
     *  ..#.J................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  .......X................ 11
     *  ........................ 12
     *  ...####................. 13
     *  .....DF................. 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetDiagonally_AndForcedNeighborAlongIntermediateVerticalJumpPoint_ExpectedJumpPointFoundOnDiagonal() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[3][7], TEST_GRID[4][8], TEST_GRID[5][14]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[7][11], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#.............. 0
     *  .........#..#.#......... 1
     *  .........#............#. 2
     *  .........#.............. 3
     *  .........#............#. 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  .........D.............. 7
     *  ..#..................... 8
     *  ..#..................... 9
     *  ........................ 10
     *  .....J.................. 11
     *  ....S................... 12
     *  ...####................. 13
     *  ........................ 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetDiagonally_AndForcedNeighborAlongIntermediateVerticalJumpPointInOppositeDirection_ExpectedJumpPointNotEndedEarly() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[4][12], TEST_GRID[5][11], TEST_GRID[9][7]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[9][7], jumpPoint);
    }

    /*
     * |0        |10       |20
     *  012345678901234567890123
     *  .........#.............. 0
     *  .........#..#.#......... 1
     *  .........#............#. 2
     *  .........#.............. 3
     *  .........#............#. 4
     *  ##########.......#.#.... 5
     *  ........................ 6
     *  ........................ 7
     *  ..#..................... 8
     *  ..#P.................... 9
     *  ....J................... 10
     *  ........................ 11
     *  ........................ 12
     *  ...####X................ 13
     *  ......F................D 14
     */
    @Test
    public void getJumpPoint_whenInitialJumpPointOffsetDiagonally_AndForcedNeighborAlongDiagonal_ExpectedJumpPointEndedAtForcedNeighbor() {
        final DefaultTiledGraph<GridNodeFake> graph = JumpPointSearchTestUtils.constructDefaultGridFormattedGraphFromTestGrid(TEST_GRID, TEST_GRID_WIDTH, TEST_GRID_HEIGHT);
        final JumpPointSearchJumpingStrategy<GridNodeFake> strategy = new JumpPointSearchDiagonalAlwaysPermittedJumpingStrategy<>();

        final GridNodeFake jumpPoint = strategy.getJumpPoint(graph, TEST_GRID[3][9], TEST_GRID[4][10], TEST_GRID[23][14]);
        Assert.assertEquals("Unexpected jump point returned", TEST_GRID[7][13], jumpPoint);
    }



}
