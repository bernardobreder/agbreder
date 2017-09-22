package com.jhtml.el.node;

/**
 * No primitivo
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class PrimitiveNode extends ExpressionNode {

  /** Token */
  private String token;

  /**
   * Construtor
   * 
   * @param token
   */
  public PrimitiveNode(String token) {
    this.token = token;
  }

  /**
   * Retorna
   * 
   * @return token
   */
  public String getToken() {
    return token;
  }

}
