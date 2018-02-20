package tdanford.battleship;

import java.util.ArrayList;
import java.util.Objects;

import com.google.common.base.Preconditions;

public class PlacedShip {

  private final Line location;
  private final Ship ship;

  private final ArrayList<Spot> hitSpots, unhitSpots;

  public PlacedShip(final Ship ship, final Line location) {
    Preconditions.checkArgument(ship != null);
    Preconditions.checkArgument(location != null);
    Preconditions.checkArgument(ship.getSize() == location.length());

    this.ship = ship;
    this.location = location;
    this.unhitSpots = new ArrayList<>(location.spots());
    this.hitSpots = new ArrayList<>();
  }

  public Ship getShip() { return ship; }

  public Line getLocation() { return location; }

  public int getNumHit() { return hitSpots.size(); }

  public void hitSpot(final Spot spot) {
    if (unhitSpots.contains(spot)) {
      hitSpots.add(spot);
      unhitSpots.remove(spot);
    }
  }

  public boolean isSunk() { return unhitSpots.isEmpty(); }

  public int hashCode() { return Objects.hash(ship, location); }

  public boolean equals(final Object o) {
    if (!(o instanceof PlacedShip)) { return false; }
    final PlacedShip p = (PlacedShip) o;
    return Objects.equals(ship, p.ship) && Objects.equals(location, p.location);
  }
}
