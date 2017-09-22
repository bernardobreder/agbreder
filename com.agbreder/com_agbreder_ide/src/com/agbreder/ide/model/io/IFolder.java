package com.agbreder.ide.model.io;

import java.io.IOException;

/**
 * Estrutura de diretório
 * 
 * 
 * @author Bernardo Breder
 */
public interface IFolder extends IResource {

  /**
   * Lista os recursos do diretório
   * 
   * @return recursos do diretório
   * @throws IOException
   */
  public IResource[] list() throws IOException;

  /**
   * Cria um arquivo
   * 
   * @param name
   * @return novo arquivo
   * @throws IOException
   */
  public IFile createFile(String name) throws IOException;

  /**
   * Cria um diretório
   * 
   * @param name
   * @return diretório
   * @throws IOException
   */
  public IFolder createFolder(String name) throws IOException;

  /**
   * Busca por um diretório em primeiro nível
   * 
   * @param name
   * @return diretório
   * @throws IOException
   */
  public IFolder getFolder(String name) throws IOException;

  /**
   * Busca por um arquivo em primeiro nível
   * 
   * @param name
   * @return arquivo
   * @throws IOException
   */
  public IFile getFile(String name) throws IOException;

  /**
   * Busca por um recurso
   * 
   * @param pattern
   * @return recursos
   */
  public IResource[] find(String pattern);

  /**
   * Busca por um recurso indicando o quanto profundo irá chegar
   * 
   * @param pattern
   * @param deep
   * @return recursos
   */
  public IResource[] find(String pattern, int deep);

  /**
   * Busca por um recurso indicando o quanto profundo irá chegar
   * 
   * @param pattern
   * @return recursos
   */
  public IFolder findFolder(String pattern);

  /**
   * Busca por um recurso
   * 
   * @param name
   * @param deep
   * @return recursos
   */
  public IFolder findFolder(String name, int deep);

  /**
   * Indica se um recurso existe
   * 
   * @param name
   * @return existencia de um recurso
   */
  public boolean exist(String name);

  /**
   * Cria o diretório
   * 
   * @return diretorio
   */
  public IFolder mkdirs();

}
