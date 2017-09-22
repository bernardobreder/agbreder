package com.agbreder.api.gui.ui;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Representa uma estrutura que pode ser serializado
 * 
 * @author bernardobreder
 */
public interface IStruct {
	
	/**
	 * Salva o componente na stream
	 * 
	 * @param output
	 */
	public void save(DataOutputStream output);
	
	/**
	 * Carrega a página na stream
	 * 
	 * @param input
	 */
	public void load(DataInputStream input);
	
}
