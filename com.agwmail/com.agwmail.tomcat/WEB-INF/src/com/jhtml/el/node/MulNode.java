package com.jhtml.el.node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Multiplicação
 * 
 * 
 * @author Bernardo Breder
 */
public class MulNode extends ArithmeticNode {

  /**
   * Construtor
   * 
   * @param left
   * @param right
   */
  public MulNode(ELNode left, ELNode right) {
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
      return ((Double) left) * ((Double) right);
    }
    return left;
  }

}
