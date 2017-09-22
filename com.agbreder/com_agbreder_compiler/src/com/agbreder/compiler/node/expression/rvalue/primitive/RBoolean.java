package com.agbreder.compiler.node.expression.rvalue.primitive;

import java.io.DataOutputStream;
import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.type.RBooleanType;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Classe primitiva
 * 
 * 
 * @author Bernardo Breder
 */
public class RBoolean extends RPrimtive {

  /** Valor */
  private boolean value;

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RBoolean(AGBDataInputStream input) throws IOException {
    this(new RToken(input));
    this.setValue(input.readBoolean());
  }

  /**
   * @param token
   */
  public RBoolean(RToken token) {
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
    this.value = this.getToken().getText().equals("true");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) {
    this.setType(RBooleanType.DEFAULT);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws IOException {
    if (value) {
      opcodes.opLoadTrue();
    }
    else {
      opcodes.opLoadFalse();
    }
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
   * @return value
   */
  public boolean isValue() {
    return value;
  }

  /**
   * @param value
   */
  public void setValue(boolean value) {
    this.value = value;
  }

}
