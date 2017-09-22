package com.agbreder.compiler.parser.node;

import com.agbreder.compiler.parser.node.type.AGBTypeNode;
import com.agbreder.compiler.token.AGBToken;

/**
 * Node de Valor
 * 
 * @author bernardobreder
 */
public abstract class AGBValueNode extends AGBNode implements AGBValue {

	private AGBToken token;

	private AGBTypeNode type;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 */
	public AGBValueNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	public AGBToken getToken() {
		return token;
	}

	/**
	 * {@inheritDoc}
	 */
	public AGBTypeNode getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setType(AGBTypeNode type) {
		this.type = type;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(AGBToken token) {
		this.token = token;
	}

}
