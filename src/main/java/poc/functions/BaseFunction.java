package poc.functions;

import poc.histogram.HistogramData;
import poc.tools.ImageHelper;

import java.util.function.Function;

public abstract class BaseFunction implements Function<Integer, Integer> {
  protected int[] integers;

  @Override
  public Integer apply(Integer rgb) {
    // Rozbior koloru na skladowe R, G, B
    int r = ImageHelper.jred(rgb);
    int g = ImageHelper.jgreen(rgb);
    int b = ImageHelper.jblue(rgb);

    //odswiezenie danych histogramu
    HistogramData.RED[integers[r]]++;
    HistogramData.GREEN[integers[g]]++;
    HistogramData.BLUE[integers[b]]++;
    int gray = ImageHelper.clamp(
        ImageHelper.integerize(0.299 * integers[r] + 0.587 * integers[g] + 0.114 * integers[b]), 0, 255);
    HistogramData.GRAY_SCALE[gray]++;

    // Zlozenie skladowych R, G, B w jeden kolor i zwrocenie jego wartosci
    return ImageHelper.jrgb(integers[r], integers[g], integers[b]);
  }
}
