package com.agbreder.compiler.node.expression.rvalue.unary;

import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.type.RType;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Classe primitivas
 * 
 * 
 * @author Bernardo Breder
 */
public class RLeftCall extends RUnary {

  /** Tipo a ser convertido */
  private RType type;

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RLeftCall(AGBDataInputStream input) throws IOException {
    this(new RToken(input), (RValue) input.create(input), (RType) input
      .create(input));
  }

  /**
   * @param token
   * @param left
   * @param type
   */
  public RLeftCall(RToken token, RValue left, RType type) {
    super(token, left);
    this.type = type;
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
  public void build(AGBCompilerOpcode opcodes) {
  }

  /**
   * Retorna
   * 
   * @return type
   */
  @Override
  public RType getType() {
    return type;
  }

  /**
   * @param type
   */
  @Override
  public void setType(RType type) {
    this.type = type;
  }

}
