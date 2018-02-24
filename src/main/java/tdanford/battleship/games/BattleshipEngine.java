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
