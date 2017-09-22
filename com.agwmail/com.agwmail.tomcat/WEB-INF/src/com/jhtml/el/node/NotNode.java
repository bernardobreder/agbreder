package com.jhtml.el.node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Not
 * 
 * 
 * @author Bernardo Breder
 */
public class NotNode extends UnaryNode {

  /**
   * Construtor
   * 
   * @param left
   */
  public NotNode(ELNode left) {
    super(left);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object execute(HttpServletRequest req, HttpServletResponse resp) {
    Object left = this.getLeft().execute(req, resp);
    if (left instanceof Boolean) {
      return !((Boolean) left);
    }
    return left;
  }

}
