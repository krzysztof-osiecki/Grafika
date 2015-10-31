package poc.forms;

import poc.data.HSL;
import poc.functions.HslFunction;
import poc.tools.ImageProcessor;
import poc.tools.ImageUpdater;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class HslForm extends BaseForm {
  public HslForm(ImageUpdater updater) throws HeadlessException {
    super(updater);
    initComponents();
  }


  private JLabel hChange;
  private JLabel sChange;
  private JLabel lChange;
  private double currentH;
  private double currentS;
  private double currentL;

  private void initComponents() {

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("HSL...");
    setMinimumSize(new Dimension(500, 200));
    setPreferredSize(new Dimension(500, 200));

    sChange = new JLabel();
    hChange = new JLabel();
    lChange = new JLabel();

    JSlider hSlider = createHSlider();
    JLabel hLabel = new JLabel(" Hue       ");
    JSlider sSlider = createSSlider();
    JLabel sLabel = new JLabel(" Saturation");
    JSlider lSlider = createLSlider();
    JLabel lLabel = new JLabel(" Lightness  ");

    //buttons
    JButton cancelButton = createCancelButton();
    JButton okButton = createOkButton();

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(hSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(hLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(sChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(hChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(lChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(sChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(hSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(hLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(hChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(sSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(sLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(lSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(lLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
    );

    pack();
  }

  private JSlider createHSlider() {
    JSlider slider = baseSlider(-180, 180);
    slider.setValue(0);
    slider.addChangeListener(this::hSliderStateChanged);
    return slider;
  }

  private JSlider createSSlider() {
    JSlider slider = baseSlider(-255, 255);
    slider.setValue(0);
    slider.addChangeListener(this::sSliderStateChanged);
    return slider;
  }

  private JSlider createLSlider() {
    JSlider slider = baseSlider(-255, 255);
    slider.setValue(0);
    slider.addChangeListener(this::lSliderStateChanged);
    return slider;
  }

  private void lSliderStateChanged(ChangeEvent evt) {
    JSlider slider = (JSlider) evt.getSource();
    lChange.setText(" " + slider.getValue());
    currentL = slider.getValue() / 255.0;
    procesHsl();
  }

  private void sSliderStateChanged(ChangeEvent evt) {
    JSlider slider = (JSlider) evt.getSource();
    hChange.setText(" " + slider.getValue());
    currentH = slider.getValue() / 255.0;
    procesHsl();
  }

  private void hSliderStateChanged(ChangeEvent evt) {
    JSlider slider = (JSlider) evt.getSource();
    sChange.setText(" " + slider.getValue());
    currentS = slider.getValue();
    procesHsl();
  }

  private void procesHsl() {
    try {
      // Dla kazdego nowego polozenia suwaka obraz roboczy jest przeliczany od nowa
      ImageProcessor.processImage(updater.getOriginalImage(), updater.getWorkImage(), new HslFunction(new HSL(currentL, currentH, currentS)));

      // Po przeliczeniu wartosci obrazu, wymuszenie odrysowania formatki glownej
      updater.repaint();
    } catch (Exception e) {
      System.out.println("hsl transform error: " + e.getMessage());
    }
  }

}


