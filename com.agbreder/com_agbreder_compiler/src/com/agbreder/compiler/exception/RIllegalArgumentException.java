package com.agbreder.compiler.exception;

import com.agbreder.compiler.util.RToken;

/**
 * Erro de operação indevida
 * 
 * 
 * @author Bernardo Breder
 */
public class RIllegalArgumentException extends RException {

  /**
   * Construtor
   * 
   * @param token
   */
  public RIllegalArgumentException(RToken token) {
    super(build(token));
  }

  /**
   * Constroi a msg
   * 
   * @param token
   * @return msg
   */
  private static String build(RToken token) {
    return String.format("illegal argument at line %d and column %d : %s",
      token.getLine(), token.getColumn(), token.getText());
  }

}
