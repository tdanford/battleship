package tdanford.battleship;

import java.util.Optional;

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
    this.player = player;
    this.shot = shot;
    this.isHit = isHit;
    this.sunk = sunk;
  }

  public BattleshipPlayer getPlayer() { return player; }

  public Spot getShot() { return shot; }

  public boolean isHit() { return isHit; }

  public Optional<Ship> getSunk() { return sunk; }

  @Override
  public BattleshipState updateState(final BattleshipState state) {
    return null;
  }
}
