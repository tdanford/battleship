package tdanford.battleship.games;

import tdanford.battleship.Spot;
import tdanford.games.Action;

public class BattleshipAction implements Action {

  public final BattleshipPlayer player;
  public final Spot spot;

  public BattleshipAction(final BattleshipPlayer player, final Spot spot) {
    this.player = player;
    this.spot = spot;
  }
}
