package com.agbreder.compiler.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.agbreder.compiler.grammer.Token;
import com.agbreder.compiler.node.RStruct;

/**
 * Token da linguagem
 * 
 * 
 * @author Bernardo Breder
 */
public class RToken extends RStruct {

  /** Conteudo */
  private final String text;
  /** Linha */
  private final int line;
  /** Coluna */
  private final int column;

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RToken(DataInputStream input) throws IOException {
    this.text = input.readUTF();
    this.line = input.readInt();
    this.column = input.readInt();
  }

  /**
   * Construtor
   * 
   * @param image
   */
  public RToken(String image) {
    this(image, -1, -1);
  }

  /**
   * Construtor
   * 
   * @param t
   */
  public RToken(Token t) {
    this(t.image, t.beginLine, t.beginColumn);
  }

  /**
   * Construtor
   * 
   * @param t1
   * @param t2
   */
  public RToken(Token t1, Token t2) {
    this(t1.image + t2.image, t1.beginLine, t1.beginColumn);
  }

  /**
   * Construtor
   * 
   * @param text
   * @param line
   * @param column
   */
  public RToken(String text, int line, int column) {
    super();
    this.text = text;
    this.line = line;
    this.column = column;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
    output.writeInt(AGBDataInputStream.TOKEN_ID);
    output.writeUTF(this.getText());
    output.writeInt(this.getLine());
    output.writeInt(this.getColumn());
  }

  /**
   * Retorna
   * 
   * @return image
   */
  public String getText() {
    return text;
  }

  /**
   * Retorna
   * 
   * @return line
   */
  public int getLine() {
    return line;
  }

  /**
   * Retorna
   * 
   * @return column
   */
  public int getColumn() {
    return column;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + column;
    result = prime * result + ((text == null) ? 0 : text.hashCode());
    result = prime * result + line;
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    RToken other = (RToken) obj;
    if (column != other.column) {
      return false;
    }
    if (text == null) {
      if (other.text != null) {
        return false;
      }
    }
    else if (!text.equals(other.text)) {
      return false;
    }
    if (line != other.line) {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.getText();
  }

}
