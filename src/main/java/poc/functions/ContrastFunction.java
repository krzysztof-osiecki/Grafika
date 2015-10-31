package poc.functions;

import poc.tools.ImageHelper;


public class ContrastFunction extends BaseFunction {

  public ContrastFunction(int change) {
    integers = new int[256];
    for (int i = 0; i < 256; i++)
      integers[i] = recalculate(i, change);
  }

  private Integer recalculate(int value, int change) {
    double newA;
    if (change < 0) {
      newA = (127 + change) / 127.0;
    } else {
      newA = 127 / (double) (127 - change);
    }
    return ImageHelper.clamp(ImageHelper.integerize(value * newA - change), 0, 255);
  }
}
