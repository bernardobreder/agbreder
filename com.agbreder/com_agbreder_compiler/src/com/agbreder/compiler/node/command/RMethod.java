package com.agbreder.compiler.node.command;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.agbreder.compiler.AGBCompilerOpcode;
import com.agbreder.compiler.node.ContextNode;
import com.agbreder.compiler.node.RVariableNode;
import com.agbreder.compiler.type.RType;
import com.agbreder.compiler.util.AGBDataInputStream;
import com.agbreder.compiler.util.RToken;

/**
 * Condição
 * 
 * 
 * @author Bernardo Breder
 */
public class RMethod extends RCommand {

  /** address */
  private int address = -1;
  /** Tipo de retorno */
  private RType type;
  /** Nome do Método */
  private RToken name;
  /** Parametros */
  private final List<RVariableNode> params = new ArrayList<RVariableNode>();
  /** Parametros */
  private final List<RVariableNode> variables = new ArrayList<RVariableNode>();
  /** Comando */
  private RCommand command;

  /**
   * Construtor
   * 
   * @param input
   */
  public RMethod(AGBDataInputStream input) {
  }

  /**
   * Construtor
   */
  public RMethod() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void head(ContextNode c) throws Exception {
    c.getMethods().add(this);
    c.pushMethod(this);
    this.getCommand().head(c);
    c.popMethod();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void compile(ContextNode c) throws Exception {
    c.pushMethod(this);
    this.getCommand().compile(c);
    c.popMethod();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void link(ContextNode c) throws Exception {
    c.pushMethod(this);
    this.getCommand().link(c);
    c.popMethod();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void build(AGBCompilerOpcode opcodes) throws Exception {
    if (this.getAddress() < 0) {
      this.setAddress(opcodes.getPc());
    }
    this.getCommand().build(opcodes);
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
  public void addParam(RVariableNode v) {
    this.params.add(v);
  }

  /**
   * Adiciona um parametro
   * 
   * @param v
   */
  public void addVariable(RVariableNode v) {
    v.setIndex(this.variables.size());
    this.variables.add(v);
  }

  /**
   * Indica que é um comando de cabeçalho
   * 
   * @return cabeçalho
   */
  @Override
  public boolean isHeader() {
    return true;
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
   * @return command
   */
  public RCommand getCommand() {
    return command;
  }

  /**
   * @param command
   */
  public void setCommand(RCommand command) {
    this.command = command;
  }

  /**
   * Retorna
   * 
   * @return params
   */
  public List<RVariableNode> getParams() {
    return params;
  }

  /**
   * Retorna
   * 
   * @return variables
   */
  public List<RVariableNode> getVariables() {
    return variables;
  }

  /**
   * Retorna
   * 
   * @return address
   */
  public int getAddress() {
    return address;
  }

  /**
   * @param address
   */
  public void setAddress(int address) {
    this.address = address;
  }

}
