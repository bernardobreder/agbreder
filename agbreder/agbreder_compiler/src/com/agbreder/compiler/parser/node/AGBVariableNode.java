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
public class AGBVariableNode extends AGBNode {

	private AGBToken token;

	private final AGBTypeNode type;

	private boolean declared;

	private Integer index;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 * @param type
	 */
	public AGBVariableNode(AGBNode parent, AGBToken token, AGBTypeNode type) {
		super(parent);
		this.token = token;
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		this.type.header(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		this.type.body(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		this.type.link(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) {
	}

	/**
	 * @return the token
	 */
	public AGBToken getToken() {
		return token;
	}

	/**
	 * @return the type
	 */
	public AGBTypeNode getType() {
		return type;
	}

	/**
	 * @return the declared
	 */
	public boolean isDeclared() {
		return declared;
	}

	/**
	 * @param declared
	 *            the declared to set
	 */
	public void setDeclared(boolean declared) {
		this.declared = declared;
	}

	/**
	 * @return the index
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getType());
		sb.append(" ");
		sb.append(this.getToken());
		return sb.toString();
	}

}
