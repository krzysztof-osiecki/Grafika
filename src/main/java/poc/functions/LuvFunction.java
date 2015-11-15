package poc.functions;

import poc.data.LUV;
import poc.histogram.HistogramData;
import poc.tools.ImageHelper;

public class LuvFunction extends BaseFunction {
  private LUV base;

  public LuvFunction(LUV base) {
    this.base = base;
  }

  @Override
  public Integer apply(Integer rgb) {
    LUV luv = new LUV(rgb);
    luv.setL(luv.getL() + base.getL());
    luv.setU(luv.getU() + base.getU());
    luv.setV(luv.getV() + base.getV());
    int newRgb = luv.toRgb();
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
