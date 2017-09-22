package com.agbreder.ide.gui.builder.agb.project;

import breder.util.swing.BFrame;

import com.agbreder.ide.gui.util.task.WriteTask;
import com.agbreder.ide.model.io.IFileSystem;
import com.agbreder.ide.model.io.IFolder;

/**
 * Ação de criar uma classe
 * 
 * @author bernardobreder
 */
public class AGBProjectBuilderAction extends WriteTask {

  /** Projeto */
  private final String projectName;

  /** Janela */
  private final BFrame frame;

  /**
   * Construtor
   * 
   * @param frame
   * @param projectName
   */
  public AGBProjectBuilderAction(BFrame frame, String projectName) {
    this.frame = frame;
    this.projectName = projectName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void action() throws Throwable {
    IFolder folder = IFileSystem.DEFAULT.createFolder(projectName);
    folder.createFolder("src");
    folder.createFolder("bin");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateUI() {
    frame.close();
  }

}
