package com.agbreder.compiler.parser.node;

import java.io.IOException;

import com.agbreder.compiler.AGBConstant;
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
public class AGBCastNode extends AGBUnaryNode {

	private AGBTypeNode type;

	private Integer index;

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBCastNode(AGBNode parent) {
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
		AGBTypeNode type = this.getType();
		AGBTypeNode leftType = this.getLeft().getType();
		if (leftType.isPrimitive() && !leftType.isNull() && !leftType.isObject()) {
			throw new AGBTokenException("can not cast a primitive value", this.getLeft().getToken());
		}
		if (type.isPrimitive()) {
			throw new AGBTokenException("can not cast to primitive type", type.getToken());
		}
		this.setType(type);
		if (!leftType.isNull()) {
			if (type.isFunction()) {
				this.index = AGBConstant.OBJECT_FUNCTION_INDEX;
			} else {
				this.index = type.getRef(context).getIndex();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		this.getLeft().build(output);
		if (!this.getLeft().getType().isNull()) {
			if (this.index < 0) {
				output.opObjectCastNative(-this.index);
			} else {
				output.opObjectCast(this.index);
			}
		}
	}

	/**
	 * @param type
	 *            the type to set
	 */
	@Override
	public void setType(AGBTypeNode type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	@Override
	public AGBTypeNode getType() {
		return type;
	}

}
