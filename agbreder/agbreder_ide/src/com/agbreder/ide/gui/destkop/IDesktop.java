package com.agbreder.ide.gui.destkop;

import com.agbreder.ide.gui.console.IDEConsole;
import com.agbreder.ide.model.io.IFile;
import com.agbreder.ide.model.io.IFolder;
import com.agbreder.ide.model.io.IResource;

/**
 * Interface de comunicação com o desktop
 * 
 * @author bernardobreder
 */
public interface IDesktop {

  /** Instancia unica */
  public static final IDesktop DEFAULT = DesktopAction.getInstance();

  /**
   * Retorno o recurso selecionado
   * 
   * @return recurso selecionado
   */
  public IResource getSelectedResource();

  /**
   * Retorno o recurso selecionado
   * 
   * @return recurso selecionado
   */
  public IFile getSelectedFile();

  /**
   * Retorno o recurso selecionado
   * 
   * @return recurso selecionado
   */
  public IFolder getSelectedProject();

  /**
   * Retorno o recurso selecionado
   * 
   * @return recurso selecionado
   */
  public IFolder getSelectedSource();

  /**
   * Retorno o recurso selecionado
   * 
   * @return recurso selecionado
   */
  public IFolder getSelectedPackage();

  /**
   * Fecha o desktop
   */
  public void close();

  /**
   * Fecha o desktop
   */
  public void open();

  /**
   * Abre um arquivo
   * 
   * @param file
   * @param content
   */
  public void openEditor(IFile file, String content);

  /**
   * Abre um arquivo
   * 
   * @return mudança
   */
  public boolean saveAllEditors();

  /**
   * Abre um arquivo
   * 
   * @return mudança
   */
  public boolean saveEditor();

  /**
   * Abre um arquivo
   */
  public void closeAllEditors();

  /**
   * Abre um arquivo
   */
  public void closeEditor();

  /**
   * Adiciona um marcador
   * 
   * @param file
   * @param lin
   * @param col
   * @param length
   * @param message
   */
  public void addMarker(IFile file, int lin, int col, int length, String message);

  /**
   * Retorna o console
   * 
   * @return console
   */
  public IDEConsole getConsole();

  /**
   * Atualiza a IDE
   */
  public void updateIde();

  /**
   * Retorna o frame da ide
   * 
   * @return frame da ide
   */
  public DesktopAction getFrame();

  /**
   * Executa o projeto corrente
   */
  public void runProject();

}
