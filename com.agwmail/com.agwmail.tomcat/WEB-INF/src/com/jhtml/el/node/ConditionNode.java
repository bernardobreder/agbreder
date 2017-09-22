package com.jhtml.el.node;

/**
 * Condição
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class ConditionNode extends BinaryNode {

  /**
   * Condição
   * 
   * @param left
   * @param right
   */
  public ConditionNode(ELNode left, ELNode right) {
    super(left, right);
  }

}
