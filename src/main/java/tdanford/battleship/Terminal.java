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
      return input.readLine();

    } catch (IOException e) {
      throw new RuntimeException("Couldn't read input", e);
    }
  }

  @Override
  public void info(final String message) {
    output.println(message);
  }
}
