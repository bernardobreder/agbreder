package com.agbreder.compiler.parser.node;

import java.io.IOException;

import com.agbreder.compiler.exception.AGBCastException;
import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBNumberTypeNode;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBOrBitNode extends AGBBinaryNode {

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBOrBitNode(AGBNode parent) {
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
		if (!this.getLeft().getType().isNumber()) {
			throw new AGBCastException(this.getLeft().getType(), AGBNumberTypeNode.getInstance(), this.getLeft().getToken());
		}
		if (!this.getRight().getType().isNumber()) {
			throw new AGBCastException(this.getRight().getType(), AGBNumberTypeNode.getInstance(), this.getRight().getToken());
		}
		this.setType(this.getLeft().getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		this.getLeft().build(output);
		this.getRight().build(output);
		output.opNumberOrBit();
	}

}
