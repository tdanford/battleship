/*
 *  Copyright 2018 Timothy Danford
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package tdanford.battleship;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.collections.impl.factory.Maps;

public class Board {

  private final int numRows, numCols;

  private final TreeMap<Spot, Boolean> hits;

  public Board() {
    this.numRows = 10;
    this.numCols = 10;
    this.hits = new TreeMap<>();
  }

  public Board(final Map<Spot, Boolean> hits) {
    this();
    this.hits.putAll(hits);
  }

  public Board withShot(final Spot shot, final Boolean hit) {
    return new Board(Maps.immutable.ofMap(hits).newWithKeyValue(shot, hit).toMap());
  }

  public int getNumRows() { return numRows; }

  public int getNumCols() { return numCols; }

  public Iterable<Spot> shots() { return hits.keySet(); }

  public boolean isHit(final Spot shot) { return hits.containsKey(shot) && hits.get(shot); }
  public boolean isMiss(final Spot shot) { return hits.containsKey(shot) && !hits.get(shot); }
  public boolean isNoShot(final Spot shot) { return !hits.containsKey(shot); }
}
