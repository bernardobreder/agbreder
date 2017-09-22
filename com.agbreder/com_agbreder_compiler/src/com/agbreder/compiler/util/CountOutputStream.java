package com.agbreder.compiler.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Saida que apreita para contar
 * 
 * 
 * @author Bernardo Breder
 */
public class CountOutputStream extends OutputStream {

  /** Contador */
  private int length = 0;

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(int b) throws IOException {
    length++;
  }

  /**
   * Retorna
   * 
   * @return count
   */
  public int getLength() {
    return length;
  }

}
