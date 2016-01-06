package poc.forms;

import poc.functions.FftFunction;
import poc.tools.ImageUpdater;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FftMaPhForm extends BaseForm {

  private JLabel maLabel;
  private JLabel phLabel;
  private JButton cancelButton;

  public FftMaPhForm(ImageUpdater updater) throws HeadlessException {
    super(updater);
    cancelButton = createCancelButton();

    FftFunction fftFunction = new FftFunction(updater.getOriginalImage());
    BufferedImage ma = fftFunction.getMa();
    BufferedImage ph = fftFunction.getPh();

    maLabel = new JLabel();
    maLabel.setIcon(new ImageIcon(ma));
    phLabel = new JLabel();
    phLabel.setIcon(new ImageIcon(ph));
    initComponents();
  }

  private void initComponents() {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Fft Ma/Ph");
    setMinimumSize(new Dimension(300, 200));
    setPreferredSize(new Dimension(380, 200));

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(maLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                        .addComponent(phLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(maLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(phLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED))
    );

    pack();
  }

}
