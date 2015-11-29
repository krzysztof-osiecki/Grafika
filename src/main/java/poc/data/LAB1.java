package poc.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import poc.tools.ImageHelper;

@Getter
@AllArgsConstructor
public class LAB1 {
  private static BiMap<Integer, LAB1> processedColors = HashBiMap.create();
  private double l;
  private int a;
  private int b;

  public void setL(double l) {
    if (l < 0) l = 0;
    if (l > 100) l = 100;
    this.l = l;
  }

  public void setA(int a) {
    this.a = ImageHelper.clamp(a, -128, 127);
  }

  public void setB(int b) {
    this.b = ImageHelper.clamp(b, -128, 127);
  }

  public LAB1(int rgb) {
    LAB1 lab = processedColors.get(rgb);
    if (lab != null) {
      l = lab.getL();
      a = lab.getA();
      b = lab.getB();
    } else {
      XYZ xyz = new XYZ(rgb);
      double fx = f(xyz.getX() / ImageHelper.Xr);
      double fy = f(xyz.getY() / ImageHelper.Yr);
      double fz = f(xyz.getZ() / ImageHelper.Zr);
      this.setL(116 * fy - 16);
      a = ImageHelper.clamp(ImageHelper.integerize(500 * (fx - fy)), -128, 127);
      b = ImageHelper.clamp(ImageHelper.integerize(200 * (fy - fz)), -128, 127);
      processedColors.put(rgb, this);
    }
  }

  public int toRgb() {
    Integer integer = processedColors.inverse().get(this);
    if (integer != null) {
      return integer;
    } else {
      double yr;
      if (l > ImageHelper.KE) {
        yr = Math.pow((l + 16) / 116.0, 3);
      } else {
        yr = l / ImageHelper.K;
      }
      double fy;
      if (yr > ImageHelper.E) {
        fy = (l + 16) / 116.0;
      } else {
        fy = (ImageHelper.K * yr + 16) / 116.0;
      }
      double fx = a / 500.0 + fy;
      double fz = fy - b / 200.0;
      double xr;
      double fx3 = Math.pow(fx, 3);
      if (fx3 > ImageHelper.E) {
        xr = fx3;
      } else {
        xr = (116 * fx - 16) / ImageHelper.K;
      }
      double fz3 = Math.pow(fz, 3);
      double zr;
      if (fz3 > ImageHelper.E) {
        zr = fz3;
      } else {
        zr = (116 * fz - 16) / ImageHelper.K;
      }
      return new XYZ(xr * ImageHelper.Xr, yr * ImageHelper.Yr, zr * ImageHelper.Zr).toRgb();
    }
  }

  private double f(double x) {
    if (x > ImageHelper.E) {
      return Math.pow(x, 1.0 / 3);
    }
    return (ImageHelper.K * x + 16) / 116.0;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof LAB1)) return false;
    LAB1 toCompare = (LAB1) obj;
    return Math.abs(toCompare.l - l) < 0.1 && toCompare.a == a && toCompare.b == b;
  }
}

