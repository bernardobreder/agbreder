package com.agbreder.ide.task.ide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import breder.util.swing.StandardDialogs;

import com.agbreder.ide.gui.destkop.IDesktop;
import com.agbreder.ide.gui.util.task.WriteTask;

/**
 * Atualiza a IDE
 * 
 * 
 * @author bbreder
 */
public class UpdateIdeTask extends WriteTask {

  /**
   * {@inheritDoc}
   */
  @Override
  public void action() throws Throwable {
    File file = new File("agbide.jar");
    if (file.exists()) {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      FileInputStream input = new FileInputStream(file);
      for (int n; ((n = input.read()) != -1);) {
        output.write((char) n);
      }
      input.close();
      file.delete();
      FileOutputStream foutput = new FileOutputStream(file);
      foutput.write(output.toByteArray());
      foutput.close();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateUI() {
    StandardDialogs.showInfoDialog(IDesktop.DEFAULT.getFrame(), "Complete",
      "The Update is Completed. \nThe Ide will Close for Restart.");
    IDesktop.DEFAULT.close();
  }

}
