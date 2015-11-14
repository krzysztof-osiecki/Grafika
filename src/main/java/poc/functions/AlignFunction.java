package poc.functions;

import poc.histogram.HistogramData;
import poc.tools.Channel;
import poc.tools.ImageHelper;

public class AlignFunction extends BaseFunction {
  private final double[] cumulativeRed = new double[256];
  private final double[] cumulativeGreen = new double[256];
  private final double[] cumulativeBlue = new double[256];
  private final int[] redValues = new int[256];
  private final int[] greenValues = new int[256];
  private final int[] blueValues = new int[256];
  private Channel channel;

  public AlignFunction(Channel channel) {
    this.channel = channel;
    cumulativeRed[0] = HistogramData.RED[0];
    cumulativeGreen[0] = HistogramData.GREEN[0];
    cumulativeBlue[0] = HistogramData.BLUE[0];
    for (int i = 1; i < 256; i++) {
      cumulativeRed[i] = cumulativeRed[i - 1] + HistogramData.RED[i];
      cumulativeGreen[i] = cumulativeGreen[i - 1] + HistogramData.GREEN[i];
      cumulativeBlue[i] = cumulativeBlue[i - 1] + HistogramData.BLUE[i];
    }
    for (int i = 0; i < 256; i++) {
      redValues[i] = ImageHelper.clamp(
          ImageHelper.integerize(255 / (double) HistogramData.PIXEL_COUNT * cumulativeRed[i]), 0, 255);
      greenValues[i] = ImageHelper.clamp(
          ImageHelper.integerize(255 / (double) HistogramData.PIXEL_COUNT * cumulativeGreen[i]), 0, 255);
      blueValues[i] = ImageHelper.clamp(
          ImageHelper.integerize(255 / (double) HistogramData.PIXEL_COUNT * cumulativeBlue[i]), 0, 255);
    }
  }

  @Override
  public Integer apply(Integer rgb) {
    // Rozbior koloru na skladowe R, G, B
    int r = ImageHelper.jred(rgb);
    int g = ImageHelper.jgreen(rgb);
    int b = ImageHelper.jblue(rgb);

    //odswiezenie danych histogramu
    switch (channel) {

      case ALL:
        r = redValues[r];
        g = greenValues[g];
        b = blueValues[b];
        break;
      case RED:
        r = redValues[r];
        break;
      case GREEN:
        g = greenValues[g];
        break;
      case BLUE:
        b = blueValues[b];
        break;
      case GRAY:
        break;
    }
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
