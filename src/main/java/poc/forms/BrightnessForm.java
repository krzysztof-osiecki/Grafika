package poc.forms;

import poc.functions.BrightnessFunction;
import poc.tools.ImageProcessor;
import poc.tools.ImageUpdater;

import javax.swing.*;
import javax.swing.event.ChangeEvent;


public class BrightnessForm extends BaseForm {
  private JLabel vSpiner;

  public BrightnessForm(ImageUpdater updater) {
    super(updater);
    initComponents();
  }

  private void initComponents() {
    vSpiner = new JLabel();
    JSlider brightnessSlider = new JSlider();
    JButton cancelButton = createCancelButton();
    JButton okButton = createOkButton();


    setTitle("Brightness...");
    setMinimumSize(new java.awt.Dimension(300, 200));
    setPreferredSize(new java.awt.Dimension(380, 200));

    brightnessSlider.setMaximum(255);
    brightnessSlider.setMinimum(-255);
    brightnessSlider.setValue(0);
    brightnessSlider.addChangeListener(this::brightnessSliderStateChanged);

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
                        .addComponent(brightnessSlider, GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
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
                    .addComponent(brightnessSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
    );

    pack();
  }

  // Reakcja na zmiane polozenia suwaka
  private void brightnessSliderStateChanged(ChangeEvent evt) {
    JSlider slider = (JSlider) evt.getSource();
    vSpiner.setText(" "+slider.getValue());
    try {
      // Dla kazdego nowego polozenia suwaka obraz roboczy jest przeliczany od nowa
      ImageProcessor.processImage(updater.getOriginalImage(), updater.getWorkImage(), new BrightnessFunction(slider.getValue()));

      // Po przeliczeniu wartosci obrazu, wymuszenie odrysowania formatki glownej
      updater.repaint();
    } catch (Exception e) {
      System.out.println("brightness error: " + e.getMessage());
    }
  }
}
