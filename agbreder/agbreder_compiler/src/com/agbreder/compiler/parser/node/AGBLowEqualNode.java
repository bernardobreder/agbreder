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
public class AGBLowEqualNode extends AGBBinaryNode {

	private Boolean value;

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBLowEqualNode(AGBNode parent) {
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
			if (this.getLeft().getType().isNumber()) {
				if (!this.getRight().getType().isNumber()) {
					this.value = false;
				}
			} else if (this.getLeft().getType().isString()) {
				if (!this.getRight().getType().isString()) {
					this.value = false;
				}
			} else if (this.getLeft().getType().isBoolean()) {
				if (!this.getRight().getType().isBoolean()) {
					this.value = false;
				}
			} else {
				this.value = false;
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
			if (!this.value) {
				output.opLoadFalse();
			}
		} else {
			this.getLeft().build(output);
			this.getRight().build(output);
			output.opLowerEqual(this.getLeft());
		}
	}

}
