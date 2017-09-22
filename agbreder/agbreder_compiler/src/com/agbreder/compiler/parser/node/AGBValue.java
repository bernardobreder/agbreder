package com.agbreder.compiler.parser.node;

import com.agbreder.compiler.parser.node.type.AGBTypeNode;
import com.agbreder.compiler.token.AGBToken;

/**
 * Indica que é um lvalue
 * 
 * @author bernardobreder
 */
public interface AGBValue extends AGBNodeInterface {

	/**
	 * Retorna o token que o representa
	 * 
	 * @return the token
	 */
	public AGBToken getToken();

	/**
	 * Retorna o tipo do valor
	 * 
	 * @return the type
	 */
	public AGBTypeNode getType();

	/**
	 * Altera o tipo do valor
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(AGBTypeNode type);

}
