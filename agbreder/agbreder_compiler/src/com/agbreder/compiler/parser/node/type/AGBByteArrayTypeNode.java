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
public class AGBByteArrayTypeNode extends AGBTypeNode {

	/** Instancia Unica */
	private static final AGBByteArrayTypeNode instance = new AGBByteArrayTypeNode();

	/**
	 * Construtor
	 * 
	 * @param token
	 */
	public AGBByteArrayTypeNode(AGBToken token) {
		super(token);
	}

	/**
	 * Construtor
	 */
	public AGBByteArrayTypeNode() {
		this(new AGBToken("", "byte", AGBTokenType.BYTE.getId(), -1, -1));
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
	public static AGBByteArrayTypeNode getInstance() {
		return instance;
	}

}
