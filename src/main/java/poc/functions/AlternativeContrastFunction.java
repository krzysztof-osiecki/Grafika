package poc.functions;

import poc.tools.ImageHelper;

public class AlternativeContrastFunction extends BaseFunction {


  public AlternativeContrastFunction(int change) {
    values = new int[256];
    for (int i = 0; i < 256; i++)
      values[i] = recalculate(i, change);
  }

  private int recalculate(int value, int change) {
    if (change < 0) {
      return decreaseContrast(value, change);
    } else {
      return increaseContrast(value, change);
    }
  }

  private int decreaseContrast(int value, Integer change) {
    if (value < 127 + change) {
      return ImageHelper.integerize((127 / (double) (127 + change)) * value);
    } else if (value > 127 - change) {
      return ImageHelper.integerize((127 * value + 255 * change) / (double) (127 + change));
    }
    return 127;
  }

  private int increaseContrast(int value, Integer change) {
    double newA = (127 - change) / 127.0;
    return value < 127 ? ImageHelper.integerize(newA * value) : ImageHelper.integerize(newA * value + 2 * change);
  }

}
