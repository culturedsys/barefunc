package systems.cultured.barefunc;

public record Unit() {
  private static final Unit UNIT = new Unit();

  public static Unit unit() {
    return UNIT;
  }
}
