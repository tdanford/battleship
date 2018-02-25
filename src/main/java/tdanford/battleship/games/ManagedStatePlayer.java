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

package tdanford.battleship.games;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import tdanford.battleship.PlacedShip;
import tdanford.battleship.Spot;

public abstract class ManagedStatePlayer implements BattleshipPlayer {

  private final String name;
  private final Set<PlacedShip> ships;

  public ManagedStatePlayer(final String name, final Collection<PlacedShip> ships) {
    this.name = name;
    this.ships = new HashSet<>(ships);
  }

  public String toString() { return name; }

  private Optional<PlacedShip> findHitShip(final Spot spot) {
    for (final PlacedShip ship : ships) {
      if (ship.getLocation().contains(spot)) {
        return Optional.of(ship);
      }
    }

    return Optional.empty();
  }

  @Override
  public BattleshipResponse findResponse(
    final BattleshipState initialState,
    final BattleshipAction action
  ) {
    final Optional<PlacedShip> hitShip = findHitShip(action.spot);

    if (hitShip.isPresent()) {

      final PlacedShip ship = hitShip.get();
      ship.hitSpot(action.spot);

      if (ship.isSunk()) {
        return new BattleshipResponse(action.player, action.spot, true, Optional.of(ship.getShip()));
      } else {
        return new BattleshipResponse(action.player, action.spot, true, Optional.empty());
      }

    } else {
      return new BattleshipResponse(action.player, action.spot, false, Optional.empty());
    }
  }
}
