package com.agbreder.compiler.type;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Tipo inteiro
 * 
 * 
 * @author Bernardo Breder
 */
public class RStringType extends RType {

  /** Instancia padrão */
  public static final RStringType DEFAULT = new RStringType();

  /**
   * Construtor
   */
  private RStringType() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(DataOutputStream output) throws IOException {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPrimitive() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isString() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canCast(RType type) {
    if (type.getClass() == RStringType.class) {
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "str";
  }

}
