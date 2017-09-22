package com.agbreder.compiler.parser.node;

import java.io.IOException;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBTypeNode;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBAssignNode extends AGBRValueNode {

	private AGBLValue left;

	private AGBRValue right;

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBAssignNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		this.getRight().header(context);
		this.getLeft().header(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		this.getRight().body(context);
		this.getLeft().body(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		this.getRight().link(context);
		this.getLeft().link(context);
		AGBTypeNode leftType = this.getLeft().getType();
		AGBTypeNode rightType = this.getRight().getType();
		if (!rightType.canCast(context, leftType)) {
			rightType.equals(leftType);
			throw new AGBTokenException(String.format("can not cast '%s' to '%s'", rightType.getToken().getImage(), leftType.getToken().getImage()), this.getRight().getToken());
		}
		{
			if (leftType.isNumber() || leftType.isBoolean()) {
				if (rightType.isNull()) {
					throw new AGBTokenException("can not assign null value with number or boolean value", this.getRight().getToken());
				}
			}
		}
		this.setType(rightType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		this.getRight().build(output);
		output.opStackDupAbs(0);
		this.getLeft().build(output);
	}

	/**
	 * @return the left
	 */
	public AGBLValue getLeft() {
		return left;
	}

	/**
	 * @return the right
	 */
	public AGBRValue getRight() {
		return right;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(AGBLValue left) {
		this.left = left;
		this.left.setParent(this);
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(AGBRValue right) {
		this.right = right;
		this.right.setParent(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.getLeft().toString() + " = " + this.getRight().toString();
	}

}
