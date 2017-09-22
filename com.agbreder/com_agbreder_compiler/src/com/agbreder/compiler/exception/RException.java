package com.agbreder.compiler.exception;

/**
 * Erro da linguagem Root
 * 
 * 
 * @author Bernardo Breder
 */
public class RException extends Exception {

  /**
   * @param message
   * @param cause
   */
  public RException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   */
  public RException(String message) {
    super(message);
  }

}
