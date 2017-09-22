package com.agbreder.ide.gui.explorer.io;

import java.io.File;

import com.agbreder.ide.model.io.IFileSystem;
import com.agbreder.ide.model.io.local.BFileSystem;


/**
 * Sistema de arquivo
 * 
 * 
 * @author Bernardo Breder
 */
public class IdeFileSystem extends BFileSystem implements IFileSystem {

  /** Instancia padrão */
  private static final IdeFileSystem DEFAULT = new IdeFileSystem();

  /**
   * Construtor
   */
  private IdeFileSystem() {
    super(new File("project"));
  }

  /**
   * Padrão
   * 
   * @return instance
   */
  public static IdeFileSystem getDefault() {
    return DEFAULT;
  }

}
