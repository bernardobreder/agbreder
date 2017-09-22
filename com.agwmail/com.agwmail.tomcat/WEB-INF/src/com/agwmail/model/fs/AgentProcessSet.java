package com.agwmail.model.fs;

/**
 * Pasta especial
 * 
 * 
 * @author Bernardo Breder
 */
public class AgentProcessSet extends AgentFolder {

  /**
   * Construtor
   * 
   * @param parent
   */
  public AgentProcessSet(AgentFolder parent) {
    super(3, parent, "process");
    this.children.add(new AgentProcess(4, this, "Calcular Triangulos"));
    this.children.add(new AgentProcess(5, this, "Comprando Mercadoria"));
    this.children.add(new AgentProcess(6, this, "Buscar Vendedor"));
  }

  /**
   * Retorna a imagem
   * 
   * @return image
   */
  @Override
  public String getImage() {
    return "processes.png";
  }

}
