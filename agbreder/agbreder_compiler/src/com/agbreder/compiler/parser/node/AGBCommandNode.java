package com.agbreder.compiler.parser.node;

/**
 * Node de comando
 * 
 * @author bernardobreder
 */
public abstract class AGBCommandNode extends AGBNode {

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBCommandNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * Indica se foi retornado algum valor
	 * 
	 * @return retornado algum valor
	 */
	public boolean isReturned() {
		return false;
	}

}
