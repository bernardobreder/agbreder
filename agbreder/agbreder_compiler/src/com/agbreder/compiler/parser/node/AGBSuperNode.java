package com.agbreder.compiler.parser.node;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBThisNode;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBSuperNode extends AGBThisNode {

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 * @param left
	 * @param right
	 */
	public AGBSuperNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		super.link(context);
		this.setType(new AGBSuperTypeNode(this.getToken()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "super";
	}

}
