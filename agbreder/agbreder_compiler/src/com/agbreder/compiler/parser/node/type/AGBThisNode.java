package com.agbreder.compiler.parser.node.type;

import java.io.IOException;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.AGBNode;
import com.agbreder.compiler.parser.node.AGBRValueNode;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBThisNode extends AGBRValueNode {

	private int index;

	private boolean isStatic;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 * @param left
	 * @param right
	 */
	public AGBThisNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		this.index = context.getMethodStack().peek().getVariables().size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		isStatic = context.getMethodStack().peek().isStatic();
		this.setType(new AGBThisTypeNode(this.getToken()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException {
		if (isStatic) {
			output.opLoadNull();
		} else {
			output.opStackLoad(this.index);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "this";
	}

}
