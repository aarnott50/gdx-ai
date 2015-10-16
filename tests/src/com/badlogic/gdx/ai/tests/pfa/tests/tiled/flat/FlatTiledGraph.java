/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.ai.tests.pfa.tests.tiled.flat;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.DefaultIndexedGraph;
import com.badlogic.gdx.ai.tests.pfa.tests.tiled.DungeonUtils;
import com.badlogic.gdx.ai.tests.pfa.tests.tiled.IndexedTiledGraph;

/** A random generated graph representing a flat tiled map.
 *
 * @author davebaol */
public class FlatTiledGraph extends DefaultIndexedGraph<FlatTiledNode> implements IndexedTiledGraph<FlatTiledNode> {
	public static final int sizeX = 125; // 200; //100;
	public static final int sizeY = 75; // 120; //60;

	public boolean diagonal;
	public FlatTiledNode startNode;

	public FlatTiledGraph () {
		super(sizeX * sizeY);
		this.diagonal = false;
		this.startNode = null;
	}

	@Override
	public void init (final int roomCount, final int roomMinSize, final int roomMaxSize, final int squashIterations) {
		final int map[][] = DungeonUtils.generate(sizeX, sizeY, roomCount, roomMinSize, roomMaxSize, squashIterations);
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				nodes.add(new FlatTiledNode(x, y, map[x][y], 4));
			}
		}

		// Each node has up to 4 neighbors, therefore no diagonal movement is possible
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
			}
		}
	}

	@Override
	public FlatTiledNode getNode (final int x, final int y) {
		return nodes.get(x * sizeY + y);
	}

	@Override
	public FlatTiledNode getNode (final int index) {
		return nodes.get(index);
	}

	protected void addConnection (final FlatTiledNode n, final int xOffset, final int yOffset) {
		final FlatTiledNode target = getNode(n.x + xOffset, n.y + yOffset);
		if (target.type == FlatTiledNode.TILE_FLOOR) {
            n.getConnections().add(new FlatTiledConnection(this, n, target));
        }
	}

    @Override
    public boolean hasConnection(
            final FlatTiledNode sourceNode,
            final FlatTiledNode destinationNode) {

        for(final Connection<FlatTiledNode> connection : sourceNode.getConnections()) {
            if (connection.getToNode() == destinationNode) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasConnection(final FlatTiledNode sourceNode, final int x, final int y) {
        return hasConnection(sourceNode, getNode(x, y));
    }

    @Override
    public int getWidth() {
        return sizeX;
    }

    @Override
    public int getHeight() {
        return sizeY;
    }

    @Override
    public boolean isInBounds(final int x, final int y) {
        return x >= 0 && x < sizeX && y >= 0 && y < sizeY;
    }

}
