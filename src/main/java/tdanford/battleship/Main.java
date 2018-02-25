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
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

import com.google.common.base.Preconditions;

import tdanford.battleship.games.BattleshipLoop;
import tdanford.battleship.games.BattleshipPlayer;
import tdanford.battleship.games.DumbComputerPlayer;
import tdanford.battleship.games.InteractivePlayer;

public class Main {

  public static void main(String[] args) {

    final BattleshipPlayer player1 = new InteractivePlayer(new StandardTerminal());
    final BattleshipPlayer player2 = new DumbComputerPlayer(randomShipPlacement());
    
    final BattleshipLoop loop = new BattleshipLoop(player1, player2);

    Optional<BattleshipPlayer> winner;

    do {
      winner = loop.turn();

    } while (!winner.isPresent());

    System.out.println(String.format("%s is the winner", winner));
  }

  private static Collection<PlacedShip> randomShipPlacement() {
    ArrayList<PlacedShip> ships = new ArrayList<>();

    for (Ship s : Ship.values()) {
      final PlacedShip placed = randomPlacedShip(s, ships);
      ships.add(placed);
    }

    return ships;
  }

  private static Random rand = new Random();

  private static PlacedShip randomPlacedShip(
    final Ship ship,
    final Collection<PlacedShip> previous
  ) {
    PlacedShip placed;
    do {
      placed = new PlacedShip(ship, randomLine(ship.getSize()));
    } while(overlapsShip(placed, previous));

    return placed;
  }

  private static boolean overlapsShip(final PlacedShip ship, final Collection<PlacedShip> placed) {
    for (final PlacedShip ps : placed) {
      if (ps.getLocation().intersects(ship.getLocation())) {
        return true;
      }
    }
    return false;
  }

  private static Line randomLine(final int length) {
    Preconditions.checkArgument(length > 0);

    boolean vertical = rand.nextBoolean();

    int c1 = vertical ? rand.nextInt(10) : rand.nextInt(10 - length);
    int r1 = vertical ? rand.nextInt(10 - length) + 1 : rand.nextInt(10) + 1;

    int c2 = vertical ? c1 : c1 + length - 1;
    int r2 = vertical ? r1 + length - 1 : r1;

    final Line line = new Line(new Spot(c1, r1), new Spot(c2, r2));

    Preconditions.checkState(line.length() == length,
      String.format("Line %s length %d must equal " +
      "parameter length %d", line, line.length(), length));

    return line;
  }
}
