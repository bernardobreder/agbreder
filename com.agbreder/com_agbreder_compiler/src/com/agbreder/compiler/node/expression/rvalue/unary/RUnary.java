package com.agbreder.compiler.node.expression.rvalue.unary;

import java.io.DataOutputStream;
import java.io.IOException;

import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.util.RToken;

/**
 * Classe primitivas
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class RUnary extends RValue {

  /** Left */
  private RValue left;

  /**
   * @param token
   * @param left
   */
  public RUnary(RToken token, RValue left) {
    super(token);
    this.left = left;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    this.getLeft().head(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws Exception {
    this.getLeft().compile(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws Exception {
    this.getLeft().link(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
  }

  /**
   * Retorna
   * 
   * @return left
   */
  public RValue getLeft() {
    return left;
  }

  /**
   * @param left
   */
  public void setLeft(RValue left) {
    this.left = left;
  }

}
