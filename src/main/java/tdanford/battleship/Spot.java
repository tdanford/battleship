package tdanford.battleship;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;

public class Spot implements Comparable<Spot> {

  private static Pattern PATTERN = Pattern.compile("([A-Ja-j])([1-9]|10)");

  private final int row;
  private final int col;

  public Spot(final String str) {
    final Matcher matcher = PATTERN.matcher(str);
    if (!matcher.matches()) { throw new IllegalArgumentException(str); }
    this.col = matcher.group(1).toUpperCase().charAt(0) - 'A';
    this.row = Integer.parseInt(matcher.group(2));
  }

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
    return Character.toChars('A' + col)[0] + String.valueOf(row);
  }

  public int compareTo(final Spot s) {
    if (s.col != col) {
      return Integer.compare(col, s.col);
    } else {
      return Integer.compare(row, s.row);
    }
  }
}
