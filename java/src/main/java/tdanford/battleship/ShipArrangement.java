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
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.eclipse.collections.impl.block.factory.Comparators;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A collection of {@link PlacedShip} values in a legal arrangement (i.e. no overlaps)
 */
public class ShipArrangement {

  private ArrayList<PlacedShip> ships;

  @JsonCreator
  public ShipArrangement(
    @JsonProperty final List<PlacedShip> ships
  ) {
    this.ships = new ArrayList<>(ships);
    this.ships.sort(Comparators.naturalOrder());
  }

  public List<PlacedShip> getShips() { return ships; }

  public boolean contains(final Spot spot) {
    return any(s -> s.getLocation().contains(spot));
  }

  public boolean intersects(final Line line) {
    return any(s -> s.getLocation().intersects(line));
  }

  public boolean any(final Predicate<PlacedShip> p) {
    for (final PlacedShip s : ships) {
      if (p.test(s)) {
        return true;
      }
    }
    return false;
  }

  public boolean all(final Predicate<PlacedShip> p) {
    for (final PlacedShip s : ships) {
      if (!p.test(s)) {
        return false;
      }
    }
    return true;
  }

  public int size() { return ships.size(); }

  public int hashCode() {
    return Objects.hash(ships);
  }

  public boolean equals(final Object o) {
    if (!( o instanceof ShipArrangement )) { return false; }
    final ShipArrangement a = (ShipArrangement) o;
    return Objects.deepEquals(ships, a.ships);
  }

  public String toString() { return String.valueOf(ships); }
}
