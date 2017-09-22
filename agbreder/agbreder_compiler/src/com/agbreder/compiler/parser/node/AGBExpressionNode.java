package com.agbreder.compiler.parser.node;

import java.io.IOException;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBExpressionNode extends AGBCommandNode {

	private final AGBRValueNode node;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param node
	 */
	public AGBExpressionNode(AGBCommandNode parent, AGBRValueNode node) {
		super(parent);
		this.node = node;
		this.node.setParent(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		this.getNode().header(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		this.getNode().body(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		this.getNode().link(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		this.getNode().build(output);
		output.opStackPop(1);
	}

	/**
	 * @return the node
	 */
	public AGBRValueNode getNode() {
		return node;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.getNode().toString();
	}

}
