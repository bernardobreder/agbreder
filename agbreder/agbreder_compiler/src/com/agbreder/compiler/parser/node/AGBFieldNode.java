package com.agbreder.compiler.parser.node;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBTypeNode;
import com.agbreder.compiler.token.AGBToken;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBFieldNode extends AGBNode {

	private AGBTypeNode type;

	private AGBToken name;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param type
	 * @param name
	 */
	public AGBFieldNode(AGBNode parent, AGBTypeNode type, AGBToken name) {
		super(parent);
		this.type = type;
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) {
		context.getClassStack().peek().addField(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) {
	}

	/**
	 * @return the type
	 */
	public AGBTypeNode getType() {
		return type;
	}

	/**
	 * @return the name
	 */
	public AGBToken getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getType());
		sb.append(" ");
		sb.append(this.getName());
		return sb.toString();
	}

}
