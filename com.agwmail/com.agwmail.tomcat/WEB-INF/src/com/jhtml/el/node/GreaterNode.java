package com.jhtml.el.node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Maior que
 * 
 * 
 * @author Bernardo Breder
 */
public class GreaterNode extends ConditionNode {

  /**
   * Construtor
   * 
   * @param left
   * @param right
   */
  public GreaterNode(ELNode left, ELNode right) {
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
      return Double.valueOf(left.toString()).compareTo(
        Double.valueOf(right.toString())) > 0;
    }
    if (left instanceof Comparable<?> && right instanceof Comparable<?>) {
      @SuppressWarnings("unchecked")
      Comparable<Object> lc = (Comparable<Object>) left;
      return lc.compareTo(right) > 0;
    }
    return false;
  }

}
