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

package tdanford.battleship.players;

import java.util.Collection;
import java.util.Random;

import tdanford.battleship.Board;
import tdanford.battleship.PlacedShip;
import tdanford.battleship.ShipArrangement;
import tdanford.battleship.Spot;
import tdanford.battleship.games.BattleshipAction;
import tdanford.battleship.games.BattleshipState;

public class DumbComputerPlayer extends AutomatedPlayer {

  private static Random rand = new Random();

  public DumbComputerPlayer(
    final String name,
    final ShipArrangement ships,
    final boolean verbose
  ) {
    super(name, ships, verbose);
  }

  @Override
  public BattleshipAction chooseAction(final BattleshipState publicKnowledge) {
    final BattleshipState.PlayerState currentState = publicKnowledge.getPlayerState(this);

    final Board board = currentState.shots;

    Spot shot = null;

    do {
      shot = randomShot(board);
    } while(!board.isNoShot(shot));

    verboseSay("SHOT -> %s", shot);

    return new BattleshipAction(this, shot);
  }

  private Spot randomShot(final Board board) {
    return new Spot(rand.nextInt(10), 1 + rand.nextInt(10));
  }
}
