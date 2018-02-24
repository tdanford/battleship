package tdanford.battleship;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import tdanford.battleship.games.BattleshipLoop;
import tdanford.battleship.games.BattleshipPlayer;
import tdanford.battleship.games.ComputerPlayer;
import tdanford.battleship.games.InteractivePlayer;

public class Main {

  public static void main(String[] args) {

    final BattleshipPlayer player1 = new InteractivePlayer(new StandardTerminal());
    final BattleshipPlayer player2 = new ComputerPlayer(randomShipPlacement());
    
    final BattleshipLoop loop = new BattleshipLoop(player1, player2);

    while(loop.turn()) {
      // do nothing
    }
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
    boolean vertical = rand.nextBoolean();

    int c1 = vertical ? rand.nextInt(10) : rand.nextInt(10 - length);
    int r1 = vertical ? rand.nextInt(10 - length) + 1 : rand.nextInt(10) + 1;

    int c2 = vertical ? c1 : c1 + length - 1;
    int r2 = vertical ? r1 + length - 1 : r1;

    return new Line(new Spot(c1, r2), new Spot(c2, r2));
  }
}
