package com.agbreder.compiler.node.expression.rvalue.binary;

import java.io.DataOutputStream;
import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.lvalue.LValue;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Classe primitivas
 * 
 * 
 * @author Bernardo Breder
 */
public class RAssign extends RValue {

  /** Left Value */
  private LValue left;
  /** Right Value */
  private RValue right;

  /**
   * Construtor
   * 
   * @param input
   */
  public RAssign(AGBDataInputStream input) {
    this(null, null, null);
  }

  /**
   * @param token
   * @param left
   * @param right
   */
  public RAssign(RToken token, LValue left, RValue right) {
    super(token);
    this.left = left;
    this.right = right;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    this.getLeft().head(c);
    this.getRight().head(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws Exception {
    this.getLeft().compile(c);
    this.getRight().compile(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws Exception {
    this.getLeft().link(c);
    this.getRight().link(c);
    this.setType(this.getRight().getType());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    this.getRight().build(opcodes);
    opcodes.opStackDup(0);
    this.getLeft().build(opcodes);
  }

  /**
   * Retorna
   * 
   * @return left
   */
  public LValue getLeft() {
    return left;
  }

  /**
   * @param left
   */
  public void setLeft(LValue left) {
    this.left = left;
  }

  /**
   * Retorna
   * 
   * @return right
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
  }

}
