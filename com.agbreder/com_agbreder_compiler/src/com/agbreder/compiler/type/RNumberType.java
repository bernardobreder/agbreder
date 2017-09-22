package com.agbreder.compiler.type;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Tipo inteiro
 * 
 * 
 * @author Bernardo Breder
 */
public class RNumberType extends RType {

  /** Instancia padrão */
  public static final RNumberType DEFAULT = new RNumberType();

  /**
   * Construtor
   */
  private RNumberType() {
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
  public boolean isNumber() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canCast(RType type) {
    if (type.getClass() == RNumberType.class) {
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "num";
  }

}
