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

import com.google.common.base.Preconditions;

import tdanford.battleship.Board;
import tdanford.battleship.Ship;
import tdanford.battleship.Spot;
import tdanford.games.Response;

public class BattleshipResponse implements Response<BattleshipState> {

  private final BattleshipPlayer player;
  private final Spot shot;
  private final Boolean isHit;
  private final Optional<Ship> sunk;

  public BattleshipResponse(
    final BattleshipPlayer player,
    final Spot shot,
    final Boolean isHit,
    final Optional<Ship> sunk
  ) {
    Preconditions.checkArgument(player != null, "Player cannot be null");
    Preconditions.checkArgument(shot != null);
    Preconditions.checkArgument(isHit || !sunk.isPresent());

    this.player = player;
    this.shot = shot;
    this.isHit = isHit;
    this.sunk = sunk;
  }

  public String toString() {
    return String.format("%s Player: %s -> %s %s",
      player.toString(),
      shot.toString(),
      isHit ? "HIT" : "MISS",
      sunk.map(String::valueOf).orElse("")
    );
  }

  public BattleshipPlayer getPlayer() { return player; }

  public Spot getShot() { return shot; }

  public boolean isHit() { return isHit; }

  public Optional<Ship> getSunk() { return sunk; }

  @Override
  public BattleshipState updateState(final BattleshipState state) {

    BattleshipState.PlayerState playerState = state.getPlayerState(player);

    Preconditions.checkState(playerState != null);

    final Board newBoard = playerState.shots.withShot(shot, isHit);

    playerState = playerState.withBoard(newBoard);

    playerState = sunk.map(playerState::withSunkShip).orElse(playerState);

    return state.withPlayerState(player, playerState);
  }
}
