package com.jhtml.el.node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Somador
 * 
 * 
 * @author Bernardo Breder
 */
public class SumNode extends ArithmeticNode {

  /**
   * Construtor
   * 
   * @param left
   * @param right
   */
  public SumNode(ELNode left, ELNode right) {
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
      return ((Double) left) + ((Double) right);
    }
    else {
      return left.toString() + right.toString();
    }
  }

}
