package poc.forms;

import poc.data.CMYK;
import poc.functions.CmykFunction;
import poc.tools.ImageProcessor;
import poc.tools.ImageUpdater;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class CmykForm extends BaseForm {
  private JLabel blackChange;
  private JLabel magentaChange;
  private JLabel cyanChange;
  private JLabel yellowChange;
  private double currentCyan;
  private double currentMagenta;
  private double currentYellow;
  private double currentBlack;

  public CmykForm(ImageUpdater updater) throws HeadlessException {
    super(updater);
    initComponents();
  }

  private void initComponents() {
    setTitle("CMYK...");
    setMinimumSize(new Dimension(400, 200));
    setPreferredSize(new Dimension(400, 200));

    cyanChange = new JLabel();
    magentaChange = new JLabel();
    yellowChange = new JLabel();
    blackChange = new JLabel();

    JSlider cyanSlider = createCyanSlider();
    JLabel cyanLabel = new JLabel(" cyan   ");
    JSlider magentaSlider = createMagentaSlider();
    JLabel magentaLabel = new JLabel(" magenta");
    JSlider yellowSlider = createYellowSlider();
    JLabel yellowLabel = new JLabel(" yellow ");
    JSlider blackSlider = createBlackSlider();
    JLabel blackLabel = new JLabel(" black  ");

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
                        .addComponent(cyanSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cyanLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(cyanChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(magentaSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(magentaLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(magentaChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(yellowSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(yellowLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(yellowChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(blackSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(blackLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(blackChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(cyanChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cyanSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cyanLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(magentaChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(magentaSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(magentaLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(yellowChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(yellowSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(yellowLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(blackChange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(blackSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(blackLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
    );

    pack();
  }

  private JSlider createCyanSlider() {
    JSlider slider = baseSlider(-255, 255);
    slider.setValue(0);
    slider.addChangeListener(this::cyanSliderStateChanged);
    return slider;
  }

  private JSlider createMagentaSlider() {
    JSlider slider = baseSlider(-255, 255);
    slider.setValue(0);
    slider.addChangeListener(this::magentaSliderStateChanged);
    return slider;
  }

  private JSlider createYellowSlider() {
    JSlider slider = baseSlider(-255, 255);
    slider.setValue(0);
    slider.addChangeListener(this::yellowSliderStateChanged);
    return slider;
  }

  private JSlider createBlackSlider() {
    JSlider slider = baseSlider(-255, 255);
    slider.setValue(0);
    slider.addChangeListener(this::blackSliderStateChanged);
    return slider;
  }

  private void blackSliderStateChanged(ChangeEvent evt) {
    JSlider slider = (JSlider) evt.getSource();
    blackChange.setText(" " + slider.getValue());
    currentBlack = slider.getValue()/255.0;
    procesCmyk();
  }

  private void yellowSliderStateChanged(ChangeEvent evt) {
    JSlider slider = (JSlider) evt.getSource();
    yellowChange.setText(" " + slider.getValue());
    currentYellow = slider.getValue()/255.0;
    procesCmyk();
  }

  private void magentaSliderStateChanged(ChangeEvent evt) {
    JSlider slider = (JSlider) evt.getSource();
    magentaChange.setText(" " + slider.getValue());
    currentMagenta = slider.getValue()/255.0;
    procesCmyk();
  }

  private void cyanSliderStateChanged(ChangeEvent evt) {
    JSlider slider = (JSlider) evt.getSource();
    cyanChange.setText(" " + slider.getValue());
    currentCyan = slider.getValue()/255.0;
    procesCmyk();
  }

  private void procesCmyk() {
    try {
      // Dla kazdego nowego polozenia suwaka obraz roboczy jest przeliczany od nowa
      ImageProcessor.processImage(updater.getOriginalImage(), updater.getWorkImage(), new CmykFunction(new CMYK(currentCyan, currentMagenta, currentYellow, currentBlack)));

      // Po przeliczeniu wartosci obrazu, wymuszenie odrysowania formatki glownej
      updater.repaint();
    } catch (Exception e) {
      System.out.println("cmyk transform error: " + e.getMessage());
    }
  }


}
