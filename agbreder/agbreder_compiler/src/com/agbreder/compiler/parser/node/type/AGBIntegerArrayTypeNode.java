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
public class AGBIntegerArrayTypeNode extends AGBTypeNode {

	/** Instancia Unica */
	private static final AGBIntegerArrayTypeNode instance = new AGBIntegerArrayTypeNode();

	/**
	 * Construtor
	 * 
	 * @param token
	 */
	public AGBIntegerArrayTypeNode(AGBToken token) {
		super(token);
	}

	/**
	 * Construtor
	 */
	public AGBIntegerArrayTypeNode() {
		this(new AGBToken("", "num", AGBTokenType.NUM.getId(), -1, -1));
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
	public static AGBIntegerArrayTypeNode getInstance() {
		return instance;
	}

}
