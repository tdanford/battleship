package tdanford.battleship.games;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import tdanford.battleship.Board;
import tdanford.battleship.PlacedShip;
import tdanford.battleship.Spot;
import tdanford.games.Player;

public class ComputerPlayer implements BattleshipPlayer {

  private Set<PlacedShip> ships;

  public ComputerPlayer(final Collection<PlacedShip> ships) {
    this.ships = new HashSet<>(ships);
  }

  public String toString() { return "Computer"; }

  private Optional<PlacedShip> findHitShip(final Spot spot) {
    for (final PlacedShip ship : ships) {
      if (ship.getLocation().contains(spot)) {
        return Optional.of(ship);
      }
    }

    return Optional.empty();
  }

  @Override
  public BattleshipAction chooseAction(final BattleshipState publicKnowledge) {
    final BattleshipState.PlayerState currentState = publicKnowledge.getPlayerState(this);

    final Board board = currentState.shots;

    Spot shot = null;

    do {
      shot = randomShot(board);
    } while(!board.isNoShot(shot));

    return new BattleshipAction(this, shot);
  }

  private static Random rand = new Random();

  private Spot randomShot(final Board board) {
    return new Spot(rand.nextInt(10), rand.nextInt(10));
  }

  @Override
  public void registerResponse(
    final Player<BattleshipAction, BattleshipState, BattleshipResponse> player,
    final BattleshipState publicKnowledge,
    final BattleshipAction act,
    final BattleshipResponse response
  ) {
  }

  @Override
  public BattleshipResponse findResponse(
    final BattleshipState initialState,
    final BattleshipAction action
  ) {
    final Optional<PlacedShip> hitShip = findHitShip(action.spot);

    if (hitShip.isPresent()) {

      final PlacedShip ship = hitShip.get();
      ship.hitSpot(action.spot);

      if (ship.isSunk()) {
        return new BattleshipResponse(action.player, action.spot, true, Optional.of(ship.getShip()));
      } else {
        return new BattleshipResponse(action.player, action.spot, true, Optional.empty());
      }

    } else {
      return new BattleshipResponse(action.player, action.spot, false, Optional.empty());
    }
  }
}
