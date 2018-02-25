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

package tdanford.battleship;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.stream.IntStream;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class SpotsTest {

  private static final int N = 100;

  private static final Random RAND = new Random();

  private static Spot randomSpot() {
    final int col = RAND.nextInt(10);
    final int row = 1 + RAND.nextInt(10);
    return new Spot(col, row);
  }

  @DataPoints
  public static Spot[] randomSpots() {
    return IntStream.range(0, N).mapToObj(i -> randomSpot()).toArray(Spot[]::new);
  }

  @Theory
  public void adjacenciesAreAdjacent(final Spot s) {
    assertThat(s.adjacencies()).allMatch(s::isAdjacent);
  }

  @Theory
  public void spotsAreComparablyZeroWithThemselves(final Spot s) {
    assertThat(s.compareTo(new Spot(s.getCol(), s.getRow()))).isEqualTo(0);
  }
}
