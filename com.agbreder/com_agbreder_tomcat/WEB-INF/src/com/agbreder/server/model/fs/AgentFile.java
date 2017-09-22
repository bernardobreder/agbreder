package com.agbreder.server.model.fs;

/**
 * Pasta de um agente
 * 
 * 
 * @author Bernardo Breder
 */
public class AgentFile extends AgentResource {

  /**
   * Construtor
   * 
   * @param id
   * @param parent
   * @param name
   */
  public AgentFile(long id, AgentFolder parent, String name) {
    super(id, parent, name);
  }

  /**
   * Indica se é arquivo
   * 
   * @return arquivo
   */
  @Override
  public boolean isFile() {
    return true;
  }

}
