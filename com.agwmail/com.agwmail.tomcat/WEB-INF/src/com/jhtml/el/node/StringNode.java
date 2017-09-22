package com.jhtml.el.node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Literal de string
 * 
 * 
 * @author Bernardo Breder
 */
public class StringNode extends PrimitiveNode {

  /**
   * Construtor
   * 
   * @param token
   */
  public StringNode(String token) {
    super(token);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object execute(HttpServletRequest req, HttpServletResponse resp) {
    return this.getToken();
  }

}
