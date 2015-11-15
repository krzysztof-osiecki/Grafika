package poc.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import poc.tools.ImageHelper;

@Getter
@AllArgsConstructor
public class XYZ {
  private static BiMap<Integer, XYZ> processedColors = HashBiMap.create();
  private double x;
  private double y;
  private double z;

  public XYZ(int rgb) {
    XYZ xyz = processedColors.get(rgb);
    if (xyz != null) {
      this.x = xyz.getX();
      this.y = xyz.getY();
      this.z = xyz.getZ();
    } else {
      double normR = ImageHelper.jred(rgb) / 255.0;
      double normG = ImageHelper.jgreen(rgb) / 255.0;
      double normB = ImageHelper.jblue(rgb) / 255.0;
      x = normR * 0.41242 + normR * 0.35759 + normR * 0.18046;
      y = normG * 0.21266 + normG * 0.71517 + normG * 0.07218;
      z = normB * 0.01933 + normB * 0.11919 + normB * 0.95044;
      processedColors.put(rgb, this);
    }
  }

  public int toRgb() {
    Integer integer = processedColors.inverse().get(this);
    if (integer != null) {
      return integer;
    } else {
      double normR = x * 3.24071 + x * -1.53726 + x * -0.49857;
      double normG = y * -0.96925 + y * 1.87599 + y * 0.04155;
      double normB = z * 0.05564 + z * -0.20399 + z * 1.05707;
      int r = ImageHelper.clamp(ImageHelper.integerize(normR * 255), 0, 255);
      int g = ImageHelper.clamp(ImageHelper.integerize(normG * 255), 0, 255);
      int b = ImageHelper.clamp(ImageHelper.integerize(normB * 255), 0, 255);
      int jrgb = ImageHelper.jrgb(r, g, b);
      return jrgb;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof XYZ)) return false;
    XYZ toCompare = (XYZ) obj;
    return Math.abs(toCompare.x - x) < 0.1 && Math.abs(toCompare.y - y) < 0.1 && Math.abs(toCompare.z - z) < 0.1;
  }
}