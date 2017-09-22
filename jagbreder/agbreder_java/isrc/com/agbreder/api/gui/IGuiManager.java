package com.agbreder.api.gui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.agbreder.api.gui.ui.IComponent;

/**
 * Gerenciador de interface. Essa classe é responsável por gerenciar os recursos
 * da interface gráfica.
 * 
 * @author bernardobreder
 */
public interface IGuiManager {
	
	/**
	 * Constroi o componente a partir de uma stream
	 * 
	 * @param input
	 * @return componente
	 * @throws IOException
	 */
	public IComponent build(InputStream input) throws IOException;
	
	/**
	 * Constroi o componente a partir de uma stream
	 * 
	 * @param component
	 * @param output
	 * @throws IOException
	 */
	public void encode(IComponent component, OutputStream output)
		throws IOException;
	
	/**
	 * Constroi o componente a partir de uma stream
	 * 
	 * @param input
	 * @return componente
	 * @throws IOException
	 */
	public InputStream decode(InputStream input) throws IOException;
	
}
