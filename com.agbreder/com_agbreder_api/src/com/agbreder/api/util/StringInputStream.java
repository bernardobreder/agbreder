package com.agbreder.api.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Array de bytes
 * 
 * 
 * @author Bernardo Breder
 */
public class StringInputStream extends InputStream implements Serializable {

  /** Conteudo */
  private String content;
  /** Posição */
  private int index;

  /**
   * @param content
   */
  public StringInputStream(String content) {
    this.content = content;
    this.index = 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int read() throws IOException {
    if (this.index >= this.content.length()) {
      return -1;
    }
    return this.content.charAt(this.index++);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int available() throws IOException {
    return this.content.length() - this.index;
  }

}
