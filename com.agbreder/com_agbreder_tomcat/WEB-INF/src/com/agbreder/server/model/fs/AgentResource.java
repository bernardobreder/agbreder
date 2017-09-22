package com.agbreder.server.model.fs;

/**
 * Recurso no sistema de arquivo
 * 
 * 
 * @author Bernardo Breder
 */
public class AgentResource {

  /** Id */
  private final long id;
  /** Parent */
  private final AgentFolder parent;
  /** Nome */
  private final String name;

  /**
   * Construtor
   * 
   * @param id
   * @param parent
   * @param name
   */
  public AgentResource(long id, AgentFolder parent, String name) {
    super();
    this.id = id;
    this.parent = parent;
    this.name = name;
  }

  /**
   * Indica se é arquivo
   * 
   * @return arquivo
   */
  public boolean isFile() {
    return false;
  }

  /**
   * Indica se é arquivo
   * 
   * @return arquivo
   */
  public boolean isFolder() {
    return false;
  }

  /**
   * Retorna
   * 
   * @return id
   */
  public long getId() {
    return id;
  }

  /**
   * Retorna
   * 
   * @return parent
   */
  public AgentFolder getParent() {
    return parent;
  }

  /**
   * Retorna
   * 
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Retorna a imagem
   * 
   * @return image
   */
  public String getImage() {
    return "file.png";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((parent == null) ? 0 : parent.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    AgentResource other = (AgentResource) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    }
    else if (!name.equals(other.name)) {
      return false;
    }
    if (parent == null) {
      if (other.parent != null) {
        return false;
      }
    }
    else if (!parent.equals(other.parent)) {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "AgentResource [parent=" + parent + ", name=" + name + "]";
  }

}
