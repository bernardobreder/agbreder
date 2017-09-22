package com.agbreder.ide.model.io;

import com.agbreder.ide.model.io.event.BFileEvent;
import com.agbreder.ide.model.io.event.FileEvent;

/**
 * Interface de eventos que ocorreram no sistema de arquivo
 * 
 * @author bernardobreder
 */
public interface IFileEvent {

  /** Instancia unica */
  public static final IFileEvent DEFAULT = BFileEvent.DEFAULT;

  /**
   * Adiciona um evento
   * 
   * @param event
   */
  public void addListener(FileEvent event);

  /**
   * Dispara o evento de criação de Diretório
   * 
   * @param folder
   */
  public void fireRefresh(IFolder folder);
  
  /**
   * Dispara o evento de criação de Diretório
   * 
   * @param folder
   */
  public void fireCreatedFolder(IFolder folder);

  /**
   * Dispara o evento de criação de Arquivo
   * 
   * @param file
   */
  public void fireCreatedFile(IFile file);

  /**
   * Dispara o evento de remoção de Diretório
   * 
   * @param folder
   */
  public void fireDeletedFolder(IFolder folder);

  /**
   * Dispara o evento de remoção de Arquivo
   * 
   * @param file
   */
  public void fireDeletedFile(IFile file);

  /**
   * Dispara o evento de remoção de Diretório
   * 
   * @param folder
   */
  public void fireChangedFolder(IFolder folder);

  /**
   * Dispara o evento de remoção de Arquivo
   * 
   * @param file
   */
  public void fireChangedFile(IFile file);

}
