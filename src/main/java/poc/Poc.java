package poc;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import poc.functions.AlignFunction;
import poc.functions.IdentityFunction;
import poc.functions.ScaleFunction;
import poc.histogram.HistogramData;
import poc.histogram.HistogramProcessor;
import poc.tools.Channel;
import poc.tools.ImageProcessor;
import poc.tools.ImageUpdater;
import poc.tools.UIHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Glowna klasa integrujaca wszystki formatki kontrolne i wyswietlajaca obraz
 */
public class Poc extends JFrame implements ImageUpdater {
  private final UIHelper uiHelper;
  private final HistogramProcessor histogramProcessor;
  private Channel selectedChannel = Channel.ALL;

  // Obrazy
  public BufferedImage backupImage;
  public BufferedImage originalImage;
  public BufferedImage workImage;

  // Komponenty do wyświetlania obrazu na formatce glownej
  private JLabel imageLabel;
  private ImageIcon imageIcon;
  private ChartPanel histogram;
  private JLabel varianceLabel;
  private JLabel intensityLabel;
  private JComboBox<Channel> comboBox;
  private JScrollPane jScrollPane;
  private JButton equalizeButton;
  private JButton revertButton;
  private JSpinner aSpinner;
  private JSpinner bSpinner;
  private JSpinner cSpinner;
  private JSpinner dSpinner;

  public static void main(String[] args) {
    new Poc();
  }

  public Poc() {
    uiHelper = new UIHelper(this);
    histogramProcessor = new HistogramProcessor();
    setTitle("Poc - Krzysztof Osiecki");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    initComponents();
    setVisible(true);
    setSize(1400, 800);
  }

  @Override
  public void repaint() {
    imageLabel.repaint();
    repaintHistogram();
    super.repaint();
  }

  @Override
  public void revertImage() {
    setCurrentImage(originalImage, workImage);
  }

  @Override
  public void updateImage() {
    setCurrentImage(workImage, originalImage);
  }

  @Override
  public BufferedImage getOriginalImage() {
    return originalImage;
  }

  @Override
  public BufferedImage getWorkImage() {
    return workImage;
  }

  private void readImage(String fn) {
    try {
      // Wczytanie obrazu z pliku
      BufferedImage loadImage = ImageIO.read(new File(fn));

      // Oryginalny obraz tworzony na podstawie wczytanego z ewentualna konwersja obrazu do formatu 32 bit RGB
      originalImage = new BufferedImage(loadImage.getWidth(), loadImage.getHeight(), BufferedImage.TYPE_INT_RGB);
      originalImage.getGraphics().drawImage(loadImage, 0, 0, null);

      // Roboczy obraz tworzony jako kopia oryginalnego
      backupImage = new BufferedImage(loadImage.getWidth(), loadImage.getHeight(), BufferedImage.TYPE_INT_RGB);
      workImage = new BufferedImage(loadImage.getWidth(), loadImage.getHeight(), BufferedImage.TYPE_INT_RGB);
      originalImage.copyData(workImage.getRaster());
      originalImage.copyData(backupImage.getRaster());

      // Wyswietlenie roboczego obrazu na formatce
      imageIcon = new ImageIcon(workImage);
      imageLabel.setIcon(imageIcon);

    } catch (Exception e) {
      System.out.println("Image read error: " + e.getMessage());
    }
  }

  private void initComponents() {
    createMenu();
    createMainWindow();
    createLayouts();
  }

  private void createLayouts() {
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup()
                .addComponent(jScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                    .addComponent(histogram, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(intensityLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(varianceLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(equalizeButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(aSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap()
                        .addComponent(bSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap()
                        .addComponent(dSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                )
                .addGroup(layout.createParallelGroup()
                    .addComponent(equalizeButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(revertButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(histogram, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                )
            )
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(jScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(histogram, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup()
                        .addComponent(jScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(intensityLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(varianceLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup()
                            .addComponent(equalizeButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(revertButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup()
                            .addComponent(aSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(10)
                            .addComponent(bSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup()
                            .addComponent(cSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(10)
                            .addComponent(dSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                ))

    );
  }

  private void createMainWindow() {
    imageLabel = new JLabel();
    jScrollPane = new JScrollPane(imageLabel);

    histogram = new ChartPanel(null);
    histogram.setPreferredSize(new java.awt.Dimension(700, 500));

    comboBox = new JComboBox<>(Channel.values());
    comboBox.addItemListener(comboItemListener());
    comboBox.setVisible(false);

    equalizeButton = new JButton("Equalize");
    equalizeButton.addActionListener(alignButtonListener());
    equalizeButton.setVisible(false);

    revertButton = new JButton("Revert");
    revertButton.addActionListener(revertButtonListener());
    revertButton.setVisible(false);

    varianceLabel = new JLabel();
    varianceLabel.setVisible(false);

    intensityLabel = new JLabel();
    intensityLabel.setVisible(false);

    aSpinner = new JSpinner();
    aSpinner.setVisible(false);

    bSpinner = new JSpinner();
    bSpinner.setVisible(false);
    bSpinner.setValue(255);

    cSpinner = new JSpinner();
    cSpinner.setVisible(false);

    dSpinner = new JSpinner();
    dSpinner.setVisible(false);
    dSpinner.setValue(255);
  }

  private void createMenu() {
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);
    //submenu file
    JMenu menu = new JMenu("File");
    menuBar.add(menu);
    //item otwierania pliku
    uiHelper.menuItem(menu, "Open", openFileListener());
    menu.addSeparator();
    //item wyjscia z aplikacji
    uiHelper.menuItem(menu, "Exit", ae -> System.exit(0));

    //submenu przeksztalcen koloru
    menu = new JMenu("Colors");
    menuBar.add(menu);

    uiHelper.menuItem(menu, "Brightness", uiHelper.brightnessForm(this));
    uiHelper.menuItem(menu, "Contrast", uiHelper.contrastForm(this));
    uiHelper.menuItem(menu, "Gamma", uiHelper.gammaForm(this));

    menu = new JMenu("Models");
    menuBar.add(menu);
    uiHelper.menuItem(menu, "HSL", uiHelper.hslForm(this));
    uiHelper.menuItem(menu, "CMYK", uiHelper.cmykForm(this));
    uiHelper.menuItem(menu, "LAB1", uiHelper.labForm(this));
    uiHelper.menuItem(menu, "LUV", uiHelper.luvForm(this));

    menu = new JMenu("Filter");
    menuBar.add(menu);
    uiHelper.menuItem(menu, "Filter", uiHelper.filterForm(this));
    uiHelper.menuItem(menu, "Gaussian", uiHelper.gaussianForm(this));
    uiHelper.menuItem(menu, "Unsharp mask", uiHelper.unsharpMaskForm(this));
  }

  private ChangeListener scaleSpinnersListener() {
    return e -> {
      this.revertImage();
      ImageProcessor.processImage(originalImage, workImage, new ScaleFunction((int) aSpinner.getValue(), (int)
          bSpinner.getValue(), (int) cSpinner.getValue(), (int) dSpinner.getValue(), selectedChannel));
    };
  }

  private ActionListener alignButtonListener() {
    return e -> {
      ImageProcessor.processImage(originalImage, workImage, new AlignFunction(selectedChannel));
      this.updateImage();
    };
  }

  private ActionListener revertButtonListener() {
    return e -> {
      aSpinner.setValue(0);
      bSpinner.setValue(255);
      cSpinner.setValue(0);
      dSpinner.setValue(255);
      backupImage.copyData(originalImage.getRaster());
      backupImage.copyData(workImage.getRaster());
      ImageProcessor.processImage(originalImage, workImage, new IdentityFunction());
      this.repaint();
    };
  }

  private ItemListener comboItemListener() {
    return ae -> {
      this.updateImage();
      aSpinner.setValue(0);
      bSpinner.setValue(255);
      cSpinner.setValue(0);
      dSpinner.setValue(255);
      selectedChannel = (Channel) ae.getItem();
      repaint();
    };
  }

  private ActionListener openFileListener() {
    return ae -> {
      JFileChooser fc = new JFileChooser();
      int returnVal = fc.showOpenDialog(null);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        String fname = file.getPath();
        readImage(fname);
        intensityLabel.setVisible(true);
        varianceLabel.setVisible(true);
        comboBox.setVisible(true);
        equalizeButton.setVisible(true);
        revertButton.setVisible(true);
        aSpinner.setVisible(true);
        bSpinner.setVisible(true);
        cSpinner.setVisible(true);
        dSpinner.setVisible(true);
        aSpinner.addChangeListener(scaleSpinnersListener());
        bSpinner.addChangeListener(scaleSpinnersListener());
        cSpinner.addChangeListener(scaleSpinnersListener());
        dSpinner.addChangeListener(scaleSpinnersListener());
      }
      //wyliczenie histogramu
      HistogramData.PIXEL_COUNT = originalImage.getHeight() * originalImage.getWidth();
      ImageProcessor.processImage(originalImage, workImage, new IdentityFunction());
      repaint();
    };
  }

  private void repaintHistogram() {
    JFreeChart chart = histogramProcessor.getHistogramChart(selectedChannel);
    intensityLabel.setText("Intensity: " + histogramProcessor.getAverangeIntensity());
    varianceLabel.setText("Variance: " + histogramProcessor.getVariance());
    histogram.setChart(chart);
    histogram.repaint();
  }

  private void setCurrentImage(BufferedImage source, BufferedImage destination) {
    try {
      source.copyData(destination.getRaster());
      imageIcon.setImage(workImage);
      imageLabel.setIcon(imageIcon);
      imageLabel.repaint();
      repaintHistogram();
    } catch (Exception e) {
      System.out.println("Error in revertImage: " + e.getMessage());
    }
  }
}
