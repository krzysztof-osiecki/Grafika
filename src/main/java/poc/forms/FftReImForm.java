package poc.forms;

import poc.functions.FftFunction;
import poc.tools.ImageUpdater;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FftReImForm extends BaseForm {

  private JLabel reLabel;
  private JLabel imLabel;
  private JButton cancelButton;

  public FftReImForm(ImageUpdater updater) throws HeadlessException {
    super(updater);
    cancelButton = createCancelButton();

    FftFunction fftFunction = new FftFunction(updater.getOriginalImage());
    BufferedImage re = fftFunction.getRe();
    BufferedImage im = fftFunction.getIm();

    reLabel = new JLabel();
    reLabel.setIcon(new ImageIcon(re));
    imLabel = new JLabel();
    imLabel.setIcon(new ImageIcon(im));
    initComponents();
  }

  private void initComponents() {

    setTitle("Fft Re/Im");
    setMinimumSize(new java.awt.Dimension(300, 200));
    setPreferredSize(new java.awt.Dimension(380, 200));

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(reLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                        .addComponent(imLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(reLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(imLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED))
    );

    pack();
  }

}
