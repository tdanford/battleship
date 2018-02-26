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

import static java.util.stream.Collectors.toList;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.tuple.Tuples;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

public class Spot implements Comparable<Spot> {

  private static Pattern PATTERN = Pattern.compile("([A-Ja-j])([1-9]|10)");

  private final int row;
  private final int col;

  public Spot(final String str) {
    Preconditions.checkArgument(str != null);

    final Matcher matcher = PATTERN.matcher(str);
    if (!matcher.matches()) { throw new IllegalArgumentException(str); }
    this.row = matcher.group(1).toUpperCase().charAt(0) - 'A';
    this.col = Integer.parseInt(matcher.group(2));
  }

  @JsonCreator
  public Spot(
    @JsonProperty final int row,
    @JsonProperty final int col
  ) {
    Preconditions.checkArgument(isLegalRow(row), String.format("Row %d isn't a legal value", row));
    Preconditions.checkArgument(isLegalCol(col), String.format("Col %d isn't a legal value", col));

    this.row = row;
    this.col = col;
  }

  private static boolean isLegalRow(final int row) {
    return row >= 0 && row < 10;
  }

  private static boolean isLegalCol(final int col) {
    return col > 0 && col <= 10;
  }

  @SuppressWarnings("unchecked")
  private static final List<Pair<Integer, Integer>> ADJACENT_DIFFS = Lists.mutable.of(
    Tuples.pair(-1, 0),
    Tuples.pair(1, 0),
    Tuples.pair(0, -1),
    Tuples.pair(0, 1)
  );

  public List<Spot> adjacencies() {
    return ADJACENT_DIFFS.stream()
      .filter(p -> isLegalRow(row + p.getOne()) && isLegalCol(col + p.getTwo()))
      .map(p -> new Spot(row + p.getOne(), col + p.getTwo()))
      .collect(toList());
  }

  public boolean isAdjacent(final Spot spot) {
    return (row == spot.row && Math.abs(col - spot.col) == 1) ||
      (col == spot.col && Math.abs(row - spot.row) == 1);
  }

  public int getRow() { return row; }

  public int getCol() { return col; }

  public int hashCode() { return Objects.hash(row, col); }

  public boolean equals(final Object o) {
    if (!(o instanceof Spot)) { return false; }
    final Spot s = (Spot) o;
    return s.row == row && s.col == col;
  }

  public String toString() {
    return Character.toChars('A' + row)[0] + String.valueOf(col);
  }

  public int compareTo(final Spot s) {
    if (s.row != row) {
      return Integer.compare(row, s.row);
    } else {
      return Integer.compare(col, s.col);
    }
  }

  public boolean isAligned(final Spot spot) {
    return (spot.getRow() == row || spot.getCol() == col);
  }

  public int axisDistance(final Spot spot) {
    Preconditions.checkArgument(spot != null);
    Preconditions.checkArgument(isAligned(spot));

    return Math.max(Math.abs(spot.getRow() - row), Math.abs(spot.getCol() - col)) + 1;
  }

  /**
   * Creates a Stream of <i>all</i> {@link Spot}s
   *
   * @return A {@link Stream} of {@link Spot}, 100 in the default board size.
   */
  public static Stream<Spot> spots() {
    return StreamSupport.stream(
      Spliterators.spliterator(new SpotIterator(), 100, 0),
      false
    );
  }

  private static class SpotIterator implements Iterator<Spot> {

    private int row = 0;
    private int col = 1;

    @Override
    public boolean hasNext() {
      return isLegalCol(col) && isLegalRow(row);
    }

    @Override
    public Spot next() {
      final Spot spot = new Spot(row, col);

      col += 1;
      if (!isLegalCol(col)) {
        col = 1;
        row += 1;
      }

      return spot;
    }
  }

}
