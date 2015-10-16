package com.badlogic.gdx.ai.tests.pfa.tests.tiled.flat;

import com.badlogic.gdx.ai.tests.pfa.tests.tiled.DungeonUtils;

public class DiagonallyConnectedFlatTiledGraph extends FlatTiledGraph {

    @Override
    public void init (final int roomCount, final int roomMinSize, final int roomMaxSize, final int squashIterations) {
        final int map[][] = DungeonUtils.generate(sizeX, sizeY, roomCount, roomMinSize, roomMaxSize, squashIterations);
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                nodes.add(new FlatTiledNode(x, y, map[x][y], 8));
            }
        }

        for (int x = 0; x < sizeX; x++) {
            final int idx = x * sizeY;
            for (int y = 0; y < sizeY; y++) {
                final FlatTiledNode n = nodes.get(idx + y);
                if (x > 0) {
                    addConnection(n, -1, 0);
                }
                if (y > 0) {
                    addConnection(n, 0, -1);
                }
                if (x < sizeX - 1) {
                    addConnection(n, 1, 0);
                }
                if (y < sizeY - 1) {
                    addConnection(n, 0, 1);
                }
                if (x > 0 && y > 0) {
                    addConnection(n, -1, -1);
                }
                if (x > 0 && y < sizeY - 1) {
                    addConnection(n, -1, 1);
                }
                if (x < sizeX - 1 && y > 0) {
                    addConnection(n, 1, -1);
                }
                if (x < sizeX - 1 && y < sizeY - 1) {
                    addConnection(n, 1, 1);
                }
            }
        }
    }

}
