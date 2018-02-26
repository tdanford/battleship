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

package tdanford.battleship;

import java.util.Optional;

import tdanford.battleship.games.BattleshipLoop;
import tdanford.battleship.games.BattleshipPlayer;
import tdanford.battleship.players.DumbComputerPlayer;
import tdanford.battleship.players.InteractivePlayer;

public class Main {

  public static void main(String[] args) {

    final boolean computersOnly = args.length > 0 && args[0].equals("computer");
    final RandomUtils rand = new RandomUtils();

    final BattleshipPlayer player1 =
      computersOnly
        ? new DumbComputerPlayer("computer1", rand.randomShipPlacement(), true)
        : new InteractivePlayer(new StandardTerminal());

    final BattleshipPlayer player2 =
      new DumbComputerPlayer("computer2", rand.randomShipPlacement(), computersOnly);
    
    final BattleshipLoop loop = new BattleshipLoop(player1, player2);

    Optional<BattleshipPlayer> winner;

    do {
      winner = loop.turn();

    } while (!winner.isPresent());

    System.out.println(String.format("%s is the winner", winner.get().getName()));
  }


}
