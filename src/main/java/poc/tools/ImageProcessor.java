package poc.tools;

import poc.histogram.HistogramData;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.function.Function;

public class ImageProcessor {

  public static void processImage(BufferedImage src, BufferedImage dst, Function<Integer, Integer> transformation) {
    // Pobranie referencji na bufor z pikselami obrazu zrodlowego i docelowego
    // Dzieki temu uzyskuje sie bezposredni dostep do ich wartosci
    int[] sp = getBitMap(src);
    int[] dp = getBitMap(dst);

    // Iteracja po wszystkich pikselach obrazu i przeliczenie kazdego z nich za pomoca funkcji changeBrightness()
    HistogramData.clear();
    int i = 0;
    for (int y = 0; y < src.getHeight(); ++y) {
      for (int x = 0; x < src.getWidth(); ++x) {
        dp[i] = transformation.apply(sp[i]);
        i++;
      }
    }
  }

  private static int[] getBitMap(BufferedImage src) {
    DataBufferInt sbuff = (DataBufferInt) src.getRaster().getDataBuffer();
    return sbuff.getData();
  }

}
