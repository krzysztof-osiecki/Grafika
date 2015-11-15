package poc.functions;

import poc.tools.ImageHelper;

public class LogFunction extends BaseFunction {

  public LogFunction(int change) {
    values = new int[256];
    for (int i = 0; i < 256; i++)
      values[i] = recalculate(i, change);
  }

  private int recalculate(int value, int change) {
    double v = change * Math.log(1 + value);
    return ImageHelper.clamp(ImageHelper.integerize(v), 0, 255);
  }
}
