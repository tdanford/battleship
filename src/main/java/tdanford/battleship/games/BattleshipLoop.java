package tdanford.battleship.games;

import java.util.Arrays;

import tdanford.games.GameLoop;

public class BattleshipLoop extends GameLoop<BattleshipState, BattleshipAction,
  BattleshipResponse, BattleshipPlayer, BattleshipEngine> {

  public BattleshipLoop(final BattleshipPlayer player1, final BattleshipPlayer player2) {
    super(
      new BattleshipEngine(),
      new BattleshipState(player1, player2),
      Arrays.asList(player1, player2)
    );
  }
}
