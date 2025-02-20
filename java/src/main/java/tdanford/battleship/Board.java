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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.TreeSet;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.collections.impl.factory.Lists;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

/**
 * A Battleship board and an associated set of shots, each annotated as a hit or miss.
 *
 * <p>Think of this as the equivalent of one half of a Battleship 'screen' from the physical
 * game, either the upper or lower board, along with a set of white and red pegs in the
 * screen.  One difference between this and the physical game is that this Board remembers
 * the ordered list of {@link Spot} values (i.e. the list of shots).</p>
 */
public class Board {

  private final Set<Spot> shotSet;
  private final ArrayList<Spot> shotList;
  private final Set<Integer> hits;

  public Board() {
    this.shotSet = new HashSet<>();
    this.shotList = new ArrayList<>();
    this.hits = new TreeSet<>();
  }

  @JsonCreator
  public Board(
    @JsonProperty final List<Spot> shots,
    @JsonProperty final List<Integer> hitIndices
  ) {
    this();
    shotSet.addAll(shots);
    shotList.addAll(shots);
    hits.addAll(hitIndices);
  }

  public String printBoard() {
    final StringBuilder sb = new StringBuilder();

    sb.append("           1\n");
    sb.append("  1234567890\n");

    for (int r = 0; r < 10; r++) {
      sb.append(String.format("%s ", Character.toChars(r + 'A')[0]));

      for (int c = 1; c <= 10; c++) {
        final Spot s = new Spot(r, c);
        if (shotSet.contains(s)) {
          sb.append(hits.contains(shotIndex(s)) ? "X" : "O");
        } else {
          sb.append(".");
        }

      }
      sb.append("\n");
    }

    return sb.toString();
  }

  public Board withShot(final Spot shot, final Boolean hit) {
    return new Board(
      Lists.mutable.ofAll(shotList).with(shot),
      hit ? Lists.mutable.ofAll(hits).with(this.shotList.size()) : Lists.mutable.ofAll(hits)
    );
  }

  public List<Spot> getShots() { return shotList; }

  public List<Integer> getHits() { return Lists.mutable.ofAll(hits); }

  private int shotIndex(final Spot shot) {
    Preconditions.checkArgument(shotSet.contains(shot));
    return shotList.indexOf(shot);
  }

  public boolean isHit(final Spot shot) {
    return shotSet.contains(shot) && hits.contains(shotIndex(shot));
  }

  public boolean isMiss(final Spot shot) {
    return shotSet.contains(shot) && !hits.contains(shotIndex(shot));
  }

  public boolean isNoShot(final Spot shot) { return !shotSet.contains(shot); }
}
