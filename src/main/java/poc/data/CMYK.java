package poc.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import poc.tools.ImageHelper;

@Getter
@Setter
@AllArgsConstructor
public class CMYK {
  private double cyan;
  private double magenta;
  private double yellow;
  private double black;

  public CMYK(int rgb) {
    int red = ImageHelper.jred(rgb);
    int green = ImageHelper.jgreen(rgb);
    int blue = ImageHelper.jblue(rgb);
    double c = 1 - red / 255.0;
    double m = 1 - green / 255.0;
    double y = 1 - blue / 255.0;
    black = Math.min(c, Math.min(m, y));
    cyan = (c - black) / (1 - black);
    magenta = (m - black) / (1 - black);
    yellow = (y - black) / (1 - black);
  }

  public int toRgb() {
    double c = cyan * (1 - black) + black;
    double m = magenta * (1 - black) + black;
    double y = yellow * (1 - black) + black;
    int r = ImageHelper.integerize((1 - c) * 255);
    int g = ImageHelper.integerize((1 - m) * 255);
    int b = ImageHelper.integerize((1 - y) * 255);
    return ImageHelper.jrgb(r, g, b);
  }
}
