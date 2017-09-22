package com.agwmail.model.user;

/**
 * Usuario
 * 
 * 
 * @author Bernardo Breder
 */
public class User {

  /** Código */
  private int id;
  /** Usuario */
  private String username;
  /** Usuario */
  private String email;

  /**
   * Retorna
   * 
   * @return id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Retorna
   * 
   * @return username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Retorna
   * 
   * @return email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email
   */
  public void setEmail(String email) {
    this.email = email;
  }

}
