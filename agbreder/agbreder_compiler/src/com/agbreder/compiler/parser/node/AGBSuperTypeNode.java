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
public class AGBSuperTypeNode extends AGBTypeNode {

	/**
	 * Construtor
	 * 
	 * @param token
	 */
	public AGBSuperTypeNode(AGBToken token) {
		super(token);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) {
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
	 * Indica se é do tipo boolean
	 * 
	 * @return tipo especifico
	 */
	public boolean isThis() {
		return true;
	}

}
