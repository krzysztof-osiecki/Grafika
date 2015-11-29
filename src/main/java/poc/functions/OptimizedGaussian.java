package poc.functions;


import poc.histogram.HistogramData;
import poc.tools.ImageHelper;

import java.util.function.Function;

public class OptimizedGaussian implements Function<Integer[], Integer> {
  private int filterSum;
  private Integer[] filter;

  public OptimizedGaussian(Integer[] filter) {
    this.filter = filter;
    for (Integer value : filter) {
      this.filterSum += value;
    }
    if (filterSum == 0) filterSum = 1;
  }

  @Override
  public Integer apply(Integer[] integers) {
    int sumR = 0;
    int sumG = 0;
    int sumB = 0;
    for (int i = 0; i < integers.length; i++) {
      int red = ImageHelper.jred(integers[i]);
      int green = ImageHelper.jgreen(integers[i]);
      int blue = ImageHelper.jblue(integers[i]);
      sumR += filter[i] * red;
      sumG += filter[i] * green;
      sumB += filter[i] * blue;
    }
    int newR = ImageHelper.clamp(ImageHelper.integerize(sumR / (double) filterSum), 0, 255);
    int newG = ImageHelper.clamp(ImageHelper.integerize(sumG / (double) filterSum), 0, 255);
    int newB = ImageHelper.clamp(ImageHelper.integerize(sumB / (double) filterSum), 0, 255);
    HistogramData.RED[newR]++;
    HistogramData.GREEN[newG]++;
    HistogramData.BLUE[newB]++;
    int gray = ImageHelper.clamp(
        ImageHelper.integerize(0.299 * newR + 0.587 * newG + 0.114 * newB), 0, 255);
    HistogramData.GRAY_SCALE[gray]++;
    return ImageHelper.jrgb(newR, newG, newB);
  }

  public int tableSize() {
    return filter.length;
  }
}
