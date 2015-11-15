package poc.functions;

import poc.histogram.HistogramData;
import poc.tools.ImageHelper;

public class FilterFunction extends PlaneFunction {
  private final Integer[][] filter;
  private final int size;
  private int filterSum;

  public FilterFunction(Integer[][] filter, int size) {
    this.filter = filter;
    this.size = size;
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        this.filterSum += filter[i][j];
      }
    }
    if (filterSum == 0) filterSum = 1;
  }

  @Override
  public Integer apply(Integer[][] integers) {
    int sumR = 0;
    int sumG = 0;
    int sumB = 0;
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        int red = ImageHelper.jred(integers[i][j]);
        int green = ImageHelper.jgreen(integers[i][j]);
        int blue = ImageHelper.jblue(integers[i][j]);

        sumR += filter[i][j] * red;
        sumG += filter[i][j] * green;
        sumB += filter[i][j] * blue;
      }
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

  @Override
  public int tableSize() {
    return size;
  }
}
