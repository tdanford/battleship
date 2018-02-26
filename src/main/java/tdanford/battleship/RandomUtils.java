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

import java.util.Collection;
import java.util.Random;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

public class RandomUtils {

  private Random rand;

  public RandomUtils() {
    this.rand = new Random();
  }

  public RandomUtils(final long seed) {
    this.rand = new Random(seed);
  }

  private Spot randomSpot() {
    return new Spot(rand.nextInt(10), 1 + rand.nextInt(10));
  }

  public Line randomLine(final int length) {
    Preconditions.checkArgument(length > 0);

    boolean vertical = rand.nextBoolean();

    int c1 = vertical ? rand.nextInt(10) + 1 : rand.nextInt(10 - length) + 1;
    int r1 = vertical ? rand.nextInt(10 - length) : rand.nextInt(10);

    int c2 = vertical ? c1 : c1 + length - 1;
    int r2 = vertical ? r1 + length - 1 : r1;

    final Line line = new Line(new Spot(r1, c1), new Spot(r2, c2));

    Preconditions.checkState(line.length() == length,
      String.format("Line %s length %d must equal " +
        "parameter length %d", line, line.length(), length));

    return line;
  }

  public PlacedShip randomPlacedShip(
    final Ship ship,
    final Predicate<PlacedShip> predicate
  ) {
    PlacedShip placed;
    do {
      placed = new PlacedShip(ship, randomLine(ship.getSize()));
    } while (!predicate.apply(placed));

    return placed;
  }

}
