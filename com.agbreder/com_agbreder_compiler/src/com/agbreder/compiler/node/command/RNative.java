package com.agbreder.compiler.node.command;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Condição
 * 
 * 
 * @author Bernardo Breder
 */
public class RNative extends RCommand {

  /** Token */
  private RToken leftToken;
  /** Token */
  private RToken rightToken;
  /** Codigo */
  private int leftCode;
  /** Codigo */
  private int rightCode;
  /** Parametros */
  private final List<RValue> params = new ArrayList<RValue>();

  /**
   * Construtor
   * 
   * @param input
   */
  public RNative(AGBDataInputStream input) {
    this(null, null);
  }

  /**
   * @param leftToken
   * @param rightToken
   */
  public RNative(RToken leftToken, RToken rightToken) {
    super();
    this.leftToken = leftToken;
    this.rightToken = rightToken;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    this.leftCode = Integer.valueOf(leftToken.getText());
    this.rightCode = Integer.valueOf(rightToken.getText());
    for (RValue param : params) {
      param.head(c);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws Exception {
    for (RValue param : params) {
      param.compile(c);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws Exception {
    for (RValue param : params) {
      param.link(c);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    for (RValue param : params) {
      param.build(opcodes);
    }
    opcodes.opUser(leftCode, rightCode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
  }

  /**
   * Adiciona um parametro
   * 
   * @param v
   */
  public void addParam(RValue v) {
    this.params.add(v);
  }

}
