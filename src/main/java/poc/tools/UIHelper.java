package poc.tools;

import org.jfree.chart.renderer.xy.XYBarRenderer;

import java.awt.*;

public class UIHelper {
  public static void colorSeries(Channel selectedChannel, XYBarRenderer r) {
    int alpha = 128;
    switch (selectedChannel) {
      case RED:
        r.setSeriesPaint(0, new Color(255, 0, 0, alpha));
        break;
      case GREEN:
        r.setSeriesPaint(0, new Color(0, 255, 0, alpha));
        break;
      case BLUE:
        r.setSeriesPaint(0, new Color(0, 0, 255, alpha));
        break;
      case GRAY:
        r.setSeriesPaint(0, new Color(255, 255, 255, alpha));
        break;
      case ALL:
        r.setSeriesPaint(0, new Color(255, 0, 0, alpha));
        r.setSeriesPaint(1, new Color(0, 255, 0, alpha));
        r.setSeriesPaint(2, new Color(0, 0, 255, alpha));
        r.setSeriesPaint(3, new Color(255, 255, 255, alpha));
        break;
    }
  }
}
