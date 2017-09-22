package com.agbreder.compiler.type;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Tipo inteiro
 * 
 * 
 * @author Bernardo Breder
 */
public class RObjectType extends RType {

  /** Instancia padrão */
  public static final RObjectType DEFAULT = new RObjectType();

  /**
   * Construtor
   */
  private RObjectType() {
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
  public boolean isObject() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "obj";
  }

}
