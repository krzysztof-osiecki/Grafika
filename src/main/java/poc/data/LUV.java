package poc.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import poc.tools.ImageHelper;

@Getter
@AllArgsConstructor
public class LUV {
  private static BiMap<Integer, LUV> processedColors = HashBiMap.create();
  private double l;
  private int u;
  private int v;

  public void setL(double l) {
    if (l < 0) l = 0;
    if (l > 100) l = 100;
    this.l = l;
  }

  public void setU(int u) {
    this.u = ImageHelper.clamp(u, -134, 220);
  }

  public void setV(int v) {
    this.v = ImageHelper.clamp(v, -140, 122);
  }

  public LUV(int rgb) {
    LUV lab = processedColors.get(rgb);
    if (lab != null) {
      this.l = lab.getL();
      this.u = lab.getU();
      this.v = lab.getV();
    } else {
      XYZ xyz = new XYZ(rgb);
      double denominator = xyz.getX() + 15 * xyz.getY() + 3 * xyz.getZ();
      double uPrim = (4 * xyz.getX()) / denominator;
      double vPrim = (9 * xyz.getY()) / denominator;
      double relY = xyz.getY() / ImageHelper.Yr;
      if (relY > ImageHelper.E) {
        this.setL(116 * Math.pow(relY, 1.0 / 3.0) - 16);
      } else {
        this.setL(ImageHelper.K * relY);
      }
      this.setU(ImageHelper.integerize(13 * l * (uPrim - ImageHelper.UPrimN)));
      this.setV(ImageHelper.integerize(13 * l * (vPrim - ImageHelper.VPrimN)));
      processedColors.put(rgb, this);
    }
  }

  public int toRgb() {
    Integer integer = processedColors.inverse().get(this);
    if (integer != null) {
      return integer;
    } else {
      double uPrim = u / (13 * l) + ImageHelper.UPrimN;
      double vPrim = v / (13 * l) + ImageHelper.VPrimN;
      double y;
      if (l > ImageHelper.KE) {
        y = ImageHelper.Yr * Math.pow((l + 16) / 116.0, 3);
      } else {
        y = l * ImageHelper.YByK;
      }
      double x = (9 / 4.0) * y * (uPrim / vPrim);
      double z = (y * (12 - 3 * uPrim - 20 * vPrim)) / (4 * vPrim);
      return new XYZ(x, y, z).toRgb();
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof LUV)) return false;
    LUV toCompare = (LUV) obj;
    return Math.abs(toCompare.l - l) < 0.1 && toCompare.u == u && toCompare.v == v;
  }
}
