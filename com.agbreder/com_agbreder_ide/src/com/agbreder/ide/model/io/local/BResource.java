package com.agbreder.ide.model.io.local;

import java.io.File;

import com.agbreder.ide.model.io.IFile;
import com.agbreder.ide.model.io.IFolder;
import com.agbreder.ide.model.io.IResource;


/**
 * Implementação de um recurso
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class BResource implements IResource {

  /** Arquivo */
  protected java.io.File file;

  /**
   * Construtor
   * 
   * @param file
   */
  BResource(java.io.File file) {
    this.file = file;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean exist() {
    if (this.file == null) {
      return true;
    }
    else {
      return this.file.exists();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    if (this.file == null) {
      return "";
    }
    else {
      return this.file.getName();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isFile() {
    return this instanceof IFile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isFolder() {
    if (this.file == null) {
      return true;
    }
    else {
      return this instanceof IFolder;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isHidden() {
    if (this.file == null) {
      return false;
    }
    else {
      return this.file.isHidden();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAbsoluteName() {
    if (this.file == null) {
      return "";
    }
    else {
      return this.file.getAbsolutePath().replace('\\', '/');
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IFolder getParent() {
    if (this.file == null) {
      return null;
    }
    File parent = this.file.getParentFile();
    return BFolder.newInstance(parent);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result =
      prime
        * result
        + ((file.getAbsolutePath() == null) ? 0 : file.getAbsolutePath()
          .hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    BResource other = (BResource) obj;
    if (file == null) {
      if (other.file != null) {
        return false;
      }
    }
    else if (!file.equals(other.file)) {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(IResource o) {
    return this.getName().compareTo(o.getName());
  }

}
