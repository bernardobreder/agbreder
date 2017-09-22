package com.agbreder.compiler.parser.node;

import java.io.IOException;

import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.token.AGBToken;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBLIdentifyNode extends AGBRIdentifyNode implements AGBLValue {

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 * @param params
	 */
	public AGBLIdentifyNode(AGBNode parent, AGBToken token) {
		super(parent, token);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException {
		output.opStackSave(this.getIndex());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.getToken().getImage();
	}

}
