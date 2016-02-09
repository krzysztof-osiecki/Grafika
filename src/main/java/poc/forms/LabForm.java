package poc.forms;

import poc.data.LAB;
import poc.functions.LabFunction;
import poc.tools.ImageProcessor;
import poc.tools.ImageUpdater;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class LabForm extends BaseForm {
  public LabForm(ImageUpdater updater) throws HeadlessException {
    super(updater);
    initComponents();
  }

  private JLabel aChange;
  private JLabel lChange;
  private JLabel bChange;
  private int currentL;
  private int currentA;
  private int currentB;

  private void initComponents() {


    setTitle("LAB...");
    setMinimumSize(new Dimension(500, 200));
    setPreferredSize(new Dimension(500, 200));

    lChange = new JLabel();
    aChange = new JLabel();
    bChange = new JLabel();

    JSlider hSlider = createLSlider();
    JLabel hLabel = new JLabel(" L       ");
    JSlider sSlider = createASlider();
    JLabel sLabel = new JLabel(" A");
    JSlider lSlider = createBSlider();
    JLabel lLabel = new JLabel(" B  ");

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
                        .addComponent(lChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(aChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(bChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(lChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(hSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(hLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(aChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(sSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(sLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(bChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(lSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(lLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
    );

    pack();
  }

  private JSlider createLSlider() {
    JSlider slider = baseSlider(-100, 100);
    slider.setValue(0);
    slider.addChangeListener(this::lSliderStateChanged);
    return slider;
  }

  private JSlider createASlider() {
    JSlider slider = baseSlider(-128, 127);
    slider.setValue(0);
    slider.addChangeListener(this::aSliderStateChanged);
    return slider;
  }

  private JSlider createBSlider() {
    JSlider slider = baseSlider(-128, 127);
    slider.setValue(0);
    slider.addChangeListener(this::bSliderStateChanged);
    return slider;
  }

  private void bSliderStateChanged(ChangeEvent evt) {
    JSlider slider = (JSlider) evt.getSource();
    bChange.setText(" " + slider.getValue());
    currentB = slider.getValue();
    procesLab();
  }

  private void aSliderStateChanged(ChangeEvent evt) {
    JSlider slider = (JSlider) evt.getSource();
    aChange.setText(" " + slider.getValue());
    currentA = slider.getValue();
    procesLab();
  }

  private void lSliderStateChanged(ChangeEvent evt) {
    JSlider slider = (JSlider) evt.getSource();
    lChange.setText(" " + slider.getValue());
    currentL = slider.getValue();
    procesLab();
  }

  private void procesLab() {
    try {
      // Dla kazdego nowego polozenia suwaka obraz roboczy jest przeliczany od nowa
      ImageProcessor.processImage(updater.getOriginalImage(), updater.getWorkImage(), new LabFunction(new LAB(currentL, currentA, currentB)));

      // Po przeliczeniu wartosci obrazu, wymuszenie odrysowania formatki glownej
      updater.repaint();
    } catch (Exception e) {
      System.out.println("lab transform error: " + e.getMessage());
    }
  }

}


