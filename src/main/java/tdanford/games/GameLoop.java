package tdanford.games;

import java.util.ArrayList;
import java.util.List;

public class GameLoop<
  GS extends GameState,
  A extends Action,
  R extends Response<GS>,
  P extends Player<A, GS, R>,
  Eng extends GameEngine<A, GS, R, P>
  > {

  private final List<P> players;
  private GS state;
  private int nextPlayer;
  private GameEngine<A, GS, R, P> engine;

  public GameLoop(
    final Eng gameEngine,
    final GS initialState,
    final GameEngine <A, GS, R, P> engine,
    final List<P> players
  ) {
    this.players = new ArrayList<>(players);
    this.state = initialState;
    this.nextPlayer = 0;
    this.engine = engine;
  }

  public void turn() {
    final P acting = players.get(nextPlayer);

    final A action = acting.chooseAction(state);

    final R response = engine.findResponse(state, players, nextPlayer, action);

    for (P player : players) {
      player.registerResponse(acting, state, action, response);
    }

    nextPlayer = (nextPlayer + 1) % players.size();

    state = response.updateState(state);
  }

}
