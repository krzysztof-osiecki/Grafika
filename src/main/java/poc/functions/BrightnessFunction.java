package poc.functions;

import poc.tools.ImageHelper;


public class BrightnessFunction extends BaseFunction {

  public BrightnessFunction(int change) {
    integers = new int[256];
    for (int i = 0; i < 256; i++)
      integers[i] = recalculate(i, change);
  }

  private int recalculate(int value, int change) {
    return ImageHelper.clamp(value + change, 0, 255);
  }
}

