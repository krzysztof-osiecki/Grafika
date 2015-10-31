package poc.tools;

/**
 * Klasa zawiera pomocnicze statyczne funkcje do obslugi obrazow
 */
public class ImageHelper {

  public static int jrgb(int r, int g, int b) {
    return (r << 16) + (g << 8) + b;
  }

  public static int jred(int rgb) {
    return ((rgb >> 16) & 0xff);
  }

  public static int jgreen(int rgb) {
    return ((rgb >> 8) & 0xff);
  }

  public static int jblue(int rgb) {
    return (rgb & 0xff);
  }

  public static int clamp(int v, int min, int max) {
    if (v <= min) return min;
    if (v >= max) return max;
    return v;
  }

  public static int integerize(double v) {
    Double floor = Math.floor(v);
    return floor.intValue();
  }

  public static double handleAngle(double v) {
    if (v >= 0) {
      while(v >= 360) v-=360;
      return v;
    } else {
      while (v <= -360) v+=360;
      return  360 - v;
    }
  }
}
