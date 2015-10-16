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
import com.badlogic.gdx.ai.tests.pfa.tests.tiled.IndexedTiledNode;
import com.badlogic.gdx.utils.Array;

/** A node for a {@link FlatTiledGraph}.
 *
 * @author davebaol */
public class FlatTiledNode extends IndexedTiledNode<FlatTiledNode> {

	public FlatTiledNode (final int x, final int y, final int type, final int connectionCapacity) {
		super(x, y, type, new Array<Connection<FlatTiledNode>>(connectionCapacity));
	}

	@Override
	public int getIndex () {
		return x * FlatTiledGraph.sizeY + y;
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
    public String toString() {
        return "(" + x + ", " + y + ")";
    }



}
