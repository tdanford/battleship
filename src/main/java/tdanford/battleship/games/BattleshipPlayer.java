package tdanford.battleship.games;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tdanford.battleship.Board;
import tdanford.battleship.PlacedShip;
import tdanford.battleship.Ship;
import tdanford.battleship.Spot;
import tdanford.battleship.Terminal;
import tdanford.games.Player;

public interface BattleshipPlayer
  extends Player<BattleshipAction, BattleshipState, BattleshipResponse> {

  BattleshipResponse findResponse(BattleshipState initialState, BattleshipAction action);
}


