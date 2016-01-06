package poc.functions;

import lombok.Getter;
import org.jtransforms.fft.DoubleFFT_2D;
import poc.tools.Channel;
import poc.tools.ImageHelper;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


public class FftFunction {
  @Getter
  private final BufferedImage re;
  @Getter
  private final BufferedImage im;
  @Getter
  private final BufferedImage ph;
  @Getter
  private final BufferedImage ma;
  private final int[] reT;
  private final int[] imT;
  private final int[] phT;
  private final int[] maT;

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
    calculateForChannel(image, Channel.RED, doubleFFT_2D);
    calculateForChannel(image, Channel.GREEN, doubleFFT_2D);
    calculateForChannel(image, Channel.BLUE, doubleFFT_2D);
    swapQuarters(re);
    swapQuarters(im);
    swapQuarters(ph);
    swapQuarters(ma);
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

  private void calculateForChannel(BufferedImage image, Channel channel, DoubleFFT_2D doubleFFT_2D) {
    double[][] doubles = new double[image.getHeight()][2 * image.getWidth()];
    double[][] doublesLog = new double[image.getHeight()][2 * image.getWidth()];
    DataBufferInt sbuff = (DataBufferInt) image.getRaster().getDataBuffer();
    int[] sp = sbuff.getData();
    int i = 0;
    for (int y = 0; y < image.getHeight(); ++y) {
      for (int x = 0; x < image.getWidth(); ++x) {
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
    double maxRe = Double.MIN_VALUE;
    double maxIm = Double.MIN_VALUE;
    for (int y = 0; y < image.getHeight(); ++y) {
      for (int x = 0; x < image.getWidth(); ++x) {
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
    for (int y = 0; y < image.getHeight(); ++y) {
      for (int x = 0; x < image.getWidth(); ++x) {
        double reD = doubles[y][2 * x];
        double imD = doubles[y][2 * x + 1];
        switch (channel) {
          case RED:
            reT[i] = ImageHelper.jrgb(ImageHelper.clamp(ImageHelper.integerize(doublesLog[y][2 * x]/maxRe * 255), 0, 255), ImageHelper.jgreen(reT[i]), ImageHelper.jblue(reT[i]));
            imT[i] = ImageHelper.jrgb(ImageHelper.clamp(ImageHelper.integerize( doublesLog[y][2 * x + 1]/maxIm * 255), 0, 255), ImageHelper.jgreen(imT[i]), ImageHelper.jblue(imT[i]));
            maT[i] = ImageHelper.jrgb(ImageHelper.clamp(ImageHelper.integerize(Math.sqrt(reD * reD + imD * imD)), 0, 255), ImageHelper.jgreen(maT[i]), ImageHelper.jblue(maT[i]));
            phT[i] = ImageHelper.jrgb(ImageHelper.clamp(ImageHelper.integerize(Math.atan(imD / reD)*255), 0, 255), ImageHelper.jgreen(phT[i]), ImageHelper.jblue(phT[i]));
            break;
          case GREEN:
            reT[i] = ImageHelper.jrgb(ImageHelper.jred(reT[i]), ImageHelper.clamp(ImageHelper.integerize(doublesLog[y][2 * x]/maxRe * 255), 0, 255), ImageHelper.jblue(reT[i]));
            imT[i] = ImageHelper.jrgb(ImageHelper.jred(imT[i]), ImageHelper.clamp(ImageHelper.integerize(doublesLog[y][2 * x + 1]/maxIm * 255), 0, 255), ImageHelper.jblue(imT[i]));
            maT[i] = ImageHelper.jrgb(ImageHelper.jred(maT[i]), ImageHelper.clamp(ImageHelper.integerize(Math.sqrt(reD * reD + imD * imD)), 0, 255), ImageHelper.jblue(maT[i]));
            phT[i] = ImageHelper.jrgb(ImageHelper.jred(phT[i]), ImageHelper.clamp(ImageHelper.integerize(Math.atan(imD / reD)*255), 0, 255), ImageHelper.jblue(phT[i]));
            break;
          case BLUE:
            reT[i] = ImageHelper.jrgb(ImageHelper.jred(reT[i]), ImageHelper.jgreen(reT[i]), ImageHelper.clamp(ImageHelper.integerize(doublesLog[y][2 * x]/maxRe * 255), 0, 255));
            imT[i] = ImageHelper.jrgb(ImageHelper.jred(imT[i]), ImageHelper.jgreen(imT[i]), ImageHelper.clamp(ImageHelper.integerize(doublesLog[y][2 * x + 1]/maxIm * 255), 0, 255));
            maT[i] = ImageHelper.jrgb(ImageHelper.jred(maT[i]), ImageHelper.jgreen(maT[i]), ImageHelper.clamp(ImageHelper.integerize(Math.sqrt(reD * reD + imD * imD)), 0, 255));
            phT[i] = ImageHelper.jrgb(ImageHelper.jred(phT[i]), ImageHelper.jgreen(phT[i]), ImageHelper.clamp(ImageHelper.integerize(Math.atan(imD / reD)*255), 0, 255));
            break;
          case GRAY:
          case ALL:
            throw new IllegalStateException("Unsupported for this operation");
        }
        i++;
      }
    }
  }
}

