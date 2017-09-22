package com.agbreder.ide.gui.editor;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UIDefaultEditor extends JScrollPane {

  /** Campo */
  private JTextArea field;
  /** Editor */
  private AbstractEditor editor;

  /**
   * Construtor
   * 
   * @param editor
   * @param text
   */
  public UIDefaultEditor(AbstractEditor editor, String text) {
    super(new JTextArea());
    this.editor = editor;
    this.field = (JTextArea) this.getViewport().getView();
    this.field.setText(text);
  }

}
