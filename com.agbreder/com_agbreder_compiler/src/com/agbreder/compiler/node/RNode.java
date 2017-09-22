package com.agbreder.compiler.node;

import com.agbreder.compiler.AGBCompilerOpcode;

/**
 * Classe basica
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class RNode extends RStruct {

  /**
   * Constroi o cabeçalho
   * 
   * @param c
   * @throws Exception
   */
  public abstract void head(ContextNode c) throws Exception;

  /**
   * Constroi o cabeçalho
   * 
   * @param c
   * @throws Exception
   */
  public abstract void compile(ContextNode c) throws Exception;

  /**
   * Constroi o cabeçalho
   * 
   * @param c
   * @throws Exception
   */
  public abstract void link(ContextNode c) throws Exception;

  /**
   * Constroi os opcodes
   * 
   * @param opcodes
   * @throws Exception
   */
  public abstract void build(AGBCompilerOpcode opcodes) throws Exception;

}
