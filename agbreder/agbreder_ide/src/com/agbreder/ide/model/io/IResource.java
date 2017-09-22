package com.agbreder.ide.model.io;

import java.io.IOException;

/**
 * Recurso de uma arquivo
 * 
 * 
 * @author Bernardo Breder
 */
public interface IResource extends Comparable<IResource> {

  /**
   * Retorna o nome do recurso
   * 
   * @return nome do recurso
   */
  public String getName();

  /**
   * Retorna o caminho absoluto do recurso
   * 
   * @return caminho absoluto do recurso
   */
  public String getAbsoluteName();

  /**
   * Retorna o pai do recurso
   * 
   * @return pai do recurso
   */
  public String getPath();

  /**
   * Retorna o pai do recurso
   * 
   * @return pai do recurso
   */
  public IFolder getParent();
  
  /**
   * Retorna o pai do recurso
   * 
   * @return pai do recurso
   */
  public IFolder getProject();

  /**
   * Indica se é um diretório
   * 
   * @return diretorio
   */
  public boolean isFolder();

  /**
   * Indica se é um arquivo
   * 
   * @return arquivo
   */
  public boolean isFile();

  /**
   * Indica se é um projeto
   * 
   * @return arquivo
   */
  public boolean isProject();

  /**
   * Indica se é um projeto
   * 
   * @return arquivo
   */
  public boolean isSource();

  /**
   * Indica se é um projeto
   * 
   * @return arquivo
   */
  public boolean isPackage();

  /**
   * Indica se o recurso existe
   * 
   * @return existe
   */
  public boolean exist();

  /**
   * Indica se é um recurso escondido
   * 
   * @return escondido
   */
  public boolean isHidden();

  /**
   * Retorna o tamnho do recurso
   * 
   * @return tamenho
   */
  public long getLength();

  /**
   * Deleta o recurso
   * 
   * @throws IOException
   */
  public void delete() throws IOException;

  /**
   * Troca o nome do recurso
   * 
   * @param name
   * @return novo recurso
   * @throws IOException
   */
  public IResource rename(String name) throws IOException;

  /**
   * Move o recurso para uma pasta
   * 
   * @param dest
   * @throws IOException
   */
  public void move(IFolder dest) throws IOException;

  /**
   * Copia o recurso para uma pasta
   * 
   * @param dest
   * @throws IOException
   */
  public void copy(IFolder dest) throws IOException;

  /**
   * Converte para Pasta
   * 
   * @return pasta
   */
  public IFolder toFolder();

  /**
   * Converte para Pasta
   * 
   * @return pasta
   */
  public IFile toFile();

}
