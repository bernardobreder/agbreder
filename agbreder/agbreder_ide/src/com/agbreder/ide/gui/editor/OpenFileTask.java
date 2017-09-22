package com.agbreder.ide.gui.editor;

import com.agbreder.ide.gui.destkop.IDesktop;
import com.agbreder.ide.gui.util.task.ReadTask;
import com.agbreder.ide.model.io.IFile;

/**
 * Ação de abrir uma arquivo no editor
 * 
 * 
 * @author bbreder
 */
public class OpenFileTask extends ReadTask {

  /** Arquivo */
  private final IFile file;
  /** Conteudo */
  private String content;

  /**
   * Construtor
   * 
   * @param file
   */
  public OpenFileTask(IFile file) {
    this.file = file;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void action() throws Throwable {
    content = file.getInputString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateUI() {
    IDesktop.DEFAULT.openEditor(file, content);
  }

}
