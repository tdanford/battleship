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

import tdanford.battleship.PlacedShip;
import tdanford.battleship.games.BattleshipAction;
import tdanford.battleship.games.BattleshipResponse;
import tdanford.battleship.games.BattleshipState;
import tdanford.games.Player;

public abstract class AutomatedPlayer extends ManagedStatePlayer {

  public AutomatedPlayer(
    final String name,
    final Collection<PlacedShip> ships,
    final boolean verbose
  ) {
    super(name, ships, verbose);
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
