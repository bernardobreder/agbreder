package com.agbreder.api;

import java.io.IOException;

/**
 * Serviço indisponível
 * 
 * 
 * @author Bernardo Breder
 */
public class InvalidServiceException extends IOException {

  /**
   * Construtor
   */
  public InvalidServiceException() {
    super("service not available");
  }

}
