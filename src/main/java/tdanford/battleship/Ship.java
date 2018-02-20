package tdanford.battleship;

public enum Ship {

  CARRIER(5),
  BATTLESHIP(4),
  DESTROYER(3),
  SUBMARINE(3),
  PATROL_BOAT(2)
  ;

  private final int size;

  Ship(final int size) {
    this.size = size;
  }

  public int getSize() { return size; }
}
