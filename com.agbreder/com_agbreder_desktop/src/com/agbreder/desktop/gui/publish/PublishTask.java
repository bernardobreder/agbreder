package com.agbreder.desktop.gui.publish;

import java.io.File;
import java.io.FileInputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import breder.util.swing.StandardDialogs;
import breder.util.task.simple.SimpleRemoteTask;

import com.agbreder.desktop.model.AGBrederLocator;

/**
 * Tarefa de publicar
 * 
 * 
 * @author Bernardo Breder
 */
public class PublishTask extends SimpleRemoteTask {

  /** Arquivo selecionado */
  private File file;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean accept() {
    JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.setMultiSelectionEnabled(false);
    chooser
      .setFileFilter(new FileNameExtensionFilter("AGBreder Source", "agb"));
    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      file = chooser.getSelectedFile();
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void perform() throws Throwable {
    AGBrederLocator.instance.publish("main.agb", new FileInputStream(file));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateUI() {
    StandardDialogs.showInfoDialog(null, "Publish", "Publish completed!");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handler(Throwable t) {
    StandardDialogs.showErrorDialog(null, "Publish",
      "Same error in the Publish");
    super.handler(t);
  }

}
