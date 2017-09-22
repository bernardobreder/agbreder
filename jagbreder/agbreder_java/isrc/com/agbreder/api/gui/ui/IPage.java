package com.agbreder.api.gui.ui;

/**
 * Página.
 * 
 * @author bernardobreder
 */
public interface IPage extends IStruct {
	
	/**
	 * Retorna o cabeçalho
	 * 
	 * @return cabeçalho
	 */
	public IHeader getHeader();
	
	/**
	 * Retorna o corpo da página
	 * 
	 * @return corpo da página
	 */
	public IHeader getBody();
	
}
