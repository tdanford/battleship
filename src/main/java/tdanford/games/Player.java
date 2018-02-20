package tdanford.games;

public interface Player<A extends Action, GS extends GameState, R extends Response> {

  A chooseAction(GS publicKnowledge);

  void registerResponse(Player<A, GS, R> player, GS publicKnowledge, A act, R response);
}
