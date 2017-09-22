package com.agbreder.compiler.node;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Classe basica
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class RStruct {

  /**
   * Serializa o objeto
   * 
   * @param output
   * @throws IOException
   */
  public abstract void save(DataOutputStream output) throws IOException;

}
