package com.agbreder.server.model.fs;

/**
 * Pasta especial
 * 
 * 
 * @author Bernardo Breder
 */
public class AgentFunction extends AgentFile {

  /**
   * Construtor
   * 
   * @param id
   * @param parent
   * @param name
   */
  public AgentFunction(long id, AgentFolder parent, String name) {
    super(id, parent, name);
  }

  /**
   * Retorna a imagem
   * 
   * @return image
   */
  @Override
  public String getImage() {
    return "functiona.png";
  }

}
