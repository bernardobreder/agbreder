package com.agbreder.compiler.node.expression.rvalue.primitive;

import java.io.DataOutputStream;
import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Classe primitiva
 * 
 * 
 * @author Bernardo Breder
 */
public class RArray extends RPrimtive {

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RArray(AGBDataInputStream input) throws IOException {
    this(new RToken(input));
  }

  /**
   * @param token
   */
  public RArray(RToken token) {
    super(token);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
  }

}
