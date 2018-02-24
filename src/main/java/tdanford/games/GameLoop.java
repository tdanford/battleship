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
  private Eng engine;

  public GameLoop(
    final Eng gameEngine,
    final GS initialState,
    final List<P> players
  ) {
    this.engine = gameEngine;
    this.state = initialState;
    this.players = new ArrayList<>(players);
    this.nextPlayer = 0;
  }

  public boolean turn() {
    final P acting = players.get(nextPlayer);

    final A action = acting.chooseAction(state);

    final R response = engine.findResponse(state, players, nextPlayer, action);

    for (P player : players) {
      player.registerResponse(acting, state, action, response);
    }

    nextPlayer = (nextPlayer + 1) % players.size();

    state = response.updateState(state);

    return true;
  }

}
