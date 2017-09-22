package com.agbreder.compiler.parser.node;

import java.io.IOException;
import java.util.List;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBFieldException;
import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBTypeNode;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBGetFieldNode extends AGBUnaryNode {

	private AGBFieldNode field;

	private int index;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 * @param left
	 */
	public AGBGetFieldNode(AGBNode parent) {
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
		if (!leftType.isType() && !leftType.isThis()) {
			throw new AGBTokenException("get field only for class type", this.getLeft().getToken());
		}
		AGBClassNode classnode = leftType.getRef(context);
		List<AGBFieldNode> fields = classnode.getAllFields();
		int size = fields.size();
		String name = this.getToken().getImage();
		for (int n = size - 1; n >= 0; n--) {
			AGBFieldNode fieldnode = fields.get(n);
			if (fieldnode.getName().getImage().equals(name)) {
				this.field = fieldnode;
				this.index = n;
				break;
			}
		}
		if (this.field == null) {
			throw new AGBFieldException(leftType.getRef(context), this.getToken());
		}
		this.setType(this.field.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		this.getLeft().build(output);
		output.opObjectGet(index);
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getLeft());
		sb.append(".");
		sb.append(this.getToken());
		return sb.toString();
	}

}
