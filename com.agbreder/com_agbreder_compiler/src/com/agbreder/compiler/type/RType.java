package com.agbreder.compiler.type;

import com.agbreder.compiler.node.RStruct;

/**
 * Tipo
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class RType extends RStruct {

  /**
   * Indica se é um tipo primitivo
   * 
   * @return primitivo
   */
  public boolean isPrimitive() {
    return false;
  }

  /**
   * Indica se é um inteiro
   * 
   * @return tipo
   */
  public boolean isNumber() {
    return false;
  }

  /**
   * Indica se é um inteiro
   * 
   * @return tipo
   */
  public boolean isString() {
    return false;
  }

  /**
   * Indica se é um inteiro
   * 
   * @return tipo
   */
  public boolean isBoolean() {
    return false;
  }

  /**
   * Indica se é um inteiro
   * 
   * @return tipo
   */
  public boolean isObject() {
    return false;
  }

  /**
   * Indica se é um inteiro
   * 
   * @return tipo
   */
  public boolean isVoid() {
    return false;
  }

  /**
   * Indica se é um inteiro
   * 
   * @return tipo
   */
  public boolean isClass() {
    return false;
  }

  /**
   * Indica se pode fazer um cast
   * 
   * @param type
   * @return ok
   */
  public boolean canCast(RType type) {
    return false;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public abstract String toString();

}
