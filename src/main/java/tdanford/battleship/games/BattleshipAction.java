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
