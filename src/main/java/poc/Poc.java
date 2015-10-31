package poc;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import poc.forms.BrightnessForm;
import poc.forms.ContrastForm;
import poc.forms.GammaForm;
import poc.forms.LogForm;
import poc.functions.IdentityFunction;
import poc.tools.Channel;
import poc.histogram.HistogramProcessor;
import poc.tools.ImageProcessor;
import poc.tools.ImageUpdater;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Glowna klasa integrujaca wszystki formatki kontrolne i wyswietlajaca obraz
 */
public class Poc extends JFrame implements ImageUpdater {
  // Obrazy
  public BufferedImage originalImage;
  public BufferedImage workImage;

  // Formatki zmiany ustawien
  private BrightnessForm brightnessForm;
  private ContrastForm contrastForm;
  private GammaForm gammaForm;

  // Klasa obslugujaca histogram
  private HistogramProcessor histogramProcessor;

  // Komponenty do wyświetlania obrazu na formatce glownej
  private JLabel imageLabel;
  private ImageIcon imageIcon;
  private LogForm logForm;
  private ChartPanel histogram;
  private Channel selectedChannel = Channel.ALL;
  private JLabel varianceLabel;
  private JLabel intensityLabel;
  private JComboBox<Channel> comboBox;

  public static void main(String[] args) {
    new Poc();
  }

  public Poc() {
    histogramProcessor = new HistogramProcessor(this);
    setTitle("Poc - Krzysztof Osiecki");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    initComponents();
    setVisible(true);
    setSize(1400, 700);
  }

  @Override
  public void repaint() {
    imageLabel.repaint();
    repaintHistogram();
    super.repaint();
  }

  /**
   * Metoda przywracająca oryginalny obraz do obrazu roboczego
   */
  @Override
  public void revertImage() {
    try {
      originalImage.copyData(workImage.getRaster());
      imageIcon.setImage(workImage);
      imageLabel.setIcon(imageIcon);
      imageLabel.repaint();
      repaintHistogram();
    } catch (Exception e) {
      System.out.println("Error in revertImage: " + e.getMessage());
    }
  }

  /**
   * Metoda aktualizujaca oryginalny obraz danymi z obrazu roboczego
   */
  @Override
  public void updateImage() {
    try {
      workImage.copyData(originalImage.getRaster());
      imageIcon.setImage(workImage);
      imageLabel.setIcon(imageIcon);
      repaintHistogram();
    } catch (Exception e) {
      System.out.println("Error in updateImage: " + e.getMessage());
    }
  }

  @Override
  public BufferedImage getOriginalImage() {
    return originalImage;
  }

  @Override
  public BufferedImage getWorkImage() {
    return workImage;
  }

  /**
   * Metoda wczytujaca obraz z pliku i tworzaca na jego podstawie obraz oryginalny i jego kopie robocza
   */
  private void readImage(String fn) {
    try {
      // Wczytanie obrazu z pliku
      BufferedImage loadImage = ImageIO.read(new File(fn));

      // Oryginalny obraz tworzony na podstawie wczytanego z ewentualna konwersja obrazu do formatu 32 bit RGB
      originalImage = new BufferedImage(loadImage.getWidth(), loadImage.getHeight(), BufferedImage.TYPE_INT_RGB);
      originalImage.getGraphics().drawImage(loadImage, 0, 0, null);

      // Roboczy obraz tworzony jako kopia oryginalnego
      workImage = new BufferedImage(loadImage.getWidth(), loadImage.getHeight(), BufferedImage.TYPE_INT_RGB);
      originalImage.copyData(workImage.getRaster());

      // Wyswietlenie roboczego obrazu na formatce
      imageIcon = new ImageIcon(workImage);
      imageLabel.setIcon(imageIcon);

    } catch (Exception e) {
      System.out.println("Image read error: " + e.getMessage());
    }
  }

  private void initComponents() {
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);
    //submenu file
    JMenu menu = new JMenu("File");
    menuBar.add(menu);
    //item otwierania pliku
    JMenuItem mitem = new JMenuItem("Open");
    mitem.addActionListener(ae -> {
      JFileChooser fc = new JFileChooser();
      int returnVal = fc.showOpenDialog(null);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        String fname = file.getPath();
        readImage(fname);
        intensityLabel.setVisible(true);
        varianceLabel.setVisible(true);
        comboBox.setVisible(true);

      }
      //wyliczenie histogramu
      ImageProcessor.processImage(originalImage, workImage, new IdentityFunction());
      repaint();
    });
    menu.add(mitem);
    menu.addSeparator();
    //item wyjscia z aplikacji
    mitem = new JMenuItem("Exit");
    mitem.addActionListener(event -> System.exit(0));
    menu.add(mitem);

    //submenu przeksztalcen koloru
    menu = new JMenu("Colors");
    menuBar.add(menu);

    //item jasnosci
    mitem = new JMenuItem("Brightness");
    mitem.addActionListener(ae -> {
      brightnessForm = new BrightnessForm(Poc.this);
      brightnessForm.pack();
      brightnessForm.setVisible(true);
    });
    menu.add(mitem);

    //item kontrastu
    mitem = new JMenuItem("Contrast");
    mitem.addActionListener(ae -> {
      contrastForm = new ContrastForm(Poc.this);
      contrastForm.pack();
      contrastForm.setVisible(true);
    });
    menu.add(mitem);

    //item gamma
    mitem = new JMenuItem("Gamma");
    mitem.addActionListener(ae -> {
      gammaForm = new GammaForm(Poc.this);
      gammaForm.pack();
      gammaForm.setVisible(true);
    });
    menu.add(mitem);

    //item log
    mitem = new JMenuItem("Log");
    mitem.addActionListener(ae -> {
      logForm = new LogForm(Poc.this);
      logForm.pack();
      logForm.setVisible(true);
    });
    menu.add(mitem);

    imageLabel = new JLabel();
    JScrollPane jScrollPane = new JScrollPane(imageLabel);

    histogram = new ChartPanel(null);
    histogram.setPreferredSize(new java.awt.Dimension(700, 500));


    comboBox = new JComboBox<>(Channel.values());
    comboBox.addItemListener(ae -> {
      selectedChannel = (Channel) ae.getItem();
      repaint();
    });

    intensityLabel = new JLabel();
    varianceLabel = new JLabel();

    intensityLabel.setVisible(false);
    varianceLabel.setVisible(false);
    comboBox.setVisible(false);

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup()
                .addComponent(jScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup()
                .addComponent(histogram, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(intensityLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(varianceLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(jScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(histogram, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup()
                        .addComponent(jScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(intensityLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(varianceLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                ))

    );
  }

  private void repaintHistogram() {
    JFreeChart chart = histogramProcessor.getHistogramChart(selectedChannel);
    intensityLabel.setText("Intensity: " + histogramProcessor.getAverangeIntensity());
    varianceLabel.setText("Variance: " + histogramProcessor.getVariance());
    histogram.setChart(chart);
    histogram.repaint();
  }
}
