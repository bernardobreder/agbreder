package com.agbreder.compiler.parser.node;

import java.io.IOException;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBBooleanTypeNode;
import com.agbreder.compiler.parser.node.type.AGBStringTypeNode;
import com.agbreder.compiler.token.AGBToken;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBStringNode extends AGBPrimitiveNode {

	private int index;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 */
	public AGBStringNode(AGBNode parent, AGBToken token) {
		super(parent, token);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		this.index = context.addString(this.getToken().getImage());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) {
		this.setType(new AGBStringTypeNode(this.getToken()));
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
		output.opLoadStr(index);
	}

}
