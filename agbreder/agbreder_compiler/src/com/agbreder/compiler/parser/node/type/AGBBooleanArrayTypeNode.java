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
public class AGBBooleanArrayTypeNode extends AGBTypeNode {

	/** Instancia Unica */
	private static final AGBBooleanArrayTypeNode instance = new AGBBooleanArrayTypeNode();

	/**
	 * Construtor
	 * 
	 * @param token
	 */
	public AGBBooleanArrayTypeNode(AGBToken token) {
		super(token);
	}

	/**
	 * Construtor
	 */
	public AGBBooleanArrayTypeNode() {
		this(new AGBToken("", "bool", AGBTokenType.BOOL.getId(), -1, -1));
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
	public static AGBBooleanArrayTypeNode getInstance() {
		return instance;
	}

}
