package com.agbreder.compiler.exception;

import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.util.RToken;

/**
 * Erro de operação indevida
 * 
 * 
 * @author Bernardo Breder
 */
public class RIllegalOperationException extends RException {

  /**
   * Construtor
   * 
   * @param left
   * @param token
   * @param right
   */
  public RIllegalOperationException(RValue left, RToken token, RValue right) {
    super(build(left, token, right));
  }

  /**
   * Constroi a msg
   * 
   * @param left
   * @param token
   * @param right
   * @return msg
   */
  private static String build(RValue left, RToken token, RValue right) {
    return String.format("illegal operator : %s %s %s", left.getType(), token
      .getText(), right.getType());
  }

}
