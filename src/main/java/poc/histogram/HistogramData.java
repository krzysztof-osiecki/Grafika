package poc.histogram;

public class HistogramData {
  public static final int[] RED = new int[256];
  public static final int[] GREEN = new int[256];
  public static final int[] BLUE = new int[256];
  public static final int[] GRAY_SCALE = new int[256];
  public static long PIXEL_COUNT = 0;

  static {
    clear();
  }

  public static void clear() {
    for (int i = 0; i < 256; i++) RED[i] = 0;
    for (int i = 0; i < 256; i++) GREEN[i] = 0;
    for (int i = 0; i < 256; i++) BLUE[i] = 0;
    for (int i = 0; i < 256; i++) GRAY_SCALE[i] = 0;
  }
}
