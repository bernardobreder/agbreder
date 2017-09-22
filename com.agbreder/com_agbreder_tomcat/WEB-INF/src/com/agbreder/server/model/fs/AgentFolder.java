package com.agbreder.server.model.fs;

import java.util.ArrayList;
import java.util.List;

/**
 * Pasta de um agente
 * 
 * 
 * @author Bernardo Breder
 */
public class AgentFolder extends AgentResource {

  /** Filhos */
  protected final List<AgentResource> children = new ArrayList<AgentResource>();

  /**
   * Construtor
   * 
   * @param id
   * @param parent
   * @param name
   */
  public AgentFolder(long id, AgentFolder parent, String name) {
    super(id, parent, name);
  }

  /**
   * Indica se é arquivo
   * 
   * @return arquivo
   */
  @Override
  public boolean isFolder() {
    return true;
  }

  /**
   * Lista os filhos
   * 
   * @return filhos
   */
  public List<AgentResource> list() {
    return this.children;
  }

  /**
   * Retorna a imagem
   * 
   * @return image
   */
  @Override
  public String getImage() {
    return "folder.png";
  }

}
