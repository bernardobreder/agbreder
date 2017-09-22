package com.agbreder.server.model.user;

/**
 * Contexto do usuário
 * 
 * 
 * @author Bernardo Breder
 */
public class UserContext {

  /** */
  private User user;

  /**
   * Construtor
   */
  public UserContext() {
  }

  /**
   * Retorna
   * 
   * @return user
   */
  public User getUser() {
    return user;
  }

  /**
   * @param user
   */
  public void setUser(User user) {
    this.user = user;
  }

}
