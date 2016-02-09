package poc.forms;

import poc.data.Tuple3Ints;
import poc.functions.PlaneFunction;
import poc.histogram.HistogramData;
import poc.tools.ImageHelper;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MedianFilterFunction extends PlaneFunction {
  private final Integer[][] filter;
  private final int size;

  public MedianFilterFunction(Integer[][] filter, int size) {
    this.filter = filter;
    this.size = size;
  }

  @Override
  public Integer apply(Integer[][] integers) {
    List<Tuple3Ints> values = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (filter[i][j] > 0) {
          int red = ImageHelper.jred(integers[i][j]);
          int green = ImageHelper.jgreen(integers[i][j]);
          int blue = ImageHelper.jblue(integers[i][j]);
          int gray = ImageHelper.clamp(
              ImageHelper.integerize(0.299 * red + 0.587 * green + 0.114 * blue), 0, 255);
          values.add(Tuple3Ints.of(gray, i, j));
        }
      }
    }
    List<Tuple3Ints> sort = values.stream().sorted((o1, o2) -> o1._1 - o2._1).collect(toList());
    Tuple3Ints median = sort.get(sort.size() / 2);

    int newR = ImageHelper.jred(integers[median._2][median._3]);
    int newG = ImageHelper.jgreen(integers[median._2][median._3]);
    int newB = ImageHelper.jblue(integers[median._2][median._3]);
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
