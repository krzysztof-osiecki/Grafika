package poc.forms;

import poc.functions.PlaneFunction;
import poc.histogram.HistogramData;
import poc.tools.ImageHelper;

public class MinFilterFunction extends PlaneFunction {
  private final Integer[][] filter;
  private final int size;

  public MinFilterFunction(Integer[][] filter, int size) {
    this.filter = filter;
    this.size = size;
  }

  @Override
  public Integer apply(Integer[][] integers) {
    int minI = 0;
    int minJ = 0;
    int minGray = 255;
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (filter[i][j] > 0) {
          int red = ImageHelper.jred(integers[i][j]);
          int green = ImageHelper.jgreen(integers[i][j]);
          int blue = ImageHelper.jblue(integers[i][j]);
          int gray = ImageHelper.clamp(
              ImageHelper.integerize(0.299 * red + 0.587 * green + 0.114 * blue), 0, 255);
          if (minGray > gray) {
            minGray = gray;
            minI = i;
            minJ = j;
          }
        }
      }
    }
    int newR = ImageHelper.jred(integers[minI][minJ]);
    int newG = ImageHelper.jgreen(integers[minI][minJ]);
    int newB = ImageHelper.jblue(integers[minI][minJ]);
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
