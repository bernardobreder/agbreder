package com.agbreder.compiler.parser.node;

import java.io.IOException;

import com.agbreder.compiler.exception.AGBCastException;
import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBTypeNode;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBReturnNode extends AGBCommandNode {

	private AGBRValueNode left;

	private AGBMethodNode method;

	private int index;

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBReturnNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		this.getLeft().header(context);
		this.setMethod(context.getMethodStack().peek());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		this.getLeft().body(context);
		this.index = this.getMethod().getVariables().size() + this.getMethod().getParams().size() + (this.getMethod().isStatic() ? 0 : 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		this.getLeft().link(context);
		checkMethodType(context);
	}

	/**
	 * @param context
	 * @throws AGBException
	 * @throws AGBCastException
	 */
	private void checkMethodType(AGBCompileContext context) throws AGBException, AGBCastException {
		AGBMethodNode method = context.getMethodStack().peek();
		AGBTypeNode type = method.getType(context);
		if (!this.getLeft().getType().canCast(context, type)) {
			throw new AGBCastException(type, this.getLeft().getType(), this.getLeft().getToken());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		this.getLeft().build(output);
		output.opJumpReturn(this.getIndex() + (output.getCounter() - 1));
	}

	/**
	 * @return the left
	 */
	public AGBRValueNode getLeft() {
		return left;
	}

	/**
	 * @return the method
	 */
	public AGBMethodNode getMethod() {
		return method;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	@Override
	public boolean isReturned() {
		return true;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(AGBMethodNode method) {
		this.method = method;
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
		sb.append("return");
		sb.append(" ");
		sb.append(this.getLeft().toString());
		return sb.toString();
	}

}
