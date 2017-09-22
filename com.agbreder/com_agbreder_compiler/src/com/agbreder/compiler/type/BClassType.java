package com.agbreder.compiler.type;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tipo de classe
 * 
 * 
 * @author Bernardo Breder
 */
public class BClassType extends RType {

  /** Tipo de retorno */
  private RType returnType;
  /** Tipo de parametros */
  private List<RType> paramsType = new ArrayList<RType>();

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
  public boolean isClass() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "class";
  }

  /**
   * @param arg0
   * @see java.util.List#add(java.lang.Object)
   */
  public void addParam(RType arg0) {
    paramsType.add(arg0);
  }

  /**
   * Retorna
   * 
   * @return returnType
   */
  public RType getReturnType() {
    return returnType;
  }

  /**
   * @param returnType
   */
  public void setReturnType(RType returnType) {
    this.returnType = returnType;
  }

  /**
   * Retorna
   * 
   * @return paramsType
   */
  public List<RType> getParamsType() {
    return paramsType;
  }

  /**
   * @param paramsType
   */
  public void setParamsType(List<RType> paramsType) {
    this.paramsType = paramsType;
  }

}
