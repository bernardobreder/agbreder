package com.agbreder.compiler.parser.node;

import com.agbreder.compiler.token.AGBToken;

/**
 * Classe primitiva
 * 
 * @author bernardobreder
 */
public abstract class AGBPrimitiveNode extends AGBRValueNode {

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 */
	public AGBPrimitiveNode(AGBNode parent, AGBToken token) {
		super(parent);
		this.setToken(token);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.getToken().getImage();
	}

}
