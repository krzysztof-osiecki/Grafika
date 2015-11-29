package poc.forms;

import poc.functions.OptimizedGaussian;
import poc.functions.UnsharpedMaskFunction;
import poc.tools.ImageHelper;
import poc.tools.ImageProcessor;
import poc.tools.ImageUpdater;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class UnsharpMaskForm extends BaseForm {

  private JLabel sigma;
  private JLabel radius;
  private JLabel alpha;
  private int currentRadius;
  private double currentSigma;
  private double currentAlpha;

  public UnsharpMaskForm(ImageUpdater updater) throws HeadlessException {
    super(updater);
    initComponents();
  }

  private void initComponents() {

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Unsharp...");
    setMinimumSize(new Dimension(500, 200));
    setPreferredSize(new Dimension(500, 200));

    radius = new JLabel();
    sigma = new JLabel();
    alpha = new JLabel();


    JSlider radiusSlider = createRadiusSlider();
    JLabel radiusLabel = new JLabel(" Radius       ");
    JSlider sigmaSlider = createSigmaSlider();
    JLabel sigmaLabel = new JLabel(" Sigma");
    JSlider alphaSlider = createAlphaSlider();
    JLabel alphaLabel = new JLabel(" alpha");

    //buttons
    JButton cancelButton = createCancelButton();
    JButton okButton = createOkButton();
    JButton applyButton = new JButton();
    applyButton.setText("Apply");
    applyButton.setPreferredSize(new java.awt.Dimension(125, 25));
    applyButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        processGaussian();
      }
    });

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(radiusSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(radiusLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(radius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sigmaSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sigmaLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(sigma, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(alphaSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(alphaLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(alpha, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(applyButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(radius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(radiusSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(radiusLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(sigma, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(sigmaSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(sigmaLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(alpha, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(alphaSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(alphaLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(applyButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
    );

    pack();
  }

  private JSlider createAlphaSlider() {
    JSlider slider = baseSlider(1, 50);
    slider.setValue(1);
    slider.addChangeListener(this::alphaSliderChanged);
    return slider;
  }

  private JSlider createRadiusSlider() {
    JSlider slider = baseSlider(1, 200);
    slider.setValue(1);
    slider.addChangeListener(this::radiusSliderChanged);
    return slider;
  }

  private JSlider createSigmaSlider() {
    JSlider slider = baseSlider(0, 50);
    slider.setValue(0);
    slider.addChangeListener(this::sigmaSliderChanged);
    return slider;
  }

  private void alphaSliderChanged(ChangeEvent evt) {
    JSlider slider = (JSlider) evt.getSource();
    alpha.setText(" " + slider.getValue() / 10.0);
    currentAlpha = slider.getValue() / 10.0;
  }

  private void radiusSliderChanged(ChangeEvent evt) {
    JSlider slider = (JSlider) evt.getSource();
    radius.setText(" " + slider.getValue());
    currentRadius = slider.getValue();
  }

  private void sigmaSliderChanged(ChangeEvent evt) {
    JSlider slider = (JSlider) evt.getSource();
    sigma.setText(" " + slider.getValue() / 10.0);
    currentSigma = slider.getValue() / 10.0;
  }

  private void processGaussian() {
    try {
      // Dla kazdego nowego polozenia suwaka obraz roboczy jest przeliczany od nowa
      ImageProcessor.processImage(updater.getOriginalImage(), updater.getWorkImage(), new UnsharpedMaskFunction(new OptimizedGaussian(calculate1DGaussianFilter()), currentAlpha));

      // Po przeliczeniu wartosci obrazu, wymuszenie odrysowania formatki glownej
      updater.repaint();
    } catch (Exception e) {
      System.out.println("gaussian transform error: " + e.getMessage());
    }
  }

  private Integer[] calculate1DGaussianFilter() {
    Integer[] integers = new Integer[2 * currentRadius + 1];
    for (int x = -currentRadius; x <= currentRadius; x++) {
      integers[x + currentRadius] = ImageHelper.integerize(1000 * Math.exp(-(Math.pow(x, 2)) / (2 * Math.pow(currentSigma, 2))));
    }
    return integers;
  }

}

