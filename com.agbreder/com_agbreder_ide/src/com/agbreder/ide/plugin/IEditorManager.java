package com.agbreder.ide.plugin;

import com.agbreder.ide.model.editor.EditorManager;
import com.agbreder.ide.model.io.IFile;

/**
 * Gerente de Editor
 * 
 * 
 * @author Bernardo Breder
 */
public interface IEditorManager {

  /** Gerente default */
  public static final IEditorManager DEFAULT = EditorManager.DEFAULT;

  /**
   * Abre um arquivo
   * 
   * @param file
   */
  public void open(IFile file) throws Exception;

}
