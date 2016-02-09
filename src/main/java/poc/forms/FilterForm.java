package poc.forms;

import poc.functions.FftConvolution;
import poc.functions.FilterFunction;
import poc.tools.ImageProcessor;
import poc.tools.ImageUpdater;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;

public class FilterForm extends BaseForm {

  private JButton cancelButton;
  private JButton okButton;
  private JSpinner spinner;
  private JButton applyButton;
  private String[][] filter;
  private JButton minButton;
  private JButton maxButton;
  private JButton medianButton;
  private JCheckBox checkBox;
  private JScrollPane jScrollPane;
  private String[] strings;
  private DefaultTableModel defaultTableModel;

  public FilterForm(ImageUpdater parent) {
    super(parent);
    initComponents();
  }

  private void initComponents() {

    setTitle("Filter...");
    setMinimumSize(new java.awt.Dimension(400, 150));
    cancelButton = createCancelButton();
    okButton = createOkButton();
    applyButton = new JButton("Apply");
    applyButton.addActionListener(applyFilter());
    minButton = new JButton("Min");
    minButton.addActionListener(applyMinFilter());
    maxButton = new JButton("Max");
    maxButton.addActionListener(applyMaxFilter());
    medianButton = new JButton("Median");
    medianButton.addActionListener(applyMedianFilter());
    spinner = new JSpinner();
    spinner.setValue(1);
    spinner.addChangeListener(e -> fillInitialFilterTable((int) spinner.getValue() * 2 + 1));
    checkBox = new JCheckBox();
    filter = new String[3][];
    strings = new String[3];
    defaultTableModel = new DefaultTableModel(filter, strings);
    fillInitialFilterTable(3);
    JTable table = new JTable(defaultTableModel);
    table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
    jScrollPane = new JScrollPane(table);
    jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    setLayout();
    pack();
  }

  private ActionListener applyMinFilter() {
    return e1 -> {
      try {
        ImageProcessor.processImage(updater.getOriginalImage(), updater.getWorkImage(), new MinFilterFunction(getInts(), (int) spinner.getValue() * 2 + 1));

        // Po przeliczeniu wartosci obrazu, wymuszenie odrysowania formatki glownej
        updater.repaint();
        updater.updateImage();
      } catch (Exception e) {
        System.out.println("min filter transform error: " + e.getMessage());
      }
    };

  }

  private ActionListener applyMaxFilter() {
    return e1 -> {
      try {
        ImageProcessor.processImage(updater.getOriginalImage(), updater.getWorkImage(), new MaxFilterFunction(getInts(), (int) spinner.getValue() * 2 + 1));
        // Po przeliczeniu wartosci obrazu, wymuszenie odrysowania formatki glownej
        updater.repaint();
        updater.updateImage();
      } catch (Exception e) {
        System.out.println("max filter transform error: " + e.getMessage());
      }
    };

  }

  private ActionListener applyMedianFilter() {
    return e1 -> {
      try {
        ImageProcessor.processImage(updater.getOriginalImage(), updater.getWorkImage(), new MedianFilterFunction(getInts(), (int) spinner.getValue() * 2 + 1));

        // Po przeliczeniu wartosci obrazu, wymuszenie odrysowania formatki glownej
        updater.repaint();
        updater.updateImage();
      } catch (Exception e) {
        System.out.println("median filter transform error: " + e.getMessage());
      }
    };

  }

  private ActionListener applyFilter() {
    return e1 -> {
      try {
        if (checkBox.isSelected()) {
          FftConvolution fftConvolution = new FftConvolution(updater.getWorkImage(), getInts());
          ImageProcessor.processImage(fftConvolution.getFiltered(), updater.getWorkImage());
        } else {
          ImageProcessor.processImage(updater.getOriginalImage(), updater.getWorkImage(), new FilterFunction(getInts(), (int) spinner.getValue() * 2 + 1));
        }
        // Po przeliczeniu wartosci obrazu, wymuszenie odrysowania formatki glownej
        updater.repaint();
      } catch (Exception e) {
        System.out.println("filter transform error: " + e.getMessage());
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

  private void fillInitialFilterTable(int size) {
    defaultTableModel.setColumnCount((int) spinner.getValue() * 2 + 1);
    defaultTableModel.setRowCount((int) spinner.getValue() * 2 + 1);
    filter = new String[size][size];
    strings = new String[size];
    for (int i = 0; i < size; i++) {
      strings[i] = "";
      for (int j = 0; j < size; j++) {
        filter[i][j] = String.valueOf(1);
        defaultTableModel.setValueAt(String.valueOf(1), i, j);
      }
    }
    defaultTableModel.setColumnIdentifiers(strings);
  }

  private void setLayout() {
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinner, 50, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(applyButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(minButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(maxButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(medianButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(checkBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup()
                    .addComponent(jScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(applyButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(minButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(maxButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(medianButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
    );
  }
}
