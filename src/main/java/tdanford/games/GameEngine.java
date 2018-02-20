package tdanford.games;

import java.util.List;

public interface GameEngine<
  A extends Action,
  GS extends GameState,
  R extends Response<GS>,
  P extends Player<A, GS, R>
  > {

  R findResponse(GS initialState, List<P> players, int playerIdx, A action);
}
