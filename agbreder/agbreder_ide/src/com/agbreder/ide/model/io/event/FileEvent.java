package com.agbreder.ide.model.io.event;

import com.agbreder.ide.model.io.IFile;
import com.agbreder.ide.model.io.IFolder;

/**
 * Interface de evento de sistema de arquivo
 * 
 * @author bernardobreder
 */
public interface FileEvent {
	
	/**
	 * Evento de criação de diretório
	 * 
	 * @param folder
	 */
	public void createdFolder(IFolder folder);
	
	/**
	 * Evento de criação de arquivo
	 * 
	 * @param file
	 */
	public void createdFile(IFile file);
	
	/**
	 * Evento de criação de diretório
	 * 
	 * @param folder
	 */
	public void deletedFolder(IFolder folder);
	
	/**
	 * Evento de criação de arquivo
	 * 
	 * @param file
	 */
	public void deletedFile(IFile file);
	
	/**
	 * Evento de criação de diretório
	 * 
	 * @param folder
	 */
	public void changedFolder(IFolder folder);
	
	/**
	 * Evento de criação de arquivo
	 * 
	 * @param file
	 */
	public void changedFile(IFile file);
	
	/**
	 * Atualiza a pasta
	 * 
	 * @param folder
	 */
	public void refreshFolder(IFolder folder);
	
}
