package com.jhtml.el.node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Incremento
 * 
 * 
 * @author Bernardo Breder
 */
public class IncNode extends UnaryNode {

  /**
   * Construtor
   * 
   * @param left
   */
  public IncNode(ELNode left) {
    super(left);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object execute(HttpServletRequest req, HttpServletResponse resp) {
    Object value = this.getLeft().execute(req, resp);
    if (value instanceof Double) {
      return ((Double) value) + 1;
    }
    return value;
  }

}
