package com.agbreder.compiler.node.expression.rvalue.binary;

import java.io.DataOutputStream;
import java.io.IOException;

import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.node.expression.rvalue.unary.RUnary;
import com.agbreder.compiler.util.RToken;

/**
 * Classe primitivas
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class RBinary extends RUnary {

  /** Left */
  private RValue right;

  /**
   * @param token
   * @param left
   * @param right
   */
  public RBinary(RToken token, RValue left, RValue right) {
    super(token, left);
    this.right = right;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    super.head(c);
    this.getRight().head(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws Exception {
    super.compile(c);
    this.getRight().compile(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws Exception {
    super.link(c);
    this.getRight().link(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
    super.save(output);
  }

  /**
   * Retorna
   * 
   * @return left
   */
  public RValue getRight() {
    return right;
  }

  /**
   * @param right
   */
  public void setRight(RValue right) {
    this.right = right;
  }

}
