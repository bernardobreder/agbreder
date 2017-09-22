package com.agbreder.ide.gui.editor.agb;

import java.awt.Color;

import com.agbreder.ide.gui.editor.AbstractEditor;

/**
 * Editor
 * 
 * @author Bernardo Breder
 */
public class AGBEditor extends AbstractEditor {

  /**
   * Construtor padrão
   */
  public AGBEditor() {
    Color blue = Color.BLUE;
    this.addWord("class", blue);
    this.addWord("do", blue);
    this.addWord("end", blue);
    this.addWord("if", blue);
    this.addWord("else", blue);
    this.addWord("for", blue);
    this.addWord("return", blue);
    this.addWord("switch", blue);
    this.addWord("while", blue);
    this.addWord("new", blue);
    this.addWord("or", blue);
    this.addWord("and", blue);
    this.addWord("obj", blue);
    this.addWord("str", blue);
    this.addWord("bool", blue);
    this.addWord("num", blue);
    this.addWord("this", blue);
    this.addWord("super", blue);
    this.addWord("static", blue);
    this.addWord("false", blue);
    this.addWord("true", blue);
    this.addWord("null", blue);
  }

}
