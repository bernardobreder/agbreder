package com.agbreder.ide.gui.editor;

import java.util.HashMap;
import java.util.Map;

/**
 * Gerenciador de Editor
 * 
 * 
 * @author Bernardo Breder
 */
public class UIEditorManager {

  /** Instancia única */
  private static final UIEditorManager instance = new UIEditorManager();
  /** Editores */
  private Map<String, AbstractEditor> editors =
    new HashMap<String, AbstractEditor>();

  /**
   * Construtor
   */
  private UIEditorManager() {
  }

  /**
   * Adiciona um editor
   * 
   * @param extension
   * @param editor
   */
  public void addEditor(String extension, AbstractEditor editor) {
    if (!extension.startsWith(".")) {
      extension = "." + extension;
    }
    this.editors.put(extension, editor);
  }

  /**
   * Retorna o editor baseado no nome do arquivo
   * 
   * @param filename
   * @return editor
   */
  public AbstractEditor getEditor(String filename) {
    for (String key : this.editors.keySet()) {
      if (filename.endsWith(key)) {
        return this.editors.get(key);
      }
    }
    return null;
  }

  /**
   * Retorna
   * 
   * @return instance
   */
  public static UIEditorManager getInstance() {
    return instance;
  }

}
