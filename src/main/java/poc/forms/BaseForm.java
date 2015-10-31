package poc.forms;

import poc.tools.ImageUpdater;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BaseForm extends JFrame {
  protected ImageUpdater updater;

  public BaseForm(ImageUpdater updater) throws HeadlessException {
    super();
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.updater = updater;
  }

  protected JButton createOkButton() {
    JButton okButton = new JButton();
    okButton.setText("OK");
    okButton.setPreferredSize(new java.awt.Dimension(125, 25));
    okButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        okButtonMouseClicked();
      }
    });
    return okButton;
  }

  protected JButton createCancelButton() {
    JButton cancelButton = new JButton();
    cancelButton.setText("Cancel");
    cancelButton.setPreferredSize(new Dimension(125, 25));
    cancelButton.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        cancelButtonMouseClicked();
      }
    });
    return cancelButton;
  }

  protected void cancelButtonMouseClicked() {
    updater.revertImage();
    setVisible(false);
    dispose();
  }

  protected void okButtonMouseClicked() {
    updater.updateImage();
    setVisible(false);
    dispose();
  }

  protected JSlider baseSlider(int min, int max) {
    JSlider slider = new JSlider();
    slider.setMaximum(max);
    slider.setMinimum(min);
    return slider;
  }
}
