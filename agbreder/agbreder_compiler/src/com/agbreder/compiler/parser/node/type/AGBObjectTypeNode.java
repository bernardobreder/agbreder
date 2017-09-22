package com.agbreder.compiler.parser.node.type;

import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.token.AGBToken;
import com.agbreder.compiler.token.AGBTokenType;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBObjectTypeNode extends AGBTypeNode {

	/** Instancia Unica */
	private static final AGBObjectTypeNode instance = new AGBObjectTypeNode();

	/**
	 * Construtor
	 * 
	 * @param token
	 */
	public AGBObjectTypeNode(AGBToken token) {
		super(token);
	}

	/**
	 * Construtor
	 */
	public AGBObjectTypeNode() {
		this(new AGBToken("", "obj", AGBTokenType.NUM.getId(), -1, -1));
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
	public void build(OpcodeOutputStream output) {
	}

	/**
	 * @return the instance
	 */
	public static AGBObjectTypeNode getInstance() {
		return instance;
	}

}
