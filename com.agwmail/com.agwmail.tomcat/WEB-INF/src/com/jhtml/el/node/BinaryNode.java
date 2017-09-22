package com.jhtml.el.node;

/**
 * Nó binário
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class BinaryNode extends UnaryNode {

  /** Valor a direita */
  private ELNode right;

  /**
   * Construtor padrão
   * 
   * @param left
   * @param right
   */
  public BinaryNode(ELNode left, ELNode right) {
    super(left);
    this.right = right;
  }

  /**
   * Retorna
   * 
   * @return right
   */
  public ELNode getRight() {
    return right;
  }

  /**
   * @param right
   * @return owner
   */
  public BinaryNode setRight(ELNode right) {
    this.right = right;
    return this;
  }

}
