package tdanford.battleship.games;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

  private final Map<BattleshipPlayer, PlayerState> players;

  public BattleshipState(final BattleshipPlayer... players) {
    this.players = new HashMap<>();
    for (final BattleshipPlayer player : players) {
      this.players.put(player, new PlayerState());
    }
  }

  public BattleshipState(final Map<BattleshipPlayer, PlayerState> states) {
    this.players = states;
  }

  public Collection<BattleshipPlayer> getPlayers() { return players.keySet(); }

  public PlayerState getPlayerState(final BattleshipPlayer player) { return players.get(player); }

  public BattleshipState withPlayerState(final BattleshipPlayer player, final PlayerState state) {
    return new BattleshipState(
      Maps.immutable.ofMap(players).newWithKeyValue(player, state).toMap()
    );
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

    public PlayerState withBoard(final Board newBoard) {
      return new PlayerState(newBoard, sunkShips);
    }

    public PlayerState withSunkShip(final Ship ship) {
      return new PlayerState(shots, Sets.mutable.ofAll(sunkShips).with(ship));
    }
  }
}

