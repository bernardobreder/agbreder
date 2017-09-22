package com.agbreder.compiler.parser.node;

import java.io.IOException;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBBooleanTypeNode;
import com.agbreder.compiler.parser.node.type.AGBTypeNode;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBNotEqualNode extends AGBBinaryNode {

	private Boolean value;

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBNotEqualNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		super.header(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		super.body(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		super.link(context);
		AGBTypeNode leftType = this.getLeft().getType();
		AGBTypeNode rightType = this.getRight().getType();
		if (((leftType.isNumber() || leftType.isBoolean()) && rightType.isNull()) || ((rightType.isNumber() || rightType.isBoolean()) && leftType.isNull())) {
			throw new AGBTokenException("can not compare null value with number or boolean value", this.getRight().getToken());
		}
		if (!leftType.isNull() && !rightType.isNull()) {
			if (leftType.isNumber()) {
				if (!rightType.isNumber()) {
					this.value = true;
				}
			} else if (leftType.isString()) {
				if (!rightType.isString()) {
					this.value = true;
				}
			} else if (leftType.isBoolean()) {
				if (!rightType.isBoolean()) {
					this.value = true;
				}
			} else if (leftType.isObject()) {
				if (!rightType.isObject()) {
					this.value = true;
				}
			} else if (leftType.isFunction()) {
				if (!rightType.isFunction()) {
					this.value = false;
				}
			} else if (leftType.isType()) {
				if (!rightType.isType()) {
					this.value = false;
				}
			} else {
				this.value = true;
			}
		}
		this.setType(new AGBBooleanTypeNode(this.getLeft().getToken()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		if (this.value != null) {
			if (this.value) {
				output.opLoadTrue();
			}
		} else {
			this.getLeft().build(output);
			this.getRight().build(output);
			output.opNotEqual(this.getLeft());
		}
	}
}
