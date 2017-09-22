package com.agbreder.compiler.node.expression;

import com.agbreder.compiler.node.RNode;
import com.agbreder.compiler.util.RToken;

/**
 * Classe de comandos
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class Value extends RNode {

  /** Token */
  private final RToken token;

  /**
   * @param token
   */
  public Value(RToken token) {
    super();
    this.token = token;
  }

  /**
   * Retorna
   * 
   * @return token
   */
  public RToken getToken() {
    return token;
  }

}
