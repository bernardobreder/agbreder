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
public class AGBStringTypeNode extends AGBTypeNode {

	/** Instancia Unica */
	private static final AGBStringTypeNode instance = new AGBStringTypeNode();

	/**
	 * Construtor
	 * 
	 * @param token
	 */
	public AGBStringTypeNode(AGBToken token) {
		super(token);
	}

	/**
	 * Construtor
	 */
	private AGBStringTypeNode() {
		this(new AGBToken("", "str", AGBTokenType.STR.getId(), -1, -1));
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
	public static AGBStringTypeNode getInstance() {
		return instance;
	}

}
