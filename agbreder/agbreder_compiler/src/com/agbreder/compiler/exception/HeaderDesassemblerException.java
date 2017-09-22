package com.agbreder.compiler.exception;

/**
 * Erro lexical
 * 
 * @author bernardobreder
 */
public class HeaderDesassemblerException extends DesassemblerException {

	/**
	 * Construtor
	 * 
	 * @param msg
	 * @param args
	 * @param line
	 * @param column
	 */
	public HeaderDesassemblerException() {
		super("wrong header of bytecode");
	}

}
