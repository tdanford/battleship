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
    Matcher matcher = null;
    do {
      final String resp = terminal.query("Shot?");
      matcher = pattern.matcher(resp);
    } while (!matcher.matches());

    final Spot spot = new Spot(
      matcher.group(1).toUpperCase().charAt(0) - 'A',
      Integer.parseInt(matcher.group(2))
    );

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
    final String hitOrMiss = terminal.query(action.spot.toString()).toLowerCase();

    if ("hit".equals(hitOrMiss)) {
      final String shipSunk = terminal.query("Sunk?").toUpperCase();

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
