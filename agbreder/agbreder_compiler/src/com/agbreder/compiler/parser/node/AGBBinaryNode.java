package com.agbreder.compiler.parser.node;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.parser.AGBCompileContext;

/**
 * Node Binary
 * 
 * @author bernardobreder
 */
public abstract class AGBBinaryNode extends AGBUnaryNode {

	private AGBRValueNode right;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 * @param left
	 * @param right
	 */
	public AGBBinaryNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		super.header(context);
		this.getRight().header(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		super.body(context);
		this.getRight().body(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		super.link(context);
		this.getRight().link(context);
	}

	/**
	 * @return the right
	 */
	public AGBRValueNode getRight() {
		return right;
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(AGBRValueNode right) {
		this.right = right;
		this.right.setParent(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getLeft());
		sb.append(" ");
		sb.append(this.getToken());
		sb.append(" ");
		sb.append(this.getRight());
		return sb.toString();
	}

}
