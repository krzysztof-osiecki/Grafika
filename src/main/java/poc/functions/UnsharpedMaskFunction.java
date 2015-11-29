package poc.functions;


import lombok.Getter;
import poc.histogram.HistogramData;
import poc.tools.ImageHelper;

import java.util.function.BiFunction;

@Getter
public class UnsharpedMaskFunction implements BiFunction<Integer, Integer, Integer> {

  private final OptimizedGaussian gaussian;
  private final double alpha;

  @Override
  public Integer apply(Integer gRgb, Integer sRgb) {
    // Rozbior koloru na skladowe R, G, B
    int r = ImageHelper.jred(gRgb);
    int g = ImageHelper.jgreen(gRgb);
    int b = ImageHelper.jblue(gRgb);

    int sR = ImageHelper.jred(sRgb);
    int sG = ImageHelper.jgreen(sRgb);
    int sB = ImageHelper.jblue(sRgb);

    r = ImageHelper.clamp(ImageHelper.integerize(sR + alpha * (sR - r)), 0, 255);
    g = ImageHelper.clamp(ImageHelper.integerize(sG + alpha * (sG - g)), 0, 255);
    b = ImageHelper.clamp(ImageHelper.integerize(sB + alpha * (sB - b)), 0, 255);

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

  public UnsharpedMaskFunction(OptimizedGaussian gaussian, double alpha) {

    this.gaussian = gaussian;
    this.alpha = alpha;
  }
}
