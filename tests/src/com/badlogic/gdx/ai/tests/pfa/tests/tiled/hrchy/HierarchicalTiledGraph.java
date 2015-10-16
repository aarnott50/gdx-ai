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

package com.badlogic.gdx.ai.tests.pfa.tests.tiled.hrchy;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedHierarchicalGraph;
import com.badlogic.gdx.ai.tests.pfa.tests.tiled.DungeonUtils;
import com.badlogic.gdx.ai.tests.pfa.tests.tiled.DungeonUtils.TwoLevelHierarchy;
import com.badlogic.gdx.ai.tests.pfa.tests.tiled.IndexedTiledGraph;
import com.badlogic.gdx.ai.tests.pfa.tests.tiled.flat.FlatTiledNode;
import com.badlogic.gdx.math.Vector2;

/** A random generated graph representing a hierarchical tiled map.
 *
 * @author davebaol */
public class HierarchicalTiledGraph extends IndexedHierarchicalGraph<HierarchicalTiledNode> implements
	IndexedTiledGraph<HierarchicalTiledNode> {
	private static final int LEVELS = 2;
	public static final int[] sizeX = {125, 3};
	public static final int[] sizeY = {75, 2};
	public static final int[] offset = {0, sizeX[0] * sizeY[0]};

	public boolean diagonal;
	public HierarchicalTiledNode startNode;

	public HierarchicalTiledGraph () {
		super(LEVELS, calculateTotalCapacity());
		this.diagonal = false;
		this.startNode = null;
	}

	@Override
	public void init (final int roomCount, final int roomMinSize, final int roomMaxSize, final int squashIterations) {
		final int tilesX = sizeX[0];
		final int tilesY = sizeY[0];
		final int buildingsX = sizeX[1];
		final int buildingsY = sizeY[1];

		final TwoLevelHierarchy twoLevelHierarchy = DungeonUtils.generate2LevelHierarchy(tilesX, tilesY, buildingsX, buildingsY,
			(int)(roomCount - roomCount * .8f), (int)(roomCount + roomCount * .8f), roomMinSize, roomMaxSize, squashIterations);

		// Create nodes for level 0 (tiles)
		this.level = 0;
		final int map[][] = twoLevelHierarchy.level0;
		for (int x = 0; x < tilesX; x++) {
			for (int y = 0; y < tilesY; y++) {
				nodes.add(new HierarchicalTiledNode(x, y, map[x][y], nodes.size, 4));
			}
		}

		// Create connections for level 0
		// Each node has up to 4 neighbors, therefore no diagonal movement is possible
		for (int x = 0; x < tilesX; x++) {
			final int idx = x * tilesY;
			for (int y = 0; y < tilesY; y++) {
				final HierarchicalTiledNode n = nodes.get(idx + y);
				if (x > 0) {
                    addLevel0Connection(n, -1, 0);
                }
				if (y > 0) {
                    addLevel0Connection(n, 0, -1);
                }
				if (x < tilesX - 1) {
                    addLevel0Connection(n, 1, 0);
                }
				if (y < tilesY - 1) {
                    addLevel0Connection(n, 0, 1);
                }
			}
		}

		// Create nodes for level 1 (buildings)
		this.level = 1;
		final int xxx = tilesX / buildingsX;
		final int yyy = tilesY / buildingsY;
		for (int x = 0; x < buildingsX; x++) {
			for (int y = 0; y < buildingsY; y++) {
				final int x0 = x * xxx;
				final int y0 = y * yyy;
				final int x1 = x0 + xxx;
				final int y1 = y0 + yyy;
				final HierarchicalTiledNode lowerLevelNode = findFloorTileClosestToCenterOfMass(this.level - 1, x0, y0, x1, y1);
				nodes.add(new HierarchicalTiledNode(x, y, map[x][y], nodes.size, 4) {
					@Override
					public HierarchicalTiledNode getLowerLevelNode () {
						return lowerLevelNode;
					}
				});
			}
		}

// System.out.println(DungeonUtils.mapToString(map));

		// Create connections for level 1
		// Each node has up to 2 neighbors
		for (int x = 0; x < buildingsX; x++) {
			for (int y = 0; y < buildingsY; y++) {
				final HierarchicalTiledNode n = getNode(x, y);
				if (twoLevelHierarchy.level1Con1[x][y]) {
                    addLevel1BidirectionalConnection(n, 0, 1);
                }
				if (twoLevelHierarchy.level1Con2[x][y]) {
                    addLevel1BidirectionalConnection(n, 1, 0);
                }
			}
		}

	}

	private HierarchicalTiledNode getNodeAtLevel (final int level, final int x, final int y) {
		return nodes.get(x * sizeY[level] + y + offset[level]);
	}

	private HierarchicalTiledNode findFloorTileClosestToCenterOfMass (final int level, final int x0, final int y0, final int x1, final int y1) {
		// Calculate center of mass
		final Vector2 centerOfMass = new Vector2(0, 0);
		int floorTiles = 0;
		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				final HierarchicalTiledNode n = getNodeAtLevel(level, x, y);
				if (n.type == FlatTiledNode.TILE_FLOOR) {
					centerOfMass.add(n.x, n.y);
					floorTiles++;
				}
			}
		}
		final int comx = (int)(centerOfMass.x / floorTiles);
		final int comy = (int)(centerOfMass.y / floorTiles);
		final HierarchicalTiledNode comTile = getNodeAtLevel(level, comx, comy);
		if (comTile.type == FlatTiledNode.TILE_FLOOR) {
            return comTile;
        }

		// Find floor tile closest to the center of mass
		float closetDist2 = Float.POSITIVE_INFINITY;
		HierarchicalTiledNode closestFloor = null;
		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				final HierarchicalTiledNode n = getNodeAtLevel(level, x, y);
				if (n.type == FlatTiledNode.TILE_FLOOR) {
					final float dist2 = Vector2.dst2(comTile.x, comTile.y, n.x, n.y);
					if (dist2 < closetDist2) {
						closetDist2 = dist2;
						closestFloor = n;
					}
				}
			}
		}

		return closestFloor;
	}

	@Override
	public HierarchicalTiledNode convertNodeBetweenLevels (final int inputLevel, final HierarchicalTiledNode node, final int outputLevel) {
		if (inputLevel < outputLevel) {
			final int newX = node.x / (sizeX[inputLevel] / sizeX[outputLevel]);
			final int newY = node.y / (sizeY[inputLevel] / sizeY[outputLevel]);
			return nodes.get(newX * sizeY[outputLevel] + newY + offset[outputLevel]);
		}

		if (inputLevel > outputLevel) {
			return node.getLowerLevelNode();
		}

		return node;
	}

	@Override
	public HierarchicalTiledNode getNode (final int x, final int y) {
		return nodes.get(x * sizeY[level] + y + offset[level]);
	}

	@Override
	public HierarchicalTiledNode getNode (final int index) {
		return nodes.get(index);
	}

	private void addLevel0Connection (final HierarchicalTiledNode n, final int xOffset, final int yOffset) {
		final HierarchicalTiledNode target = getNode(n.x + xOffset, n.y + yOffset);
		if (target.type == FlatTiledNode.TILE_FLOOR) {
            n.getConnections().add(new HierarchicalTiledConnection(this, n, target));
        }
	}

	private void addLevel1BidirectionalConnection (final HierarchicalTiledNode n, final int xOffset, final int yOffset) {
		final HierarchicalTiledNode target = getNode(n.x + xOffset, n.y + yOffset);
		n.getConnections().add(new HierarchicalTiledConnection(this, n, target));
		target.getConnections().add(new HierarchicalTiledConnection(this, target, n));
	}

	private static final int calculateTotalCapacity () {
		int capacity = 0;
		for (int i = 0; i < LEVELS; i++) {
            capacity += sizeX[i] * sizeY[i];
        }
		return capacity;
	}

    @Override
    public boolean hasConnection(
            final HierarchicalTiledNode sourceNode,
            final HierarchicalTiledNode destinationNode) {

        for(final Connection<HierarchicalTiledNode> connection : sourceNode.getConnections()) {
            if (connection.getToNode() == destinationNode) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasConnection(final HierarchicalTiledNode sourceNode, final int x, final int y) {
        return hasConnection(sourceNode, getNode(x, y));
    }

    @Override
    public int getWidth() {
        throw new UnsupportedOperationException("getWidth does not make sense for a multi-leveled map");
    }

    @Override
    public int getHeight() {
        throw new UnsupportedOperationException("getHeight does not make sense for a multi-leveled map");
    }

    @Override
    public boolean isInBounds(final int x, final int y) {
        throw new UnsupportedOperationException("isInBounds does not make sense for a multi-leveled map");
    }
}
