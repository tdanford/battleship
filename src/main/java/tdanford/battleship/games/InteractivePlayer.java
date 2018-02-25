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

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tdanford.battleship.Ship;
import tdanford.battleship.Spot;
import tdanford.battleship.Terminal;
import tdanford.games.Player;

public class InteractivePlayer implements BattleshipPlayer {

  private final Terminal terminal;

  public InteractivePlayer(final Terminal terminal) {
    this.terminal = terminal;
  }

  public String toString() { return "Human"; }

  @Override
  public BattleshipAction chooseAction(final BattleshipState publicKnowledge) {
    final Pattern pattern = Pattern.compile("([A-Ja-j])([1-9]|10)");
    Matcher matcher;
    String resp;
    do {
      final String message = publicKnowledge.getPlayerState(this).shots.printBoard() + "Shot?";
      resp = terminal.query(message).toUpperCase();
      matcher = pattern.matcher(resp);
    } while (!matcher.matches());

    final Spot spot = new Spot(resp);

    return new BattleshipAction(this, spot);
  }

  @Override
  public void registerResponse(
    final Player<BattleshipAction, BattleshipState, BattleshipResponse> player,
    final BattleshipState publicKnowledge,
    final BattleshipAction act,
    final BattleshipResponse response
  ) {
    terminal.info(response.toString());
  }

  @Override
  public BattleshipResponse findResponse(
    final BattleshipState initialState,
    final BattleshipAction action
  ) {
    final String message = String.format(
      "Computer: %s -> ?\n\t(enter: 'hit' or 'miss')", action.spot
    );
    final String hitOrMiss = terminal.query(message).toLowerCase();

    if ("hit".equals(hitOrMiss)) {
      final String shipSunk = terminal.query("Ship sunk? (enter ship name, or blank)")
        .toUpperCase();

      try {
        final Ship ship = Ship.valueOf(shipSunk);
        return new BattleshipResponse(action.player, action.spot, true, Optional.of(ship));

      } catch(IllegalArgumentException e) {
        return new BattleshipResponse(action.player, action.spot, true, Optional.empty());
      }

    } else {
      return new BattleshipResponse(action.player, action.spot, false, Optional.empty());
    }
  }
}
