package com.jhtml.el.node;

/**
 * Operação Arithmetic
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class ArithmeticNode extends BinaryNode {

  /**
   * Construtor
   * 
   * @param left
   * @param right
   */
  public ArithmeticNode(ELNode left, ELNode right) {
    super(left, right);
  }

}
