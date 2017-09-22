package com.agbreder.compiler.parser.node;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.parser.AGBCompileContext;

/**
 * Node de Unary
 * 
 * @author bernardobreder
 */
public abstract class AGBUnaryNode extends AGBRValueNode {

	private AGBRValueNode left;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 * @param left
	 */
	public AGBUnaryNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		this.getLeft().header(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		this.getLeft().body(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		this.getLeft().link(context);
	}

	/**
	 * @return the left
	 */
	public AGBRValueNode getLeft() {
		return left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(AGBRValueNode left) {
		this.left = left;
		this.left.setParent(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getToken());
		sb.append(" ");
		sb.append(this.getLeft());
		return sb.toString();
	}

}
