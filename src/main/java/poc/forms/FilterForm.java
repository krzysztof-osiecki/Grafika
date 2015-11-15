package poc.forms;

import poc.functions.FilterFunction;
import poc.tools.ImageProcessor;
import poc.tools.ImageUpdater;

import javax.swing.*;
import java.awt.event.ActionListener;

public class FilterForm extends BaseForm {

  private JButton cancelButton;
  private JButton okButton;
  private JTable table;
  private JSpinner spinner;
  private JButton applyButton;
  private String[][] filter;

  public FilterForm(ImageUpdater parent) {
    super(parent);
    initComponents();
  }

  private void initComponents() {

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Filter...");
    setMinimumSize(new java.awt.Dimension(400, 150));
    table = getInitialFilterTable(3);
    cancelButton = createCancelButton();
    okButton = createOkButton();
    applyButton = new JButton("Apply");
    applyButton.addActionListener(applyFilter());
    spinner = new JSpinner();
    spinner.setValue(1);
    spinner.addChangeListener(e -> {
      table = getInitialFilterTable((int) spinner.getValue() * 2 + 1);
      setLayout();
      pack();
    });

    setLayout();
    pack();
  }

  private ActionListener applyFilter() {
    return e1 -> {
      try {
        ImageProcessor.processImage(updater.getOriginalImage(), updater.getWorkImage(), new FilterFunction(getInts(), (int) spinner.getValue() * 2 + 1));

        // Po przeliczeniu wartosci obrazu, wymuszenie odrysowania formatki glownej
        updater.repaint();
      } catch (Exception e) {
        System.out.println("hsl transform error: " + e.getMessage());
      }
    };

  }

  private Integer[][] getInts() {
    Integer[][] integers = new Integer[(int) spinner.getValue() * 2 + 1][(int) spinner.getValue() * 2 + 1];
    for (int i = 0; i < (int) spinner.getValue() * 2 + 1; i++) {
      for (int j = 0; j < (int) spinner.getValue() * 2 + 1; j++) {
        integers[i][j] = Integer.valueOf(filter[i][j]);
      }
    }
    return integers;
  }

  private JTable getInitialFilterTable(int size) {
    filter = new String[size][size];
    String[] strings = new String[size];
    for (int i = 0; i < size; i++) {
      strings[i] = "";
      for (int j = 0; j < size; j++) {
        filter[i][j] = String.valueOf(1);
      }
    }
    return new JTable(filter, strings);
  }

  private void setLayout() {
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(table, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(applyButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup()
                    .addComponent(table, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(applyButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
    );
  }
}
