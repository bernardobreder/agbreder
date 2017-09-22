package com.agbreder.compiler;

import com.agbreder.compiler.exception.AGBException;

/**
 * Console de Compilador
 * 
 * @author bernardobreder
 */
public class AGBCompilerConsole {

	/**
	 * Inicializador
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AGBCompilerHelper.main(args);
		} catch (AGBException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
