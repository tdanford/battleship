package tdanford.battleship;

import java.util.Arrays;
import java.util.Collection;

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
    return Arrays.asList(
      new PlacedShip(Ship.BATTLESHIP, new Line(new Spot("C1"), new Spot("C4")))
    );
  }
}
