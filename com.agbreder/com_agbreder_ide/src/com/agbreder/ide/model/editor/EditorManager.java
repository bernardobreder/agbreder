package com.agbreder.ide.model.editor;

import java.util.HashMap;
import java.util.Map;

import com.agbreder.ide.gui.editor.AbstractEditor;
import com.agbreder.ide.gui.editor.UIDefaultEditor;
import com.agbreder.ide.gui.editor.UIEditorManager;
import com.agbreder.ide.gui.ide.IdeFrame;
import com.agbreder.ide.model.io.IFile;
import com.agbreder.ide.plugin.IEditorManager;

import breder.util.util.input.InputStreamUtil;

/**
 * Implementação padrão de gerente de editor
 * 
 * 
 * @author Bernardo Breder
 */
public class EditorManager implements IEditorManager {

  /** Instancia unica */
  public static final EditorManager DEFAULT = new EditorManager();
  /** Bytes */
  private Map<String, byte[]> bytes = new HashMap<String, byte[]>();

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void open(IFile file) throws Exception {
    AbstractEditor editor =
      UIEditorManager.getInstance().getEditor(file.getName());
    if (editor != null) {
      byte[] bytes = InputStreamUtil.getBytes(file.getInputStream());
      this.bytes.put(file.getAbsoluteName(), bytes);
      UIDefaultEditor editorui =
        new UIDefaultEditor(editor, new String(bytes, "utf-8"));
      IdeFrame.getInstance().addEditor(file.getName(), editorui);
    }
  }

}
