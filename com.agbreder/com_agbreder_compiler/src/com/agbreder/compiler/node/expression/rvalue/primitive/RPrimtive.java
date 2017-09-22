package com.agbreder.compiler.node.expression.rvalue.primitive;

import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.util.RToken;

/**
 * Classe primitivas
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class RPrimtive extends RValue {

  /**
   * @param token
   */
  public RPrimtive(RToken token) {
    super(token);
  }

}
