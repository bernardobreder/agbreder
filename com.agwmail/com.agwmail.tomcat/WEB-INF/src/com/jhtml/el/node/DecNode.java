package com.jhtml.el.node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Decremento
 * 
 * 
 * @author Bernardo Breder
 */
public class DecNode extends UnaryNode {

  /**
   * No de decremento
   * 
   * @param left
   */
  public DecNode(ELNode left) {
    super(left);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object execute(HttpServletRequest req, HttpServletResponse resp) {
    Object value = this.getLeft().execute(req, resp);
    if (value instanceof Double) {
      return ((Double) value) - 1;
    }
    return value;
  }

}
