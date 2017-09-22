package com.agbreder.compiler.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Saida que apreita para contar
 * 
 * 
 * @author Bernardo Breder
 */
public class ByteFixOutputStream extends OutputStream {

  /** Contador */
  private final byte[] bytes;
  /** Posição */
  private int index = 0;

  /**
   * Construtor
   * 
   * @param bytes
   */
  public ByteFixOutputStream(int bytes) {
    this.bytes = new byte[bytes];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(int b) throws IOException {
    bytes[index++] = (byte) b;
  }

  /**
   * Retorna
   * 
   * @return bytes
   */
  public byte[] getBytes() {
    return bytes;
  }

}