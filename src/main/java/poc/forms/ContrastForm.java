package poc.forms;

import poc.functions.AlternativeContrastFunction;
import poc.functions.ContrastFunction;
import poc.functions.NegativeFunction;
import poc.tools.ImageProcessor;
import poc.tools.ImageUpdater;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ContrastForm extends BaseForm {

  private JLabel regularLabelValue;
  private JLabel alternativeLabelValue;

  public ContrastForm(ImageUpdater updater) {
    super(updater);
    initComponents();
  }

  private void initComponents() {


    setTitle("Contrast...");
    setMinimumSize(new java.awt.Dimension(400, 150));
    setPreferredSize(new java.awt.Dimension(400, 150));

    regularLabelValue = new JLabel();
    alternativeLabelValue = new JLabel();
    JSlider regularSlider = createRegularSlider();
    JLabel regularLabel = new JLabel(" 1version");
    JSlider alternativeSlider = createAlternativeSlider();
    JLabel alternativeLabel = new JLabel(" 2version");
    JButton cancelButton = createCancelButton();
    JButton okButton = createOkButton();
    JButton negativeButton = new JButton();
    negativeButton.setText("negative");
    negativeButton.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        negativeButtonClicked();
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
                        .addComponent(regularSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(regularLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.regularLabelValue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(alternativeSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(alternativeLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(this.alternativeLabelValue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(negativeButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(regularLabelValue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(regularSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(regularLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(alternativeLabelValue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(alternativeSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(alternativeLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(negativeButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
    );

    pack();
  }

  private JSlider createRegularSlider() {
    JSlider slider = baseSlider(-128, 127);
    slider.setValue(0);
    slider.addChangeListener(this::regularSliderStateChanged);
    return slider;
  }

  private JSlider createAlternativeSlider() {
    JSlider slider = baseSlider(-128, 127);
    slider.setValue(0);
    slider.addChangeListener(this::alternativeSliderStateChanged);
    return slider;
  }

  private void alternativeSliderStateChanged(ChangeEvent evt) {
    JSlider slider = (JSlider) evt.getSource();
    alternativeLabelValue.setText(" "+String.valueOf(slider.getValue()));
    try {
      // Dla kazdego nowego polozenia suwaka obraz roboczy jest przeliczany od nowa
      ImageProcessor.processImage(updater.getOriginalImage(), updater.getWorkImage(), new AlternativeContrastFunction(slider.getValue()));

      // Po przeliczeniu wartosci obrazu, wymuszenie odrysowania formatki glownej
      updater.repaint();
    } catch (Exception e) {
      System.out.println("alternative contrast error: " + e.getMessage());
    }
  }

  private void negativeButtonClicked() {
    try {
      // Dla kazdego nowego polozenia suwaka obraz roboczy jest przeliczany od nowa
      ImageProcessor.processImage(updater.getOriginalImage(), updater.getWorkImage(), new NegativeFunction());

      // Po przeliczeniu wartosci obrazu, wymuszenie odrysowania formatki glownej
      updater.repaint();
    } catch (Exception e) {
      System.out.println("negative error: " + e.getMessage());
    }
  }

  private void regularSliderStateChanged(ChangeEvent evt) {
    JSlider slider = (JSlider) evt.getSource();
    regularLabelValue.setText(" "+String.valueOf(slider.getValue()));
    try {
      // Dla kazdego nowego polozenia suwaka obraz roboczy jest przeliczany od nowa
      ImageProcessor.processImage(updater.getOriginalImage(), updater.getWorkImage(), new ContrastFunction(slider.getValue()));

      // Po przeliczeniu wartosci obrazu, wymuszenie odrysowania formatki glownej
      updater.repaint();
    } catch (Exception e) {
      System.out.println("regular contrast error: " + e.getMessage());
    }
  }
}
