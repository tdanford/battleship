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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.factory.Sets;

import tdanford.battleship.Board;
import tdanford.battleship.Ship;
import tdanford.games.GameState;

/**
 * The state of a battleship game is the state of all its players
 */
public class BattleshipState implements GameState {

  private final BattleshipPlayer[] playerArray;
  private final Map<BattleshipPlayer, PlayerState> players;

  public BattleshipState(final BattleshipPlayer... players) {
    this.playerArray = players;
    this.players = new HashMap<>();
    for (final BattleshipPlayer player : players) {
      this.players.put(player, new PlayerState());
    }
  }

  public BattleshipState(
    final BattleshipPlayer[] array,
    final Map<BattleshipPlayer, PlayerState> states
  ) {
    this.playerArray = array;
    this.players = states;
  }

  public Collection<BattleshipPlayer> getPlayers() { return players.keySet(); }

  public PlayerState getPlayerState(final BattleshipPlayer player) { return players.get(player); }

  public BattleshipState withPlayerState(final BattleshipPlayer player, final PlayerState state) {
    return new BattleshipState(
      playerArray,
      Maps.immutable.ofMap(players).newWithKeyValue(player, state).toMap()
    );
  }

  @Override
  public Optional<BattleshipPlayer> winningPlayer() {

    for (int i = 0; i < 2; i++) {
      if (players.get(playerArray[i]).isLost()) {
        return Optional.of(playerArray[1 - i]);
      }
    }

    return Optional.empty();
  }

  public static class PlayerState {

    public Board shots;
    public Set<Ship> sunkShips;

    public PlayerState() {
      this.shots = new Board();
      this.sunkShips = new HashSet<>();
    }

    public PlayerState(final Board shots, final Set<Ship> sunk) {
      this.shots = shots;
      this.sunkShips = sunk;
    }

    public boolean isLost() {
      return sunkShips.size() == 5;
    }

    public PlayerState withBoard(final Board newBoard) {
      return new PlayerState(newBoard, sunkShips);
    }

    public PlayerState withSunkShip(final Ship ship) {
      return new PlayerState(shots, Sets.mutable.ofAll(sunkShips).with(ship));
    }
  }
}

