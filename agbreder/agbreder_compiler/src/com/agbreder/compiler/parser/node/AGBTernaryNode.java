package com.agbreder.compiler.parser.node;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.parser.AGBCompileContext;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public abstract class AGBTernaryNode extends AGBBinaryNode {

	private AGBRValueNode center;

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBTernaryNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		super.header(context);
		this.getCenter().header(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		super.body(context);
		this.getCenter().body(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		super.link(context);
		this.getCenter().link(context);
	}

	/**
	 * @return the center
	 */
	public AGBRValueNode getCenter() {
		return center;
	}

	/**
	 * @param center
	 *            the center to set
	 */
	public void setCenter(AGBRValueNode center) {
		this.center = center;
		this.center.setParent(this);
	}

}
