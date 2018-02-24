package tdanford.battleship.games;

import java.util.List;

import tdanford.games.GameEngine;

public class BattleshipEngine
  implements GameEngine<BattleshipAction, BattleshipState, BattleshipResponse, BattleshipPlayer> {

  @Override
  public BattleshipResponse findResponse(
    final BattleshipState initialState,
    final List<BattleshipPlayer> players,
    final int playerIdx,
    final BattleshipAction action
  ) {
    return players.get(1-playerIdx).findResponse(initialState, action);
  }
}
