package com.agbreder.compiler.exception;

import com.agbreder.compiler.parser.node.AGBClassNode;
import com.agbreder.compiler.token.AGBToken;

/**
 * Erro de cast entre tipos
 * 
 * @author bernardobreder
 */
public class AGBFieldException extends AGBTokenException {

	/**
	 * Construtor
	 * 
	 * @param left
	 * @param right
	 * @param msg
	 * @param token
	 */
	public AGBFieldException(AGBClassNode node, AGBToken token) {
		super(String.format("can not found the field '%s' of class '%s'", token.getImage(), node.getName().getImage()), token);
	}

}
