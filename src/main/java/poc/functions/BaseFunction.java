package poc.functions;

import poc.histogram.HistogramData;
import poc.tools.Channel;
import poc.tools.ImageHelper;

import java.util.function.Function;

public abstract class BaseFunction implements Function<Integer, Integer> {
  protected int[] values = new int[256];
  protected Channel channel = Channel.ALL;

  @Override
  public Integer apply(Integer rgb) {
    // Rozbior koloru na skladowe R, G, B
    int r = ImageHelper.jred(rgb);
    int g = ImageHelper.jgreen(rgb);
    int b = ImageHelper.jblue(rgb);

    switch (channel) {
      case ALL:
      case GRAY:
        r = values[r];
        g = values[g];
        b = values[b];
        break;
      case RED:
        r = values[r];
        break;
      case GREEN:
        g = values[g];
        break;
      case BLUE:
        b = values[b];
        break;
    }

    //odswiezenie danych histogramu
    HistogramData.RED[r]++;
    HistogramData.GREEN[g]++;
    HistogramData.BLUE[b]++;
    int gray = ImageHelper.clamp(
        ImageHelper.integerize(0.299 * r + 0.587 * g + 0.114 * b), 0, 255);
    HistogramData.GRAY_SCALE[gray]++;

    // Zlozenie skladowych R, G, B w jeden kolor i zwrocenie jego wartosci
    return ImageHelper.jrgb(r, g, b);
  }
}
