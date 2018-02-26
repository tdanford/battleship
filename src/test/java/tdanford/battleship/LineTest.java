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

import org.junit.Test;

public class LineTest {

  public static Line line(final String first, final String second) {
    return new Line(new Spot(first), new Spot(second));
  }

  @Test
  public void testLineEnumeration() {
    assertThat(Line.enumerateLines(1)).hasSize(100);
    assertThat(Line.enumerateLines(5)).hasSize(120);
  }

  @Test
  public void testStartFinishOrdering() {
    assertThat(line("A1", "A5").getStart()).isEqualTo(new Spot("A1"));
    assertThat(line("A1", "C1").getStart()).isEqualTo(new Spot("A1"));

    assertThat(line("A1", "A5").getFinish()).isEqualTo(new Spot("A5"));
    assertThat(line("A1", "C1").getFinish()).isEqualTo(new Spot("C1"));

    assertThat(line("A5", "A1").getStart()).isEqualTo(new Spot("A1"));
    assertThat(line("C1", "A1").getStart()).isEqualTo(new Spot("A1"));

    assertThat(line("A5", "A1").getFinish()).isEqualTo(new Spot("A5"));
    assertThat(line("C1", "A1").getFinish()).isEqualTo(new Spot("C1"));
  }

  @Test
  public void testLineLength() {
    assertThat(line("A1", "A1").length()).isEqualTo(1);
    assertThat(line("A1", "A5").length()).isEqualTo(5);
  }
}
