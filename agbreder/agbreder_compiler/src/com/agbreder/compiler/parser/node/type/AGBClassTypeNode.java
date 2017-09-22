package com.agbreder.compiler.parser.node.type;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.AGBClassNode;
import com.agbreder.compiler.token.AGBToken;

/**
 * Classe responsável por representar um tipo de uma classe.
 * 
 * @author bernardobreder
 */
public class AGBClassTypeNode extends AGBTypeNode {

	/**
	 * Construtor
	 * 
	 * @param token
	 */
	public AGBClassTypeNode(AGBToken token) {
		super(token);
	}

	/**
	 * Construtor
	 * 
	 * @param token
	 * @param classNode
	 */
	public AGBClassTypeNode(AGBToken token, AGBClassNode classNode) {
		super(token);
		this.classNode = classNode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		this.getRef(context);
	}

}
