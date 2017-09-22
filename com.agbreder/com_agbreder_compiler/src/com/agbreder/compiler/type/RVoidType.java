package com.agbreder.compiler.type;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Tipo inteiro
 * 
 * 
 * @author Bernardo Breder
 */
public class RVoidType extends RType {

  /** Instancia padrão */
  public static final RVoidType DEFAULT = new RVoidType();

  /**
   * Construtor
   */
  private RVoidType() {
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
  public boolean isVoid() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canCast(RType type) {
    if (type.getClass() == RVoidType.class) {
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "void";
  }

}
