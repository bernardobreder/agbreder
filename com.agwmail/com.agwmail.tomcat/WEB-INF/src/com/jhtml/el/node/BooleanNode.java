package com.jhtml.el.node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Literal Boolean
 * 
 * 
 * @author Bernardo Breder
 */
public class BooleanNode extends PrimitiveNode {

  /**
   * Construtor
   * 
   * @param token
   */
  public BooleanNode(String token) {
    super(token);
  }

  /**
   * Flag
   * 
   * @return flag
   */
  public Boolean getFlag() {
    if (this.getToken().equals("true")) {
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object execute(HttpServletRequest req, HttpServletResponse resp) {
    return this.getFlag();
  }

}
