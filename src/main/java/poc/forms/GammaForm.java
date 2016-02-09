package poc.forms;

import poc.functions.GammaFunction;
import poc.tools.ImageProcessor;
import poc.tools.ImageUpdater;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class GammaForm extends BaseForm {
  private JSpinner vSpiner;

  public GammaForm(ImageUpdater parent) {
    super(parent);
    initComponents();
  }

  private void initComponents() {


    setTitle("Contrast...");
    setMinimumSize(new java.awt.Dimension(400, 150));
    setPreferredSize(new java.awt.Dimension(400, 150));

    vSpiner = new JSpinner();
    vSpiner.setValue(1);
    JSlider regularSlider = createSlider();
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
                        .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(regularSlider, GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(vSpiner, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(vSpiner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(regularSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
    );

    pack();
  }

  private JSlider createSlider() {
    JSlider slider = baseSlider(0, 500);
    slider.setValue(100);
    slider.addChangeListener(this::sliderStateChanged);
    return slider;
  }

  private void sliderStateChanged(ChangeEvent evt) {
    JSlider slider = (JSlider) evt.getSource();
    vSpiner.setValue(slider.getValue() / 100.0);
    try {
      // Dla kazdego nowego polozenia suwaka obraz roboczy jest przeliczany od nowa
      ImageProcessor.processImage(updater.getOriginalImage(), updater.getWorkImage(), new GammaFunction(slider.getValue() / 100.0));

      // Po przeliczeniu wartosci obrazu, wymuszenie odrysowania formatki glownej
      updater.repaint();
    } catch (Exception e) {
      System.out.println("gamma error: " + e.getMessage());
    }
  }

}
