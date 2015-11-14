package poc.histogram;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import poc.tools.Channel;
import poc.tools.UIHelper;

public class HistogramProcessor {

  public double getAverangeIntensity() {
    double pixelCount = HistogramData.PIXEL_COUNT;
    double averangeIntensity = 0;
    for (int i = 0; i < 256; i++) {
      averangeIntensity += i * (HistogramData.GRAY_SCALE[i] / pixelCount);
    }
    return averangeIntensity;
  }

  public double getVariance() {
    double pixelCount = HistogramData.PIXEL_COUNT;
    double variance = 0;
    double averangeIntensity = getAverangeIntensity();
    for (int i = 0; i < 256; i++) {
      variance += Math.pow((i - averangeIntensity), 2) * (HistogramData.GRAY_SCALE[i] / pixelCount);
    }
    return variance;
  }

  public JFreeChart getHistogramChart(Channel selectedChannel) {
    JFreeChart chart = ChartFactory.createHistogram(
        "Histogram",
        "", "",
        createDataset(selectedChannel),
        PlotOrientation.VERTICAL,
        true, false, false);
    XYPlot plot = (XYPlot) chart.getPlot();
    ValueAxis domainAxis = plot.getDomainAxis();
    domainAxis.setVisible(false);
    domainAxis.setRange(0, 256);
    ValueAxis rangeAxis = plot.getRangeAxis();
    rangeAxis.setVisible(false);
    rangeAxis.setLowerBound(0);
    XYBarRenderer r = (XYBarRenderer) chart.getXYPlot().getRenderer();
    UIHelper.colorSeries(selectedChannel, r);
    return chart;
  }

  private IntervalXYDataset createDataset(Channel selectedChannel) {
    XYSeriesCollection dataset = new XYSeriesCollection();
    double pixelCount = HistogramData.PIXEL_COUNT;
    final XYSeries red = new XYSeries("red");
    final XYSeries green = new XYSeries("green");
    final XYSeries blue = new XYSeries("blue");
    final XYSeries gray = new XYSeries("gray");
    for (int i = 1; i < 255; i++) {
      red.add(i, HistogramData.RED[i] / pixelCount);
      green.add(i, HistogramData.GREEN[i] / pixelCount);
      blue.add(i, HistogramData.BLUE[i] / pixelCount);
      gray.add(i, HistogramData.GRAY_SCALE[i] / pixelCount);
    }

    switch (selectedChannel) {
      case RED:
        dataset.addSeries(red);
        break;
      case GREEN:
        dataset.addSeries(green);
        break;
      case BLUE:
        dataset.addSeries(blue);
        break;
      case GRAY:
        dataset.addSeries(gray);
        break;
      case ALL:
        dataset.addSeries(red);
        dataset.addSeries(green);
        dataset.addSeries(blue);
        dataset.addSeries(gray);
        break;
    }

    return dataset;
  }
}
