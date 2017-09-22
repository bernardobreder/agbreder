package com.agbreder.compiler.node.command;

import com.agbreder.compiler.node.RNode;

/**
 * Classe de comandos
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class RCommand extends RNode {

  /**
   * Indica que é um comando de cabeçalho
   * 
   * @return cabeçalho
   */
  public boolean isHeader() {
    return false;
  }

}
