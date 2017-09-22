package com.agbreder.server.servlet.core;

import java.io.IOException;

/**
 * Serviço indisponível
 * 
 * 
 * @author Bernardo Breder
 */
public class AutenticatedException extends IOException {

  /**
   * Construtor
   * 
   * @param username
   */
  public AutenticatedException(String username) {
    super("wrong username or password for: " + username);
  }

}
