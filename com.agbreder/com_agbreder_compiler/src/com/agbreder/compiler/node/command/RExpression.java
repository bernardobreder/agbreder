package com.agbreder.compiler.node.command;

import java.io.DataOutputStream;
import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.expression.rvalue.RValue;
import com.agbreder.compiler.util.AGBDataInputStream;

/**
 * Comando de expressão
 * 
 * 
 * @author Bernardo Breder
 */
public class RExpression extends RCommand {

  /** Valor */
  private RValue value;

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RExpression(AGBDataInputStream input) throws IOException {
    this((RValue) input.create(input));
  }

  /**
   * @param value
   */
  public RExpression(RValue value) {
    super();
    this.value = value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    this.getValue().head(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws Exception {
    this.getValue().compile(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws Exception {
    this.getValue().link(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    this.getValue().build(opcodes);
    opcodes.opStackPop(1);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
    output.writeInt(AGBDataInputStream.EXPRESSION_ID);
    this.getValue().save(output);
  }

  /**
   * Retorna
   * 
   * @return value
   */
  public RValue getValue() {
    return value;
  }

  /**
   * @param value
   */
  public void setValue(RValue value) {
    this.value = value;
  }

}
