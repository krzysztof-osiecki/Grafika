package poc.functions;

public class NegativeFunction extends BaseFunction {
  private static final int[] staticIntegers;

  static {
    staticIntegers = new int[256];
    for (int i = 256; i > 0; i--)
      staticIntegers[256 - i] = i-1;
  }

  public NegativeFunction() {
    values = staticIntegers;
  }

}
