package com.agbreder.server.model.fs;

/**
 * Pasta especial
 * 
 * 
 * @author Bernardo Breder
 */
public class AgentProcess extends AgentFolder {

  /**
   * Construtor
   * 
   * @param id
   * @param parent
   * @param name
   */
  public AgentProcess(long id, AgentFolder parent, String name) {
    super(id, parent, name);
  }

  /**
   * Retorna a imagem
   * 
   * @return image
   */
  @Override
  public String getImage() {
    return "process.png";
  }

}
