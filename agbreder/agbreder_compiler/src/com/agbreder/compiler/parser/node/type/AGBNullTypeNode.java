package com.agbreder.compiler.parser.node.type;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.token.AGBToken;
import com.agbreder.compiler.token.AGBTokenType;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBNullTypeNode extends AGBTypeNode {

	/** Instancia Unica */
	private static final AGBNullTypeNode instance = new AGBNullTypeNode();

	/**
	 * Construtor
	 */
	public AGBNullTypeNode() {
		this(new AGBToken("", "null", AGBTokenType.NULL.getId(), -1, -1));
	}

	/**
	 * Construtor
	 * 
	 * @param token
	 */
	public AGBNullTypeNode(AGBToken token) {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNull() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isObject() {
		return true;
	}

	/**
	 * @return the instance
	 */
	public static AGBNullTypeNode getInstance() {
		return instance;
	}

}
