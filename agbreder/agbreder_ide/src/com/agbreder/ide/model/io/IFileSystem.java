package com.agbreder.ide.model.io;

import com.agbreder.ide.model.io.local.BFileSystem;

/**
 * Retorna o sistema de arquivo
 * 
 * 
 * @author Bernardo Breder
 */
public interface IFileSystem extends IFolder {

  /** Retorna o default */
  public static final IFileSystem DEFAULT = BFileSystem.getInstance();

  /**
   * Retorna os arquivos copiados
   * 
   * @return os arquivos copiados
   */
  public IResource[] getCopiedResources();

  /**
   * Indica se foi copiado um arquivo
   * 
   * @return copiado
   */
  public boolean isCopied();

  /**
   * Atribui arquivos a ser copiado
   * 
   * @param resources
   * @param isCopy
   */
  public void setCopiedResources(IResource[] resources, boolean isCopy);

  /**
   * Retorna o home
   * 
   * @return diretorio
   */
  public IFolder getHomeFolder();

}
