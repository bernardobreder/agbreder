package com.agbreder.ide.model.io.event;

import com.agbreder.ide.model.io.IFile;
import com.agbreder.ide.model.io.IFolder;

/**
 * Implementação vazia
 * 
 * 
 * @author bbreder
 */
public class FileAdapter implements FileEvent {

  /**
   * {@inheritDoc}
   */
  @Override
  public void createdFolder(IFolder folder) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createdFile(IFile file) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deletedFolder(IFolder folder) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deletedFile(IFile file) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changedFolder(IFolder folder) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changedFile(IFile file) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshFolder(IFolder folder) {
  }

}
