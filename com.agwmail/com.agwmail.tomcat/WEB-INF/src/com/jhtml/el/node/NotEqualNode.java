package com.jhtml.el.node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Nao igual
 * 
 * 
 * @author Bernardo Breder
 */
public class NotEqualNode extends ConditionNode {

  /**
   * Construtor
   * 
   * @param left
   * @param right
   */
  public NotEqualNode(ELNode left, ELNode right) {
    super(left, right);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object execute(HttpServletRequest req, HttpServletResponse resp) {
    Object left = this.getLeft().execute(req, resp);
    Object right = this.getRight().execute(req, resp);
    if (left instanceof Number && right instanceof Number) {
      return !Double.valueOf(left.toString()).equals(
        Double.valueOf(right.toString()));
    }
    else {
      return !left.equals(right);
    }
  }

}
