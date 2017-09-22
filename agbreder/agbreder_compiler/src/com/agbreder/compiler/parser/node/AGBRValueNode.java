package com.agbreder.compiler.parser.node;

/**
 * Valor do lado direito
 * 
 * @author bernardobreder
 */
public abstract class AGBRValueNode extends AGBValueNode implements AGBRValue {

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 */
	public AGBRValueNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * Indica se o valor é um estático
	 * 
	 * @return valor é um estático
	 */
	public boolean isStatic() {
		return false;
	}

}
