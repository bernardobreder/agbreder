package com.agbreder.compiler.node;

import java.io.DataOutputStream;
import java.io.IOException;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.type.RType;
import com.agbreder.compiler.util.RToken;

/**
 * Classe de uma variável
 * 
 * 
 * @author Bernardo Breder
 */
public class RVariableNode extends RNode {

  /** Tipo da variavel */
  private RType type;
  /** Nome da variavel */
  private RToken name;
  /** Indice do método */
  private int index;

  /**
   * Construtor
   * 
   * @param type
   * @param token
   */
  public RVariableNode(RType type, RToken token) {
    super();
    this.type = type;
    this.name = token;
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
  public void compile(ContextNode c) throws Exception {
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
  public void build(AGBCompilerOpcode opcodes) throws IOException {
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
   * @return type
   */
  public RType getType() {
    return type;
  }

  /**
   * @param type
   */
  public void setType(RType type) {
    this.type = type;
  }

  /**
   * Retorna
   * 
   * @return name
   */
  public RToken getName() {
    return name;
  }

  /**
   * @param name
   */
  public void setName(RToken name) {
    this.name = name;
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

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "VariableNode [type=" + type + ", name=" + name + "]";
  }

}
