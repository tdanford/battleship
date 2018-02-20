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
