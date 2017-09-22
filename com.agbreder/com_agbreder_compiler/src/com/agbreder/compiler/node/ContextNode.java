package com.agbreder.compiler.node;

import java.util.ArrayList;
import java.util.List;

import com.agbreder.compiler.node.command.RBlock;
import com.agbreder.compiler.node.command.RMethod;

/**
 * Nó de contexto
 * 
 * 
 * @author Bernardo Breder
 */
public class ContextNode {

  /** Pool de primitivo */
  private final List<Double> numPool = new ArrayList<Double>();
  /** Pool de primitivo */
  private final List<String> strPool = new ArrayList<String>();
  /** Pilha de variáveis */
  private final List<RVariableNode> variables = new ArrayList<RVariableNode>();
  /** Pilha de variáveis */
  private final List<RBlock> blocks = new ArrayList<RBlock>();
  /** Metodos */
  private final List<RMethod> methods = new ArrayList<RMethod>();
  /** Metodos */
  private final List<RMethod> stackMethods = new ArrayList<RMethod>();

  /**
   * Retorna
   * 
   * @return numPool
   */
  public List<Double> getNumPool() {
    return numPool;
  }

  /**
   * Retorna
   * 
   * @return strPool
   */
  public List<String> getStrPool() {
    return strPool;
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
   * @return blocks
   */
  public List<RBlock> getBlocks() {
    return blocks;
  }

  /**
   * Retorna
   * 
   * @return blocks
   */
  public RBlock getLastBlock() {
    return this.blocks.get(this.blocks.size() - 1);
  }

  /**
   * Retorna
   * 
   * @return blocks
   */
  public RMethod getLastMethod() {
    if (this.stackMethods.size() == 0) {
      return null;
    }
    return this.stackMethods.get(this.stackMethods.size() - 1);
  }

  /**
   * Retorna
   * 
   * @return methods
   */
  public List<RMethod> getMethods() {
    return methods;
  }

  /**
   * Empilha um método em contexto
   * 
   * @param method
   */
  public void pushMethod(RMethod method) {
    this.stackMethods.add(method);
  }

  /**
   * Desempilha um método em contexto
   */
  public void popMethod() {
    this.stackMethods.remove(stackMethods.size() - 1);
  }

}
