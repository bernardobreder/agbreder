package com.agbreder.compiler.exception;

/**
 * Erro lexical
 * 
 * @author bernardobreder
 */
public class DesassemblerException extends AGBException {

	/**
	 * Construtor
	 * 
	 * @param msg
	 * @param args
	 * @param line
	 * @param column
	 */
	public DesassemblerException(String msg, Object... args) {
		super("[desassembler]: " + String.format(msg, args));
	}

}
