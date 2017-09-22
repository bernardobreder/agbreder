package com.agbreder.compiler.node.expression.lvalue;

import com.agbreder.compiler.node.expression.Value;
import com.agbreder.compiler.util.RToken;

/**
 * Classe de comandos
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class LValue extends Value {

  /**
   * @param token
   */
  public LValue(RToken token) {
    super(token);
  }

}
