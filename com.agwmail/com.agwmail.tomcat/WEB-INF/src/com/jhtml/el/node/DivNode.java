package com.jhtml.el.node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Divisão
 * 
 * 
 * @author Bernardo Breder
 */
public class DivNode extends ArithmeticNode {

  /**
   * Construtor
   * 
   * @param left
   * @param right
   */
  public DivNode(ELNode left, ELNode right) {
    super(left, right);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object execute(HttpServletRequest req, HttpServletResponse resp) {
    Object left = this.getLeft().execute(req, resp);
    Object right = this.getRight().execute(req, resp);
    if (left instanceof Double && right instanceof Double) {
      return ((Double) left) / ((Double) right);
    }
    return left;
  }

}
