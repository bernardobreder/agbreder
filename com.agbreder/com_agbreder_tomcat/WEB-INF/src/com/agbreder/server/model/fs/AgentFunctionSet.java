package com.agbreder.server.model.fs;

/**
 * Pasta especial
 * 
 * 
 * @author Bernardo Breder
 */
public class AgentFunctionSet extends AgentFolder {

  /**
   * Construtor
   * 
   * @param parent
   */
  public AgentFunctionSet(AgentFolder parent) {
    super(2, parent, "function");
    this.children.add(new AgentFunction(10, this, "getComputerName"));
    this.children.add(new AgentFunction(11, this, "getPersonName"));
    this.children.add(new AgentFunction(12, this, "getAge"));
  }

  /**
   * Retorna a imagem
   * 
   * @return image
   */
  @Override
  public String getImage() {
    return "functions.png";
  }

}
