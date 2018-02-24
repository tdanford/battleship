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


