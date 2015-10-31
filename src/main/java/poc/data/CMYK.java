package poc.data;

import poc.tools.ImageHelper;

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

  public CMYK(double cyan, double magenta, double yellow, double black) {
    this.cyan = cyan;
    this.magenta = magenta;
    this.yellow = yellow;
    this.black = black;
  }

  public double getCyan() {
    return cyan;
  }

  public void setCyan(double cyan) {
    this.cyan = normalize(cyan);
  }

  public double getMagenta() {
    return magenta;
  }

  public void setMagenta(double magenta) {
    this.magenta = normalize(magenta);
  }

  public double getYellow() {
    return yellow;
  }

  public void setYellow(double yellow) {
    this.yellow = normalize(yellow);
  }

  public double getBlack() {
    return black;
  }

  public void setBlack(double black) {
    this.black = normalize(black);
  }

  private double normalize(double val){
    if(val < 0) return 0;
    if(val > 1) return 1;
    return val;
  }
}
