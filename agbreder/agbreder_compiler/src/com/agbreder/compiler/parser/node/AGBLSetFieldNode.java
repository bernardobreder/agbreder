package com.agbreder.compiler.parser.node;

import java.io.IOException;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.token.AGBToken;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBLSetFieldNode extends AGBLValueNode {

	private AGBGetFieldNode left;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param left
	 * @param token
	 * @param params
	 */
	public AGBLSetFieldNode(AGBNode parent, AGBGetFieldNode left, AGBToken token) {
		super(parent);
		this.setToken(token);
		this.setLeft(left);
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
		this.setType(this.getLeft().getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		this.getLeft().getLeft().build(output);
		output.opObjectSet(this.getLeft().getIndex());
	}

	/**
	 * @return the left
	 */
	public AGBGetFieldNode getLeft() {
		return left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(AGBGetFieldNode left) {
		this.left = left;
		this.left.setParent(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.getLeft().toString();
	}

}
