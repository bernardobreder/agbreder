package com.agbreder.compiler.exception;

import com.agbreder.compiler.token.AGBToken;

/**
 * Erro de cast entre tipos
 * 
 * @author bernardobreder
 */
public class AGBReferenceException extends AGBTokenException {

	/**
	 * Construtor
	 * 
	 * @param left
	 * @param right
	 * @param msg
	 * @param token
	 */
	public AGBReferenceException(AGBToken token) {
		super(String.format("can not found '%s'", token.getImage()), token);
	}

}
