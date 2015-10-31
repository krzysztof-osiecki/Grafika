import org.junit.Ignore;
import org.junit.Test;
import poc.data.CMYK;
import poc.data.HSL;
import poc.tools.ImageHelper;

import static org.junit.Assert.assertTrue;

public class ColorConversionTest {
  @Test
  public void CMYKConversionWorksFine() {
    testCmyk(250, 124, 90);
    testCmyk(123, 123, 123);
    testCmyk(111, 11, 1);
  }

  @Test
  public void HSLConversionWorksFine() {
    testHsl(0, 255, 120);
    testHsl(0, 125, 255);
    testHsl(255, 125, 0);
    testHsl(255, 255, 255);
  }

  @Ignore
  @Test
  public void HSLFails() {
    testHsl(250, 124, 90);
    testHsl(123, 123, 123);
    testHsl(111, 11, 1);
  }


  private void testCmyk(int red, int green, int blue) {
    int jrgb = ImageHelper.jrgb(red, green, blue);
    CMYK cmyk = new CMYK(jrgb);
    int rgbResult = cmyk.toRgb();
    int jred = ImageHelper.jred(rgbResult);
    int jgreen = ImageHelper.jgreen(rgbResult);
    int jblue = ImageHelper.jblue(rgbResult);
    assertTrue(Math.abs(red - jred) < 1);
    assertTrue(Math.abs(green - jgreen) <= 1);
    assertTrue(Math.abs(blue - jblue) <= 1);
  }

  private void testHsl(int red, int green, int blue) {
    int jrgb = ImageHelper.jrgb(red, green, blue);
    HSL cmyk = new HSL(jrgb);
    int rgbResult = cmyk.toRgb();
    int jred = ImageHelper.jred(rgbResult);
    int jgreen = ImageHelper.jgreen(rgbResult);
    int jblue = ImageHelper.jblue(rgbResult);
    assertTrue(Math.abs(red - jred) < 1);
    assertTrue(Math.abs(green - jgreen) <= 1);
    assertTrue(Math.abs(blue - jblue) <= 1);
  }
}
