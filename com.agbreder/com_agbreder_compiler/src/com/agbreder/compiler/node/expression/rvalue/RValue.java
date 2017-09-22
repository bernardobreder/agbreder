package com.agbreder.compiler.node.expression.rvalue;

import com.agbreder.compiler.node.expression.Value;
import com.agbreder.compiler.type.RType;
import com.agbreder.compiler.util.RToken;

/**
 * Classe de comandos
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class RValue extends Value {

  /** Tipo de classe */
  private RType type;

  /**
   * @param token
   */
  public RValue(RToken token) {
    super(token);
  }

  /**
   * Retorna
   * 
   * @return type
   */
  public RType getType() {
    return type;
  }

  /**
   * @param type
   */
  public void setType(RType type) {
    this.type = type;
  }

}
