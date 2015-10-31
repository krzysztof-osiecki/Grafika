package poc.functions;

import poc.data.HSL;
import poc.histogram.HistogramData;
import poc.tools.ImageHelper;

public class HslFunction extends BaseFunction {
  private HSL base;

  public HslFunction(HSL base) {
    this.base = base;
  }

  @Override
  public Integer apply(Integer rgb) {
    HSL hsl = new HSL(rgb);
    hsl.setH(hsl.getH() + base.getH());
    hsl.setL(hsl.getL() + base.getL());
    hsl.setS(hsl.getS() + base.getS());
    int newRgb = hsl.toRgb();
    // Rozbior koloru na skladowe R, G, B
    int r = ImageHelper.jred(newRgb);
    int g = ImageHelper.jgreen(newRgb);
    int b = ImageHelper.jblue(newRgb);

    //odswiezenie danych histogramu
    HistogramData.RED[r]++;
    HistogramData.GREEN[g]++;
    HistogramData.BLUE[b]++;
    int gray = ImageHelper.clamp(
        ImageHelper.integerize(0.299 * r + 0.587 * g + 0.114 * b), 0, 255);
    HistogramData.GRAY_SCALE[gray]++;

    return newRgb;
  }
}
