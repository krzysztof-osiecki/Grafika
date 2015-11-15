package poc.functions;

public class IdentityFunction extends BaseFunction {
  private static final int[] staticIntegers;

  static {
    staticIntegers = new int[256];
    for (int i = 0; i < 256; i++)
      staticIntegers[i] = i;
  }

  public IdentityFunction() {
    values = staticIntegers;
  }

}
