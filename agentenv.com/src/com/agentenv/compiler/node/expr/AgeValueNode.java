package com.agentenv.compiler.node.expr;

import com.agentenv.compiler.lexical.AgeToken;
import com.agentenv.compiler.node.AgeNode;
import com.agentenv.compiler.node.AgeNodeInterface;

public abstract class AgeValueNode extends AgeNode implements AgeValueInterface {

	private AgeToken token;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 */
	public AgeValueNode(AgeNodeInterface parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	public AgeToken getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(AgeToken token) {
		this.token = token;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isPrimitive() {
		return false;
	}

}
