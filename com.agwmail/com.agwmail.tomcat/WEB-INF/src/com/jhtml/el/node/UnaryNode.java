package com.jhtml.el.node;

/**
 * Nó unario
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class UnaryNode extends ExpressionNode {

  /** Valor a esquerda */
  private ELNode left;

  /**
   * Construtor padrão
   * 
   * @param left
   */
  public UnaryNode(ELNode left) {
    super();
    this.left = left;
  }

  /**
   * Retorna
   * 
   * @return left
   */
  public ELNode getLeft() {
    return left;
  }

  /**
   * @param left
   * @return owner
   */
  public UnaryNode setLeft(ELNode left) {
    this.left = left;
    return this;
  }

}
