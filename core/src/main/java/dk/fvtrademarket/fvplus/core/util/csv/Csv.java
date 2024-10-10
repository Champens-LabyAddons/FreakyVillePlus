package dk.fvtrademarket.fvplus.core.util.csv;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * En meget simpel CSV læser, som kan læse en fil og returnere en liste af linjer.
 * Hver linje er en liste af strenge, som er adskilt af kommaer.
 *
 * @since 1.0.0
 */
public class Csv {
  private final BufferedReader bufferedReader;

  public Csv(BufferedReader bufferedReader) {
    this.bufferedReader = bufferedReader;
  }

  public List<String[]> readALl() {
    List<String[]> lines = new ArrayList<>();
    for (Object s : bufferedReader.lines().toArray()) {
      lines.add(((String) s).split(","));
    }
    return lines;
  }
}
