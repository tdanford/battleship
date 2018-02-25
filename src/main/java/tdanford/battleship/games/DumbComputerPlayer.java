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
import java.util.Random;

import tdanford.battleship.Board;
import tdanford.battleship.PlacedShip;
import tdanford.battleship.Spot;
import tdanford.games.Player;

public class DumbComputerPlayer extends ManagedStatePlayer {

  public DumbComputerPlayer(final Collection<PlacedShip> ships) {
    super("Computer", ships);
  }

  @Override
  public BattleshipAction chooseAction(final BattleshipState publicKnowledge) {
    final BattleshipState.PlayerState currentState = publicKnowledge.getPlayerState(this);

    final Board board = currentState.shots;

    Spot shot = null;

    do {
      shot = randomShot(board);
    } while(!board.isNoShot(shot));

    return new BattleshipAction(this, shot);
  }

  private static Random rand = new Random();

  private Spot randomShot(final Board board) {
    return new Spot(rand.nextInt(10), rand.nextInt(10));
  }

  @Override
  public void registerResponse(
    final Player<BattleshipAction, BattleshipState, BattleshipResponse> player,
    final BattleshipState publicKnowledge,
    final BattleshipAction act,
    final BattleshipResponse response
  ) {
    // don't need to register a response, since the result of our action will
    // arrive in the next BattleshipState value we see in chooseAction
  }
}
