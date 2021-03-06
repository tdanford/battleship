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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public interface Terminal {

  String query(String prompt);

  void info(String message);
}

class StandardTerminal implements Terminal {

  private final BufferedReader input;

  private final PrintWriter output;

  public StandardTerminal() {
    this.output = new PrintWriter(System.out);
    this.input = new BufferedReader(new InputStreamReader(System.in, Charset.defaultCharset()));
  }

  @Override
  public String query(final String prompt) {
    output.println(prompt);
    output.print("> "); output.flush();

    try {
      final String read = input.readLine();

      if (read == null) {
        System.exit(0);
      }

      return read;

    } catch (IOException e) {
      throw new RuntimeException("Couldn't read input", e);
    }
  }

  @Override
  public void info(final String message) {
    output.println(message);
  }
}
