package com.agbreder.compiler.node.expression.rvalue.array;

import java.io.DataOutputStream;
import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.node.expression.rvalue.binary.RBinary;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Classe primitivas
 * 
 * 
 * @author Bernardo Breder
 */
public class RArrayGet extends RBinary {

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RArrayGet(AGBDataInputStream input) throws IOException {
    this(new RToken(input), (RValue) input.create(input), (RValue) input
      .create(input));
  }

  /**
   * @param token
   * @param left
   * @param right
   */
  public RArrayGet(RToken token, RValue left, RValue right) {
    super(token, left, right);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    super.head(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws Exception {
    super.compile(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws Exception {
    super.link(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    this.getLeft().build(opcodes);
    this.getRight().build(opcodes);
    opcodes.opArrayGet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
  }

}
