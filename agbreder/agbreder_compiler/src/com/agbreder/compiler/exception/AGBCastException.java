package com.agbreder.compiler.exception;

import com.agbreder.compiler.parser.node.type.AGBTypeNode;
import com.agbreder.compiler.token.AGBToken;

/**
 * Erro de cast entre tipos
 * 
 * @author bernardobreder
 */
public class AGBCastException extends AGBTokenException {

	/**
	 * Construtor
	 * 
	 * @param left
	 * @param right
	 * @param msg
	 * @param token
	 */
	public AGBCastException(AGBTypeNode left, AGBTypeNode right, AGBToken token) {
		super(String.format("can not cast '%s' to '%s'", left.getToken().getImage(), right.getToken().getImage()), token);
	}

}
