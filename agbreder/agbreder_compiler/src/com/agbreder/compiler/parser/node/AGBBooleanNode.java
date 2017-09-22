package com.agbreder.compiler.parser.node;

import java.io.IOException;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBBooleanTypeNode;
import com.agbreder.compiler.token.AGBToken;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBBooleanNode extends AGBPrimitiveNode {

	private boolean value;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 * @param value
	 */
	public AGBBooleanNode(AGBNode parent, AGBToken token, boolean value) {
		super(parent, token);
		this.value = value;
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
		this.setType(new AGBBooleanTypeNode(this.getToken()));
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
	public void build(OpcodeOutputStream output) throws IOException {
		output.opLoadBoolean(this.value);
	}

}
