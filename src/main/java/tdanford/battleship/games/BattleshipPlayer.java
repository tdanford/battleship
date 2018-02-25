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

import java.util.Objects;

import tdanford.games.Player;

public abstract class BattleshipPlayer
  implements Player<BattleshipAction, BattleshipState, BattleshipResponse> {

  private final String name;

  public BattleshipPlayer(final String name) {
    this.name = name;
  }

  public String getName() { return name; }

  public String toString() { return name; }

  public int hashCode() { return Objects.hash(name); }

  public boolean equals(final Object o) {
    if (!(o instanceof BattleshipPlayer)) { return false; }
    final BattleshipPlayer p = (BattleshipPlayer) o;
    return Objects.equals(name, p.name);
  }

  abstract BattleshipResponse findResponse(BattleshipState initialState, BattleshipAction action);
}


