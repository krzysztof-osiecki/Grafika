package poc.tools;

import poc.functions.OptimizedGaussian;
import poc.functions.PlaneFunction;
import poc.functions.UnsharpedMaskFunction;
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
    HistogramData.clear();
    int i = 0;
    for (int y = 0; y < src.getHeight(); ++y) {
      for (int x = 0; x < src.getWidth(); ++x) {
        dp[i] = transformation.apply(sp[i]);
        i++;
      }
    }
  }

  public static void processImage(BufferedImage src, BufferedImage dst, PlaneFunction transformation) {
    // Pobranie referencji na bufor z pikselami obrazu zrodlowego i docelowego
    // Dzieki temu uzyskuje sie bezposredni dostep do ich wartosci
    int[] sp = getBitMap(src);
    int[] dp = getBitMap(dst);
    HistogramData.clear();

    int i = 0;
    int halfSize = -(transformation.tableSize() - 1) / 2;
    for (int y = 0; y < src.getHeight(); ++y) {
      for (int x = 0; x < src.getWidth(); ++x) {
        Integer[][] ints = new Integer[transformation.tableSize()][transformation.tableSize()];

        for (int a = 0; a < transformation.tableSize(); a++) {
          for (int b = 0; b < transformation.tableSize(); b++) {
            int currX = x + halfSize + a;
            if (currX < 0) currX = 0;
            if (currX >= src.getWidth()) currX = src.getWidth() - 1;
            int currY = y + halfSize + b;
            if (currY < 0) currY = 0;
            if (currY >= src.getHeight()) currY = src.getHeight() - 1;
            int newI = src.getWidth() * currY + currX;
            ints[a][b] = sp[newI];
          }
        }

        dp[i] = transformation.apply(ints);
        i++;
      }
    }
  }

  public static void processImage(BufferedImage src, BufferedImage dst, UnsharpedMaskFunction transformation) {
    ImageProcessor.processImage(src, dst, transformation.getGaussian());
    int[] sp = getBitMap(src);
    int[] dp = getBitMap(dst);
    int i = 0;
    for (int y = 0; y < src.getHeight(); ++y) {
      for (int x = 0; x < src.getWidth(); ++x) {
        dp[i] = transformation.apply(dp[i], sp[i]);
        i++;
      }
    }
  }

  public static void processImage(BufferedImage src, BufferedImage dst, OptimizedGaussian transformation) {
    // Pobranie referencji na bufor z pikselami obrazu zrodlowego i docelowego
    // Dzieki temu uzyskuje sie bezposredni dostep do ich wartosci
    int[] sp = getBitMap(src);
    int[] dp = getBitMap(dst);
    HistogramData.clear();
    int i = 0;
    for (int y = 0; y < src.getHeight(); ++y) {
      for (int x = 0; x < src.getWidth(); ++x) {
        int halfSize = -(transformation.tableSize() - 1) / 2;
        Integer[] ints = new Integer[transformation.tableSize()];

        for (int a = 0; a < transformation.tableSize(); a++) {
          int currX = x + halfSize + a;
          if (currX < 0) currX = 0;
          if (currX >= src.getWidth()) currX = src.getWidth() - 1;
          int newI = src.getWidth() * y + currX;
          ints[a] = sp[newI];
        }

        dp[i] = transformation.apply(ints);
        i++;
      }
    }

    i = 0;
    for (int y = 0; y < src.getHeight(); ++y) {
      for (int x = 0; x < src.getWidth(); ++x) {
        int halfSize = -(transformation.tableSize() - 1) / 2;
        Integer[] ints = new Integer[transformation.tableSize()];

        for (int a = 0; a < transformation.tableSize(); a++) {
          int currY = y + halfSize + a;
          if (currY < 0) currY = 0;
          if (currY >= src.getHeight()) currY = src.getHeight() - 1;
          int newI = src.getWidth() * currY + x;
          ints[a] = dp[newI];
        }

        dp[i] = transformation.apply(ints);
        i++;
      }
    }
  }

  private static int[] getBitMap(BufferedImage src) {
    DataBufferInt sbuff = (DataBufferInt) src.getRaster().getDataBuffer();
    return sbuff.getData();
  }

}
