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

import java.util.stream.Stream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShipArrangementTest {

  private static final Logger LOG = LoggerFactory.getLogger(ShipArrangementTest.class);

  @Test
  public void testShipArrangementEnumeration() {
    assertThat(ShipArrangement.enumerateArrangements(Ship.CARRIER)).hasSize(120);

    final RandomUtils utils = new RandomUtils();
    final long startTime = System.currentTimeMillis();
    final long N = 10000L;
    final Stream<ShipArrangement> arrs =
      //ShipArrangement.enumerateArrangements(Ship.values()).limit(N);
      utils.randomShipArrangements(N, arr -> true);
    //arrs.forEach(System.out::println);
    assertThat(arrs.count()).isEqualTo(N);
    final long endTime = System.currentTimeMillis();

    LOG.info("Generating {} ship arrangements took {} seconds", N, (endTime-startTime) / 1000.0);
  }
}
