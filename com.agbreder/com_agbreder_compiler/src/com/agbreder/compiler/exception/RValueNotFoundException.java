package com.agbreder.compiler.exception;


/**
 * Erro de operação indevida
 * 
 * 
 * @author Bernardo Breder
 */
public class RValueNotFoundException extends RException {

  /**
   * Construtor
   * 
   * @param name
   */
  public RValueNotFoundException(String name) {
    super(build(name));
  }

  /**
   * Constroi a msg
   * 
   * @param name
   * @return msg
   */
  private static String build(String name) {
    return String.format("lvalue not found : %s", name);
  }

}
