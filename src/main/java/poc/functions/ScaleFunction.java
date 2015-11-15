package poc.functions;

import poc.tools.Channel;
import poc.tools.ImageHelper;

public class ScaleFunction extends BaseFunction {

  public ScaleFunction(int a, int b, int c, int d, Channel channel) {
    this.channel = channel;
    double k = (d - c) / (double) (b - a);
    for (int i = 0; i < 255; i++) {
      values[i] = ImageHelper.clamp(ImageHelper.integerize(k * (i - a) + c), 0, 255);
    }
  }
}
