package poc;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import poc.forms.*;
import poc.functions.AlignFunction;
import poc.functions.IdentityFunction;
import poc.histogram.HistogramData;
import poc.histogram.HistogramProcessor;
import poc.tools.Channel;
import poc.tools.ImageProcessor;
import poc.tools.ImageUpdater;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Glowna klasa integrujaca wszystki formatki kontrolne i wyswietlajaca obraz
 */
public class Poc extends JFrame implements ImageUpdater {
  // Obrazy
  public BufferedImage backupImage;
  public BufferedImage originalImage;
  public BufferedImage workImage;

  // Formatki zmiany ustawien
  private BrightnessForm brightnessForm;
  private ContrastForm contrastForm;
  private GammaForm gammaForm;
  private HslForm hslForm;
  private CmykForm cmykForm;

  // Klasa obslugujaca histogram
  private HistogramProcessor histogramProcessor;
  private Channel selectedChannel = Channel.ALL;

  // Komponenty do wyświetlania obrazu na formatce glownej
  private JLabel imageLabel;
  private ImageIcon imageIcon;
  private LogForm logForm;
  private ChartPanel histogram;
  private JLabel varianceLabel;
  private JLabel intensityLabel;
  private JComboBox<Channel> comboBox;
  private JScrollPane jScrollPane;
  private JButton alignButton;
  private JButton revertButton;

  public static void main(String[] args) {
    new Poc();
  }

  public Poc() {
    histogramProcessor = new HistogramProcessor();
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
    setCurrentImage(originalImage, workImage);
  }

  /**
   * Metoda aktualizujaca oryginalny obraz danymi z obrazu roboczego
   */
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
                        .addComponent(alignButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup()
                        .addComponent(alignButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(alignButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(revertButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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

    alignButton = new JButton("Equalize");
    alignButton.addActionListener(alignButtonListener());
    alignButton.setVisible(false);

    revertButton = new JButton("Revert");
    revertButton.addActionListener(revertButtonListener());
    revertButton.setVisible(false);

    varianceLabel = new JLabel();
    varianceLabel.setVisible(false);

    intensityLabel = new JLabel();
    intensityLabel.setVisible(false);


  }

  private ActionListener alignButtonListener() {
    return e -> {
      ImageProcessor.processImage(originalImage, workImage, new AlignFunction(selectedChannel));
      this.updateImage();
    };
  }

  private ActionListener revertButtonListener() {
    return e -> {
      backupImage.copyData(originalImage.getRaster());
      backupImage.copyData(workImage.getRaster());
      ImageProcessor.processImage(originalImage, workImage, new IdentityFunction());
      this.repaint();
    };
  }

  private ItemListener comboItemListener() {
    return ae -> {
      selectedChannel = (Channel) ae.getItem();
      repaint();
    };
  }

  private void createMenu() {
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
        alignButton.setVisible(true);
        revertButton.setVisible(true);
      }
      //wyliczenie histogramu
      HistogramData.PIXEL_COUNT = originalImage.getHeight() * originalImage.getWidth();
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

    //item log
    mitem = new JMenuItem("HSL");
    mitem.addActionListener(ae -> {
      hslForm = new HslForm(Poc.this);
      hslForm.pack();
      hslForm.setVisible(true);
    });
    menu.add(mitem);

    //item log
    mitem = new JMenuItem("CMYK");
    mitem.addActionListener(ae -> {
      cmykForm = new CmykForm(Poc.this);
      cmykForm.pack();
      cmykForm.setVisible(true);
    });
    menu.add(mitem);
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
