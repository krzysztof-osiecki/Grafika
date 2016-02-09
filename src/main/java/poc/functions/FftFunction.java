package poc.functions;

import lombok.Getter;
import org.jtransforms.fft.DoubleFFT_2D;
import poc.tools.Channel;
import poc.tools.ImageHelper;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


public class FftFunction {
  private int[] filteredT;
  @Getter
  private BufferedImage re;
  @Getter
  private BufferedImage im;
  @Getter
  private BufferedImage ph;
  @Getter
  private BufferedImage ma;
  @Getter
  private BufferedImage filtered;
  @Getter
  private int[] reT;
  @Getter
  private int[] imT;
  private int[] phT;
  private int[] maT;

  @Getter
  private double[][] calculatedValues;
  private double[][] currentChannel;

  public FftFunction(BufferedImage image) {
    re = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);
    im = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
    ph = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
    ma = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

    DataBufferInt rebuff = (DataBufferInt) re.getRaster().getDataBuffer();
    DataBufferInt imbuff = (DataBufferInt) im.getRaster().getDataBuffer();
    DataBufferInt phbuff = (DataBufferInt) ph.getRaster().getDataBuffer();
    DataBufferInt mabuff = (DataBufferInt) ma.getRaster().getDataBuffer();
    reT = rebuff.getData();
    imT = imbuff.getData();
    phT = phbuff.getData();
    maT = mabuff.getData();

    DoubleFFT_2D doubleFFT_2D = new DoubleFFT_2D(image.getHeight(), image.getWidth());
    DataBufferInt sbuff = (DataBufferInt) image.getRaster().getDataBuffer();
    int[] sp = sbuff.getData();
    calculate(sp, image.getHeight(), image.getWidth(), Channel.RED, doubleFFT_2D);
    calculate(sp, image.getHeight(), image.getWidth(), Channel.GREEN, doubleFFT_2D);
    calculate(sp, image.getHeight(), image.getWidth(), Channel.BLUE, doubleFFT_2D);
    swapQuarters(re);
    swapQuarters(im);
    swapQuarters(ph);
    swapQuarters(ma);
  }

  public FftFunction(BufferedImage image, double[][] filter) {
    filtered = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);
    re = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);
    im = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
    ph = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
    ma = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

    DataBufferInt filteredBuff = (DataBufferInt) filtered.getRaster().getDataBuffer();
    DataBufferInt rebuff = (DataBufferInt) re.getRaster().getDataBuffer();
    DataBufferInt imbuff = (DataBufferInt) im.getRaster().getDataBuffer();
    DataBufferInt phbuff = (DataBufferInt) ph.getRaster().getDataBuffer();
    DataBufferInt mabuff = (DataBufferInt) ma.getRaster().getDataBuffer();

    filteredT = filteredBuff.getData();
    reT = rebuff.getData();
    imT = imbuff.getData();
    phT = phbuff.getData();
    maT = mabuff.getData();

    double[][] redValues = new double[image.getHeight()][2 * image.getWidth()];
    double[][] greenValues = new double[image.getHeight()][2 * image.getWidth()];
    double[][] blueValues = new double[image.getHeight()][2 * image.getWidth()];

    double[][] realRedValues = new double[image.getHeight()][image.getWidth()];
    double[][] realGreenValues = new double[image.getHeight()][image.getWidth()];
    double[][] realBlueValues = new double[image.getHeight()][image.getWidth()];

    int[][] realIntRedValues = new int[image.getHeight()][image.getWidth()];
    int[][] realIntGreenValues = new int[image.getHeight()][image.getWidth()];
    int[][] realIntBlueValues = new int[image.getHeight()][image.getWidth()];

    int[][] combinedRealIntValues = new int[image.getHeight()][image.getWidth()];

    DoubleFFT_2D doubleFFT_2D = new DoubleFFT_2D(image.getHeight(), image.getWidth());
    DataBufferInt sbuff = (DataBufferInt) image.getRaster().getDataBuffer();
    int[] sp = sbuff.getData();

    calculate(filter, filter.length, filter[0].length, doubleFFT_2D);

    calculate(sp, image.getHeight(), image.getWidth(), Channel.RED, doubleFFT_2D);
    calculateValues(redValues);
    doubleFFT_2D.complexInverse(redValues, true);
    writeRealOnly(redValues, realRedValues);
    scale(realRedValues, realIntRedValues);

    calculate(sp, image.getHeight(), image.getWidth(), Channel.GREEN, doubleFFT_2D);
    calculateValues(greenValues);
    doubleFFT_2D.complexInverse(greenValues, true);
    writeRealOnly(greenValues, realGreenValues);
    scale(realGreenValues, realIntGreenValues);

    calculate(sp, image.getHeight(), image.getWidth(), Channel.BLUE, doubleFFT_2D);
    calculateValues(blueValues);
    doubleFFT_2D.complexInverse(blueValues, true);
    writeRealOnly(blueValues, realBlueValues);
    scale(realBlueValues, realIntBlueValues);

    combine(realIntRedValues, realIntGreenValues, realIntBlueValues, combinedRealIntValues);
    write(combinedRealIntValues, filteredT);
    swapQuarters(filtered);
  }

  private void write(int[][] combinedRealIntValues, int[] filteredT) {
    int i = 0 ;
    for (int x = 0; x < combinedRealIntValues.length; x++) {
      for (int y = 0; y< combinedRealIntValues[x].length; y++) {
        filteredT[i] = combinedRealIntValues[x][y];
        i++;
      }
    }
  }

  private void combine(int[][] realIntRedValues, int[][] realIntGreenValues, int[][] realIntBlueValues, int[][] combinedRealIntValues) {
    for (int x = 0; x < combinedRealIntValues.length; x++) {
      for (int y = 0; y < combinedRealIntValues[x].length; y++) {
        combinedRealIntValues[x][y] = ImageHelper.jrgb(realIntRedValues[x][y], realIntGreenValues[x][y], realIntBlueValues[x][y]);
      }
    }
  }

  private void scale(double[][] realValues, int[][] realIntValues) {
    for (int x = 0; x < realIntValues.length; x++) {
      for (int y = 0; y < realIntValues[x].length; y++) {
          realIntValues[x][y] = ImageHelper.clamp(ImageHelper.integerize((realValues[x][y] * 255)), 0 ,255);
      }
    }
  }

  private void writeRealOnly(double[][] redValues, double[][] realRedValues) {
    for (int x = 0; x < realRedValues.length; x++) {
      for (int y = 0; y < realRedValues[x].length; y++) {
        realRedValues[x][y] = redValues[x][2 * y];
      }
    }
  }

  private void calculateValues(double[][] values) {
    for (int x = 0; x < calculatedValues.length; x++) {
      for (int y = 0; y < calculatedValues[x].length; y += 2) {
        values[x][y] = calculatedValues[x][y] * currentChannel[x][y] - calculatedValues[x][y + 1] * currentChannel[x][y + 1];
        values[x][y + 1] = calculatedValues[x][y + 1] * currentChannel[x][y] + calculatedValues[x][y] * currentChannel[x][y + 1];
      }
    }
  }

  private void swapQuarters(BufferedImage image) {
    int i = 0;
    DataBufferInt buffer = (DataBufferInt) image.getRaster().getDataBuffer();
    int[] values = buffer.getData();
    for (int y = 0; y < image.getHeight() / 2; ++y) {
      for (int x = 0; x < image.getWidth(); ++x) {
        int newX = (x + image.getWidth() / 2) % image.getWidth();
        int newY = y + image.getHeight() / 2;
        int newI = image.getWidth() * newY + newX;
        int swp = values[i];
        values[i] = values[newI];
        values[newI] = swp;
        i++;
      }
    }
  }

  private void calculate(int[] sp, int height, int width, Channel channel, DoubleFFT_2D doubleFFT_2D) {
    double[][] doubles = new double[height][2 * width];
    int i = 0;
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        switch (channel) {
          case RED:
            doubles[y][2 * x] = ImageHelper.jred(sp[i]) / 255.0;
            break;
          case GREEN:
            doubles[y][2 * x] = ImageHelper.jgreen(sp[i]) / 255.0;
            break;
          case BLUE:
            doubles[y][2 * x] = ImageHelper.jblue(sp[i]) / 255.0;
            break;
          case GRAY:
          case ALL:
            throw new IllegalStateException("Unsupported for this operation");
        }

        i++;
      }
    }
    doubleFFT_2D.complexForward(doubles);
    this.currentChannel = doubles;
    double maxRe = Double.MIN_VALUE;
    double maxIm = Double.MIN_VALUE;

    double[][] doublesLog = new double[height][2 * width];
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        doublesLog[y][2 * x] = Math.log(Math.abs(doubles[y][2 * x]) + 1);
        doublesLog[y][2 * x + 1] = Math.log(Math.abs(doubles[y][2 * x + 1]) + 1);
        if (doublesLog[y][2 * x] > maxRe)
          maxRe = doublesLog[y][2 * x];
        if (doublesLog[y][2 * x + 1] > maxIm)
          maxIm = doublesLog[y][2 * x + 1];
        i++;
      }
    }
    i = 0;
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        double reD = doubles[y][2 * x];
        double imD = doubles[y][2 * x + 1];
        switch (channel) {
          case RED:
            reT[i] = ImageHelper.jrgb(ImageHelper.clamp(ImageHelper.integerize(doublesLog[y][2 * x] / maxRe * 255), 0, 255), ImageHelper.jgreen(reT[i]), ImageHelper.jblue(reT[i]));
            imT[i] = ImageHelper.jrgb(ImageHelper.clamp(ImageHelper.integerize(doublesLog[y][2 * x + 1] / maxIm * 255), 0, 255), ImageHelper.jgreen(imT[i]), ImageHelper.jblue(imT[i]));
            maT[i] = ImageHelper.jrgb(ImageHelper.clamp(ImageHelper.integerize(Math.sqrt(reD * reD + imD * imD)), 0, 255), ImageHelper.jgreen(maT[i]), ImageHelper.jblue(maT[i]));
            phT[i] = ImageHelper.jrgb(ImageHelper.clamp(ImageHelper.integerize(Math.atan(imD / reD) * 255), 0, 255), ImageHelper.jgreen(phT[i]), ImageHelper.jblue(phT[i]));
            break;
          case GREEN:
            reT[i] = ImageHelper.jrgb(ImageHelper.jred(reT[i]), ImageHelper.clamp(ImageHelper.integerize(doublesLog[y][2 * x] / maxRe * 255), 0, 255), ImageHelper.jblue(reT[i]));
            imT[i] = ImageHelper.jrgb(ImageHelper.jred(imT[i]), ImageHelper.clamp(ImageHelper.integerize(doublesLog[y][2 * x + 1] / maxIm * 255), 0, 255), ImageHelper.jblue(imT[i]));
            maT[i] = ImageHelper.jrgb(ImageHelper.jred(maT[i]), ImageHelper.clamp(ImageHelper.integerize(Math.sqrt(reD * reD + imD * imD)), 0, 255), ImageHelper.jblue(maT[i]));
            phT[i] = ImageHelper.jrgb(ImageHelper.jred(phT[i]), ImageHelper.clamp(ImageHelper.integerize(Math.atan(imD / reD) * 255), 0, 255), ImageHelper.jblue(phT[i]));
            break;
          case BLUE:
            reT[i] = ImageHelper.jrgb(ImageHelper.jred(reT[i]), ImageHelper.jgreen(reT[i]), ImageHelper.clamp(ImageHelper.integerize(doublesLog[y][2 * x] / maxRe * 255), 0, 255));
            imT[i] = ImageHelper.jrgb(ImageHelper.jred(imT[i]), ImageHelper.jgreen(imT[i]), ImageHelper.clamp(ImageHelper.integerize(doublesLog[y][2 * x + 1] / maxIm * 255), 0, 255));
            maT[i] = ImageHelper.jrgb(ImageHelper.jred(maT[i]), ImageHelper.jgreen(maT[i]), ImageHelper.clamp(ImageHelper.integerize(Math.sqrt(reD * reD + imD * imD)), 0, 255));
            phT[i] = ImageHelper.jrgb(ImageHelper.jred(phT[i]), ImageHelper.jgreen(phT[i]), ImageHelper.clamp(ImageHelper.integerize(Math.atan(imD / reD) * 255), 0, 255));
            break;
          case GRAY:
          case ALL:
            throw new IllegalStateException("Unsupported for this operation");
        }
        i++;
      }
    }
  }

  private void calculate(double[][] oDoubles, int height, int width, DoubleFFT_2D doubleFFT_2D) {
    double[][] doubles = new double[height][2 * width];
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        doubles[y][2 * x] = oDoubles[y][x];
      }
    }
    doubleFFT_2D.complexForward(doubles);
    this.calculatedValues = doubles;
  }
}


