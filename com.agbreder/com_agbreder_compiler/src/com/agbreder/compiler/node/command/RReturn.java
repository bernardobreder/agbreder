package com.agbreder.compiler.node.command;

import java.io.DataOutputStream;
import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.util.RToken;

/**
 * Condição
 * 
 * 
 * @author Bernardo Breder
 */
public class RReturn extends RCommand {

  /** Token */
  private final RToken token;
  /** Condição */
  private final RValue left;
  /** Indice */
  private int index;
  /** Indice */
  private int pops;

  /**
   * @param token
   * @param left
   */
  public RReturn(RToken token, RValue left) {
    super();
    this.token = token;
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
    index = 1 + c.getLastBlock().getVariables().size();
    RMethod method = c.getLastMethod();
    if (method != null) {
      pops = method.getParams().size() + method.getVariables().size();
      index += method.getParams().size();
    }
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
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    this.getLeft().build(opcodes);
    opcodes.opStackStoreAbs(index);
    opcodes.opJumpReturn(pops);
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
   * @return condition
   */
  public RValue getLeft() {
    return left;
  }

  /**
   * Retorna
   * 
   * @return token
   */
  public RToken getToken() {
    return token;
  }

}
