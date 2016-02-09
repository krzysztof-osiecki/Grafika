package poc.functions;

import lombok.Getter;

import java.awt.image.BufferedImage;

public class FftConvolution {
  private final BufferedImage image;
  private final Integer[][] filter;
  private final FftFunction filterFftFunction;
  @Getter
  private final BufferedImage filtered;
  private double[][] normalizedFilter;
  private final int filterSum;

  public FftConvolution(BufferedImage image, Integer[][] filter) {
    this.image = image;
    this.filter = filter;
    int sum = 0;
    for (Integer[] aFilter : filter) {
      for (int anAFilter : aFilter) {
        sum += anAFilter;
      }
    }
    if (sum == 0) sum = 1;
    this.filterSum = sum;
    this.extendFilter();
    this.filterFftFunction = new FftFunction(image, normalizedFilter);
    filtered = filterFftFunction.getFiltered();
  }

  private void extendFilter() {
    normalizedFilter = new double[image.getHeight()][image.getWidth()];
    int widthMiddle = image.getWidth() / 2;
    int heightMiddle = image.getHeight() / 2;
    int filterMiddle = filter.length / 2;
    int a = 0;
    int b = 0;
    for (int j = widthMiddle - filterMiddle; j <= widthMiddle + filterMiddle; j++) {
      for (int i = heightMiddle - filterMiddle; i <= heightMiddle + filterMiddle; i++) {
        normalizedFilter[i][j] = filter[a][b] / (double) filterSum;
        b++;
      }
      b = 0;
      a++;
    }
  }


}
