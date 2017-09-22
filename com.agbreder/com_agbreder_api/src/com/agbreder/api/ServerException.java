package com.agbreder.api;

import java.io.IOException;

/**
 * Serviço indisponível
 * 
 * 
 * @author Bernardo Breder
 */
public class ServerException extends IOException {

  /**
   * Construtor
   * 
   * @param t
   */
  public ServerException(Throwable t) {
    super(t);
  }

}
