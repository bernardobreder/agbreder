package com.agbreder.compiler.node.expression.rvalue.primitive;

import java.io.DataOutputStream;
import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.type.RNumberType;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Classe primitiva
 * 
 * 
 * @author Bernardo Breder
 */
public class RNumber extends RPrimtive {

  /** Indice do Pool */
  private int index;

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RNumber(AGBDataInputStream input) throws IOException {
    this(new RToken(input));
    this.setIndex(input.readInt());
  }

  /**
   * @param token
   */
  public RNumber(RToken token) {
    super(token);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    Double value = new Double(this.getToken().getText());
    this.index = c.getNumPool().indexOf(value);
    if (index < 0) {
      this.index = c.getNumPool().size();
      c.getNumPool().add(value);
    }
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
    this.setType(RNumberType.DEFAULT);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws IOException
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws IOException {
    opcodes.opLoadNum(index);
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
   * @return index
   */
  public int getIndex() {
    return index;
  }

  /**
   * @param index
   */
  public void setIndex(int index) {
    this.index = index;
  }

}
