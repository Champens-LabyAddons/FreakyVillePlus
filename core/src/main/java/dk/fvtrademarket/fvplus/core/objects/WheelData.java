package dk.fvtrademarket.fvplus.core.objects;

/**
 * Repræsenterer data om en lykkehjuls forespørgsel på <a href="https://freakyville.dk">FreakyVilles Hjemmeside</a>
 */
public class WheelData {
  private final boolean success;
  private final Wheel wheel;

  public WheelData(boolean success, Wheel wheel) {
    this.success = success;
    this.wheel = wheel;
  }

  public boolean success() {
    return this.success;
  }

  public Wheel getWheel() {
    return this.wheel;
  }

  /**
   * Repræsenterer en mulighed på et lykkehjul
   */
  public class Option {
    private final String option;

    public Option(String option) {
      this.option = option;
    }

    public String getOption() {
      return this.option;
    }
  }

  /**
   * Repræsenterer et lykkehjul
   */
  public class Wheel {
    private final String id;
    private final Option[] options;
    private final String winner;
    private final String createdAt;

    public Wheel(String id, String winner, Option[] options, String createdAt) {
      this.id = id;
      this.winner = winner;
      this.options = options;
      this.createdAt = createdAt;
    }

    public String getId() {
      return this.id;
    }

    public String getWinner() {
      return this.winner;
    }

    public Option[] getOptions() {
      return this.options;
    }

    public String getCreatedAt() {
      return this.createdAt;
    }
  }
}
