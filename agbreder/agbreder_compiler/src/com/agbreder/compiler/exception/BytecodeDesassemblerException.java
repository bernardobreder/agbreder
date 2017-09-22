package com.agbreder.compiler.exception;

/**
 * Erro lexical
 * 
 * @author bernardobreder
 */
public class BytecodeDesassemblerException extends DesassemblerException {

	/**
	 * Construtor
	 * 
	 * @param msg
	 * @param args
	 * @param line
	 * @param column
	 */
	public BytecodeDesassemblerException() {
		super("wrong opcode of bytecode");
	}

}
