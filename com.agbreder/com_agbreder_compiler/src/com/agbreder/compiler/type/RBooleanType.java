package com.agbreder.compiler.type;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Tipo inteiro
 * 
 * 
 * @author Bernardo Breder
 */
public class RBooleanType extends RType {

  /** Instancia padrão */
  public static final RBooleanType DEFAULT = new RBooleanType();

  /**
   * Construtor
   */
  private RBooleanType() {
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
  public boolean isBoolean() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canCast(RType type) {
    if (type.getClass() == RBooleanType.class) {
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "bool";
  }

}
