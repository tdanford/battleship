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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

public class SpotTest {

  @Test
  public void testGetters() {
    assertThat(new Spot("A1").getRow()).isEqualTo(1);
    assertThat(new Spot("A1").getCol()).isEqualTo(0);

    assertThat(new Spot("A10").getRow()).isEqualTo(10);
    assertThat(new Spot("A10").getCol()).isEqualTo(0);
  }

  @Test
  public void testConstructorPreconditions() {
    assertThatThrownBy(() -> new Spot("A11")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new Spot("K3")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new Spot(null)).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new Spot("")).isInstanceOf(IllegalArgumentException.class);
  }
}
