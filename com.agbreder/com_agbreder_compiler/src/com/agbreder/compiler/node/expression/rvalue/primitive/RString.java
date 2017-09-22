package com.agbreder.compiler.node.expression.rvalue.primitive;

import java.io.DataOutputStream;
import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.type.RStringType;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Classe primitiva
 * 
 * 
 * @author Bernardo Breder
 */
public class RString extends RPrimtive {

  /** Indice do Pool */
  private int index;

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public RString(AGBDataInputStream input) throws IOException {
    this(new RToken(input));
    this.setIndex(input.readInt());
  }

  /**
   * @param token
   */
  public RString(RToken token) {
    super(token);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    String value =
      this.getToken().getText().substring(1,
        this.getToken().getText().length() - 1);
    this.index = c.getStrPool().indexOf(value);
    if (index < 0) {
      this.index = c.getStrPool().size();
      c.getStrPool().add(value);
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
    this.setType(RStringType.DEFAULT);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws IOException {
    opcodes.opLoadStr(index);
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
