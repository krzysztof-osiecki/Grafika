package poc.functions;

import poc.data.CMYK;
import poc.histogram.HistogramData;
import poc.tools.ImageHelper;

public class CmykFunction extends BaseFunction {
  private CMYK base;

  public CmykFunction(CMYK base) {
    this.base = base;
  }

  @Override
  public Integer apply(Integer rgb) {
    CMYK cmyk = new CMYK(rgb);
    cmyk.setCyan(cmyk.getCyan() + base.getCyan());
    cmyk.setMagenta(cmyk.getMagenta() + base.getMagenta());
    cmyk.setYellow(cmyk.getYellow() + base.getYellow());
    cmyk.setBlack(cmyk.getBlack() + base.getBlack());
    int newRgb = cmyk.toRgb();
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
