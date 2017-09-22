package com.agwmail.model.user;

/**
 * Contexto do usuário
 * 
 * 
 * @author Bernardo Breder
 */
public class UserContext {

  /** Timer */
  private long timer;
  /** */
  private User user;

  /**
   * Construtor
   */
  public UserContext() {
    this.refreshTimer();
  }

  /**
   * Indica se houve timeout
   * 
   * @return timeout
   */
  public boolean isTimeout() {
    return this.timer - System.currentTimeMillis() > 24 * 60 * 60 * 1000;
  }

  /**
   * Atualiza o tempo
   */
  public void refreshTimer() {
    this.timer = System.currentTimeMillis();
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

  /**
   * Retorna
   * 
   * @return timer
   */
  public long getTimer() {
    return timer;
  }

}
