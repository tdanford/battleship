package tdanford.battleship;

import java.util.Objects;

import com.google.common.base.Preconditions;

public class Spot implements Comparable<Spot> {

  private final int row;
  private final int col;

  public Spot(final int col, final int row) {
    Preconditions.checkArgument(col >= 0);
    Preconditions.checkArgument(row >= 0);

    this.row = row;
    this.col = col;
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
    return ('A' + col) + String.valueOf(row);
  }

  public int compareTo(final Spot s) {
    if (s.col != col) {
      return Integer.compare(col, s.col);
    } else {
      return Integer.compare(row, s.row);
    }
  }
}
