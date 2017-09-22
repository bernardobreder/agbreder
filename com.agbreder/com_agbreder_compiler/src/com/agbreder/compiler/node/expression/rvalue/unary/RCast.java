package com.agbreder.compiler.node.expression.rvalue.unary;

import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Cast de tipos
 * 
 * 
 * @author Bernardo Breder
 */
public class RCast extends RUnary {

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RCast(AGBDataInputStream input) throws IOException {
    this(new RToken(input), (RValue) input.create(input));
  }

  /**
   * @param token
   * @param left
   */
  public RCast(RToken token, RValue left) {
    super(token, left);
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
  public void build(AGBCompilerOpcode opcodes) throws IOException {
  }

}
