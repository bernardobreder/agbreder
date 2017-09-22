package com.agbreder.compiler.node.expression.rvalue.array;

import java.io.DataOutputStream;
import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.node.expression.rvalue.unary.RUnary;
import com.agbreder.compiler.type.RNumberType;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Criar um array
 * 
 * 
 * @author Bernardo Breder
 */
public class RArrayLength extends RUnary {

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RArrayLength(AGBDataInputStream input) throws IOException {
    this(new RToken(input), (RValue) input.create(input));
  }

  /**
   * Construtor
   * 
   * @param token
   * @param left
   */
  public RArrayLength(RToken token, RValue left) {
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
    this.setType(RNumberType.DEFAULT);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    this.getLeft().build(opcodes);
    opcodes.opArrayLen();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
  }

}
