package com.agbreder.ide.gui.builder.agb.folder;

import java.io.File;

import breder.util.swing.BFrame;

import com.agbreder.ide.gui.util.task.WriteTask;
import com.agbreder.ide.model.io.IFolder;

/**
 * Ação de criar uma classe
 * 
 * @author bernardobreder
 */
public class AGBFolderBuilderAction extends WriteTask {

  /** Janela */
  private final BFrame frame;
  /** Classe */
  private final String packageName;
  /** Source */
  private final IFolder sourceFolder;

  /**
   * Construtor
   * 
   * @param frame
   * @param sourceFolder
   * @param packageName
   */
  public AGBFolderBuilderAction(BFrame frame, IFolder sourceFolder,
    String packageName) {
    this.frame = frame;
    this.sourceFolder = sourceFolder;
    this.packageName = packageName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void action() throws Throwable {
    String packageAux =
      packageName.replace('.', File.separatorChar).replace('/',
        File.separatorChar).replace('\\', File.separatorChar);
    IFolder packageFolder = sourceFolder.getFolder(packageAux);
    packageFolder.mkdirs();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateUI() {
    frame.close();
  }

}
