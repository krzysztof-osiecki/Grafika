package poc.tools;

import poc.Poc;
import poc.forms.*;

import javax.swing.*;
import java.awt.event.ActionListener;

public class UIHelper {
  public ImageUpdater updater;

  public UIHelper(ImageUpdater updater) {
    this.updater = updater;
  }

  public void menuItem(JMenu menu, String name, ActionListener actionListener) {
    JMenuItem mitem = new JMenuItem(name);
    mitem.addActionListener(actionListener);
    menu.add(mitem);
  }

  public ActionListener brightnessForm(Poc poc) {
    return ae -> {
      BrightnessForm brightnessForm = new BrightnessForm(poc);
      brightnessForm.pack();
      brightnessForm.setVisible(true);
    };
  }

  public ActionListener gammaForm(Poc poc) {
    return ae -> {
      GammaForm gammaForm = new GammaForm(poc);
      gammaForm.pack();
      gammaForm.setVisible(true);
    };
  }

  public ActionListener contrastForm(Poc poc) {
    return ae -> {
      ContrastForm contrastForm = new ContrastForm(poc);
      contrastForm.pack();
      contrastForm.setVisible(true);
    };
  }

  public ActionListener hslForm(Poc poc) {
    return ae -> {
      HslForm hslForm = new HslForm(poc);
      hslForm.pack();
      hslForm.setVisible(true);
    };
  }

  public ActionListener cmykForm(Poc poc) {
    return ae -> {
      CmykForm cmykForm = new CmykForm(poc);
      cmykForm.pack();
      cmykForm.setVisible(true);
    };
  }

  public ActionListener labForm(Poc poc) {
    return ae -> {
      LabForm labForm = new LabForm(poc);
      labForm.pack();
      labForm.setVisible(true);
    };
  }

  public ActionListener luvForm(Poc poc) {
    return ae -> {
      LuvForm luvForm = new LuvForm(poc);
      luvForm.pack();
      luvForm.setVisible(true);
    };
  }

  public ActionListener filterForm(Poc poc) {
    return ae -> {
      FilterForm filterForm = new FilterForm(poc);
      filterForm.pack();
      filterForm.setVisible(true);
    };
  }


  public ActionListener unsharpMaskForm(Poc poc) {
    return ae -> {
      UnsharpMaskForm unsharpMaskForm = new UnsharpMaskForm(poc);
      unsharpMaskForm.pack();
      unsharpMaskForm.setVisible(true);
    };
  }

  public ActionListener gaussianForm(Poc poc) {
    return ae -> {
      GaussianForm gaussianForm = new GaussianForm(poc);
      gaussianForm.pack();
      gaussianForm.setVisible(true);
    };
  }
}
