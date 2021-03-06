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

import java.util.Arrays;

import tdanford.games.GameLoop;

public class BattleshipLoop extends GameLoop<BattleshipAction, BattleshipState,
  BattleshipResponse, BattleshipPlayer, BattleshipEngine> {

  public BattleshipLoop(final BattleshipPlayer player1, final BattleshipPlayer player2) {
    super(
      new BattleshipEngine(),
      new BattleshipState(player1, player2),
      Arrays.asList(player1, player2)
    );
  }
}
