package com.agbreder.compiler.node.expression.rvalue.array;

import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.node.expression.rvalue.ternary.RTernary;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Classe primitivas
 * 
 * 
 * @author Bernardo Breder
 */
public class RArraySet extends RTernary {

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RArraySet(AGBDataInputStream input) throws IOException {
    this(new RToken(input), (RValue) input.create(input), (RValue) input
      .create(input), (RValue) input.create(input));
  }

  /**
   * Construtor
   * 
   * @param token
   * @param left
   * @param center
   * @param right
   */
  public RArraySet(RToken token, RValue left, RValue center, RValue right) {
    super(token, left, center, right);
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
    this.getRight().build(opcodes);
    opcodes.opStackDup(0);
    this.getLeft().build(opcodes);
    this.getCenter().build(opcodes);
    opcodes.opArraySet();
  }

}
