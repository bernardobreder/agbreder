package com.jhtml.el.node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Numero
 * 
 * 
 * @author Bernardo Breder
 */
public class NumberNode extends PrimitiveNode {

  /**
   * Construtor
   * 
   * @param token
   */
  public NumberNode(String token) {
    super(token);
  }

  /**
   * Valor
   * 
   * @return double
   */
  public Double getDouble() {
    return new Double(this.getToken());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object execute(HttpServletRequest req, HttpServletResponse resp) {
    return this.getDouble();
  }

}
