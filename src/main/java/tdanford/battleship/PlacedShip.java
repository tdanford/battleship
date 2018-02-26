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
import java.util.Objects;

import com.google.common.base.Preconditions;

public class PlacedShip {

  private final Line location;
  private final Ship ship;

  private final ArrayList<Spot> hitSpots, unhitSpots;

  public PlacedShip(final Ship ship, final Line location) {
    Preconditions.checkArgument(ship != null);
    Preconditions.checkArgument(location != null);

    Preconditions.checkArgument(ship.getSize() == location.length(), String.format(
      "Ship length %d must match line length %d", ship.getSize(), location.length()
    ));

    this.ship = ship;
    this.location = location;
    this.unhitSpots = new ArrayList<>(location.spots());
    this.hitSpots = new ArrayList<>();
  }

  public boolean overlaps(final Iterable<PlacedShip> ships) {
    for (final PlacedShip ship : ships) {
      if (location.intersects(ship.getLocation())) {
        return true;
      }
    }
    return false;
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
