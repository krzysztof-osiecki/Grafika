package poc.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import poc.tools.ImageHelper;

@Getter
@AllArgsConstructor
public class HSL {

  private double l;
  private double s;
  private double h;

  public HSL(int rgb) {
    double red = ImageHelper.jred(rgb) / 255.0;
    double green = ImageHelper.jgreen(rgb) / 255.0;
    double blue = ImageHelper.jblue(rgb) / 255.0;
    double max = Math.max(red, Math.max(green, blue));
    double min = Math.min(red, Math.min(green, blue));
    l = (max + min) / 2.0;
    s = calculateS(l, max, min);
    h = calculateH(red, green, blue, max, min);
  }

  public int toRgb() {
    double q = calculateQ();
    double p = 2 * l - q;
    double tempH = h / 360.0;
    double tR = tempH + 1 / 3.0;
    double tG = tempH;
    double tB = tempH - 1 / 3.0;
    tR = fitIntoRange(tR);
    tG = fitIntoRange(tG);
    tB = fitIntoRange(tB);
    int red = getColor(tR, q, p);
    int green = getColor(tG, q, p);
    int blue = getColor(tB, q, p);
    return ImageHelper.jrgb(red, green, blue);
  }

  public void setL(double l) {
    this.l = normalize(l);
  }

  public void setS(double s) {
    this.s = normalize(s);
  }

  public void setH(double h) {
    this.h = ImageHelper.handleAngle(h);
  }

  private double calculateQ() {
    double q;
    if (l >= 0.5) {
      q = l + s - (l * s);
    } else {
      q = l * (1 + s);
    }
    return q;
  }

  private int getColor(double t, double q, double p) {
    if (t < 1 / 6.0) {
      return ImageHelper.clamp(ImageHelper.integerize(
          (p + (q - p) * 6 * t) * 255
      ), 0, 255);
    } else if (t < 0.5) {
      return ImageHelper.clamp(ImageHelper.integerize(q * 255), 0, 255);
    } else if (t < 2 / 3.0) {
      return ImageHelper.clamp(ImageHelper.integerize(
          (p + (q - p) * 6 * (2 / 3.0 - t)) * 255
      ), 0, 255);
    }
    return ImageHelper.clamp(ImageHelper.integerize(p * 255), 0, 255);
  }

  private double fitIntoRange(double value) {
    if (value < 0) return value + 1;
    else if (value > 1) return value - 1;
    return value;
  }

  private double calculateH(double red, double green, double blue, double max, double min) {
    if (red == max) {
      if (green >= blue) {
        return ImageHelper.handleAngle(60 * (green - blue) / (max - min));
      } else {
        return ImageHelper.handleAngle(60 * (green - blue) / (max - min) + 360);
      }
    } else if (green == max) {
      return ImageHelper.handleAngle(60 * (blue - red) / (max - min) + 120);
    } else {
      return ImageHelper.handleAngle(60 * (red - green) / (max - min) + 240);
    }
  }

  private double calculateS(double l, double max, double min) {
    if (max == min || l == 0)
      return 0;
    if (l > 0.5)
      return (max - min) / (2 * l);
    return (max - min) / (2 - 2 * l);
  }

  private double normalize(double val){
    if(val < 0) return 0;
    if(val > 1) return 1;
    return val;
  }
}
