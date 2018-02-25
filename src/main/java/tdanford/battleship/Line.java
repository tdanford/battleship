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

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import com.google.common.base.Preconditions;

/**
 * Axis-aligned line on the grid
 */
public class Line implements Comparable<Line>, Serializable {

  private static final long serialVersionUID = 1L;

  private final Spot start, finish;

  public Line(final Spot s1, final Spot s2) {
    Preconditions.checkArgument(s1 != null);
    Preconditions.checkArgument(s2 != null);
    Preconditions.checkArgument(s1.getRow() == s2.getRow() || s1.getCol() == s2.getCol());

    if (s1.compareTo(s2) <= 0) {
      this.start = s1;
      this.finish = s2;
    } else {
      this.start = s2;
      this.finish = s1;
    }

    Preconditions.checkState(isVertical() || isHorizontal());
    Preconditions.checkState(isSpot() || !isVertical() || !isHorizontal());
  }

  public int compareTo(final Line line) {
    if (!start.equals(line.start)) {
      return start.compareTo(line.start);
    } else {
      return finish.compareTo(line.finish);
    }
  }

  public boolean isSpot() { return start.equals(finish); }

  public Spot nextSpot() {
    Preconditions.checkState(!isSpot());

    if (isVertical()) {
      return new Spot(finish.getRow() + 1, finish.getCol());
    } else {
      return new Spot(finish.getRow(), finish.getCol() + 1);
    }
  }

  public Spot previousSpot() {
    Preconditions.checkState(!isSpot());

    if (isVertical()) {
      return new Spot(finish.getRow() - 1, finish.getCol());
    } else {
      return new Spot(finish.getRow(), finish.getCol() - 1);
    }
  }

  public int hashCode() { return Objects.hash(start, finish); }

  public boolean equals(final Object o) {
    if (!(o instanceof Line)) { return false; }
    final Line line = (Line) o;
    return Objects.equals(start, line.start) && Objects.equals(finish, line.finish);
  }

  public String toString() {
    return String.format("%s-%s", start, finish);
  }

  public boolean isVertical() {
    return start.getCol() == finish.getCol();
  }

  public boolean isHorizontal() {
    return start.getRow() == finish.getRow();
  }

  public Spot getStart() { return start; }

  public Spot getFinish() { return finish; }

  public boolean contains(int col, int row) {
    if (isVertical()) {
      return start.getCol() == col && containsRow(row);
    } else {
      return start.getRow() == row && containsCol(col);
    }
  }

  public boolean containsRow(final int row) {
    return start.getRow() <= row && row <= finish.getRow();
  }

  public boolean containsCol(final int col) {
    return start.getCol() <= col && col <= finish.getCol();
  }

  public boolean contains(final Spot s) {
    return contains(s.getCol(), s.getRow());
  }

  public boolean intersects(final Line line) {
    if (isVertical() == line.isVertical()) {
      return contains(line.getStart()) || line.contains(start);
    } else {
      if (isVertical()) {
        return containsRow(line.getStart().getRow()) && line.containsCol(start.getCol());
      } else {
        return containsCol(line.getStart().getCol()) && line.containsRow(start.getRow());
      }
    }
  }

  public List<Spot> spots() {
    if (isVertical()) {
      return IntStream.rangeClosed(start.getRow(), finish.getRow())
        .mapToObj(r -> new Spot(r, start.getCol()))
        .collect(toList());
    } else {
      return IntStream.rangeClosed(start.getCol(), finish.getCol())
        .mapToObj(c -> new Spot(start.getRow(), c))
        .collect(toList());
    }
  }

  public int length() {
    return isVertical()
      ? finish.getRow() - start.getRow() + 1
      : finish.getCol() - start.getCol() + 1;
  }
}
