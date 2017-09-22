package com.agbreder.ide.model.io.local;

import java.io.File;
import java.io.IOException;

import com.agbreder.ide.model.io.IFileSystem;
import com.agbreder.ide.model.io.IFolder;
import com.agbreder.ide.model.io.IResource;

/**
 * Sistema de arquivo
 * 
 * @author Bernardo Breder
 */
public class BFileSystem extends BFolder implements IFileSystem {

  public static final File DIR = new File("project");

  /** Recursos copiados */
  private IResource[] copied;

  /** Copiado */
  private boolean copy;

  /** Instancia unica */
  private static final BFileSystem instance = new BFileSystem();

  /**
   * Construtor
   */
  protected BFileSystem() {
    super(DIR);
    if (!this.file.exists()) {
      this.file.mkdirs();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IResource[] list() throws IOException {
    java.io.File[] files = this.file.listFiles();
    IResource[] result = new IResource[files.length];
    for (int n = 0; n < files.length; n++) {
      java.io.File file = files[n];
      if (file.isDirectory()) {
        result[n] = BFolder.newInstance(file);
      }
      else {
        result[n] = BFile.newInstance(file);
      }
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IResource[] getCopiedResources() {
    return this.copied;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCopiedResources(IResource[] resources, boolean isCopy) {
    this.copied = resources;
    this.copy = isCopy;
  }

  /**
   * Instancia unica
   * 
   * @return instancia
   */
  public static BFileSystem getInstance() {
    return instance;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCopied() {
    return this.copy;
  }

  /**
   * Home {@inheritDoc}
   */
  @Override
  public IFolder getHomeFolder() {
    String env = System.getenv("HOME");
    if (env == null) {
      return null;
    }
    else {
      return BFolder.newInstance(new File(env));
    }
  }

}
