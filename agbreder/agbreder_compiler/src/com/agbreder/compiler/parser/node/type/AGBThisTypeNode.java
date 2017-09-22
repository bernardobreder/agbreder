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
public class AGBThisTypeNode extends AGBTypeNode {

	/** Instancia Unica */
	private static final AGBThisTypeNode instance = new AGBThisTypeNode();

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBThisTypeNode() {
		this(new AGBToken("", "this", AGBTokenType.THIS.getId(), -1, -1));
	}

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 */
	public AGBThisTypeNode(AGBToken token) {
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
	public void body(AGBCompileContext context) throws AGBException {
		this.getRef(context);
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
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType() {
		return true;
	}

	/**
	 * Indica se é do tipo boolean
	 * 
	 * @return tipo especifico
	 */
	@Override
	public boolean isThis() {
		return true;
	}

	/**
	 * @return the instance
	 */
	public static AGBThisTypeNode getInstance() {
		return instance;
	}

}
