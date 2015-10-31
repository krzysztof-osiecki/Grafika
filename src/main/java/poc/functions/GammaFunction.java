package poc.functions;

import poc.tools.ImageHelper;

public class GammaFunction extends BaseFunction {

  public GammaFunction(double change) {
    integers = new int[256];
    for (int i = 0; i < 256; i++)
      integers[i] = recalculate(i, change);
  }

  private int recalculate(int value, double change) {
    double v = Math.pow(value / (double) 255, change) * 255;
    return ImageHelper.clamp(ImageHelper.integerize(v), 0, 255);
  }
}
