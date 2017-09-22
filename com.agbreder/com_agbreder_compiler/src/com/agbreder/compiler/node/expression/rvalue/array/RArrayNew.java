package com.agbreder.compiler.node.expression.rvalue.array;

import java.io.DataOutputStream;
import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.exception.RIllegalArgumentException;
import com.agbreder.compiler.exception.RNotImplementedException;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Criar um array
 * 
 * 
 * @author Bernardo Breder
 */
public class RArrayNew extends RValue {

  /** Token */
  private final RToken classToken;
  /** Token */
  private final RValue count;

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RArrayNew(AGBDataInputStream input) throws IOException {
    this(new RToken(input), new RToken(input), (RValue) input.create(input));
  }

  /**
   * Construtor
   * 
   * @param token
   * @param classToken
   * @param count
   */
  public RArrayNew(RToken token, RToken classToken, RValue count) {
    super(token);
    this.classToken = classToken;
    this.count = count;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    this.getCount().head(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws Exception {
    this.getCount().compile(c);
    if (!this.getCount().getType().isNumber()) {
      throw new RIllegalArgumentException(this.getCount().getToken());
    }
    if (!classToken.getText().equals("obj")) {
      throw new RNotImplementedException();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws Exception {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    this.getCount().build(opcodes);
    opcodes.opArrayNew();
  }

  /**
   * Retorna
   * 
   * @return classToken
   */
  public RToken getClassToken() {
    return classToken;
  }

  /**
   * Retorna
   * 
   * @return countToken
   */
  public RValue getCount() {
    return count;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
  }
}
