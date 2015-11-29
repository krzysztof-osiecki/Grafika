package poc.functions;

import poc.data.LAB1;
import poc.histogram.HistogramData;
import poc.tools.ImageHelper;

public class LabFunction extends BaseFunction {
  private LAB1 base;

  public LabFunction(LAB1 base) {
    this.base = base;
  }

  @Override
  public Integer apply(Integer rgb) {
    LAB1 lab = new LAB1(rgb);
    lab.setL(lab.getL() + base.getL());
    lab.setA(lab.getA() + base.getA());
    lab.setB(lab.getB() + base.getB());
    int newRgb = lab.toRgb();
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
