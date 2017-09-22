package com.agbreder.compiler.exception;

import com.agbreder.compiler.util.RToken;

/**
 * Erro de operação indevida
 * 
 * 
 * @author Bernardo Breder
 */
public class RWrongTypeException extends RException {

  /**
   * Construtor
   * 
   * @param token
   */
  public RWrongTypeException(RToken token) {
    super(build(token));
  }

  /**
   * Constroi a msg
   * 
   * @param token
   * @return msg
   */
  private static String build(RToken token) {
    return String.format("type invalied", token.getLine(), token.getColumn(),
      token.getText());
  }

}
