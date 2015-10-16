package com.badlogic.gdx.ai.pfa.jumppointsearch;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.utils.Array;

public class JumpPointSearchTestUtils {

    public static boolean[][] buildPassableTilesFromString(final String stringRepresentation) {
        final String[] rows = stringRepresentation.split("\n");
        final int width = rows[0].length();
        final int height = rows.length;

        final boolean[][] passableTiles = new boolean[width][height];

        for (int y = 0; y < height; y++) {
            final String row = rows[y];
            for (int x = 0; x < width; x++) {
                passableTiles[x][y] = row.charAt(x) != '#';
            }
        }

        return passableTiles;
    }

    public static GridNodeFake[][] buildTestGrid(final int width, final int height, final boolean[][] passableTiles) {
        final GridNodeFake[][] grid = new GridNodeFake[width][height];
        fillTestGrid(grid, width, height);
        connectTestGrid(grid, width, height, passableTiles);
        return grid;
    }

    public static DefaultTiledGraph<GridNodeFake> constructDefaultGridFormattedGraphFromTestGrid(final GridNodeFake[][] testGrid, final int width, final int height) {
        final DefaultTiledGraph<GridNodeFake> graph = new DefaultTiledGraph<>(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                graph.setNode(x, y, testGrid[x][y]);
            }
        }
        return graph;
    }

    public static class GridNodeFake implements TiledNode<GridNodeFake> {
        public final int x;
        public final int y;
        public final Array<Connection<GridNodeFake>> connections = new Array<>(8);

        public GridNodeFake() {
            this(0, 0);
        }

        public GridNodeFake(final int x, final int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }

        @Override
        public Array<Connection<GridNodeFake>> getConnections() {
            return connections;
        }

        @Override
        public String toString() {
            return "GridNodeFake [x=" + x + ", y=" + y + ", connections=" +
                    connections + "]";
        }


    }




    private static void fillTestGrid(final GridNodeFake[][] grid, final int width, final int height) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new GridNodeFake(x, y);
            }
        }
    }

    private static void connectTestGrid(final GridNodeFake[][] grid, final int width, final int height, final boolean[][] passableTiles) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                addConnectionIfPossible(grid, width, height, passableTiles, x, y, x - 1, y - 1);
                addConnectionIfPossible(grid, width, height, passableTiles, x, y, x - 1, y);
                addConnectionIfPossible(grid, width, height, passableTiles, x, y, x - 1, y + 1);
                addConnectionIfPossible(grid, width, height, passableTiles, x, y, x, y - 1);
                addConnectionIfPossible(grid, width, height, passableTiles, x, y, x, y + 1);
                addConnectionIfPossible(grid, width, height, passableTiles, x, y, x + 1, y - 1);
                addConnectionIfPossible(grid, width, height, passableTiles, x, y, x + 1, y);
                addConnectionIfPossible(grid, width, height, passableTiles, x, y, x + 1, y + 1);
            }
        }
    }

    private static void addConnectionIfPossible(final GridNodeFake[][] grid, final int width, final int height, final boolean[][] passableTiles, final int sx, final int sy, final int dx, final int dy) {
        if (!passableTiles[sx][sy] || dx < 0 || dx >= width || dy < 0 || dy >= height || !passableTiles[dx][dy]) {
            return;
        }

        grid[sx][sy].connections.add(new DefaultConnection<GridNodeFake>(grid[sx][sy], grid[dx][dy]));
    }

}
