package com.jhtml.el.node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Negação
 * 
 * 
 * @author Bernardo Breder
 */
public class NegNode extends UnaryNode {

  /**
   * Construtor
   * 
   * @param left
   */
  public NegNode(ELNode left) {
    super(left);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object execute(HttpServletRequest req, HttpServletResponse resp) {
    Object left = this.getLeft().execute(req, resp);
    if (left instanceof Double) {
      return ((Double) left) * -1;
    }
    return left;
  }

}
