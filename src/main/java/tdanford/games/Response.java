package tdanford.games;

public interface Response<GS extends GameState> {

  GS updateState(GS state);
}
