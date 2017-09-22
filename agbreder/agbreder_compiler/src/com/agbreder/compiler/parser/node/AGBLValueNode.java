package com.agbreder.compiler.parser.node;

/**
 * Node para lvalue
 * 
 * @author bernardobreder
 */
public abstract class AGBLValueNode extends AGBValueNode implements AGBLValue {

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 */
	public AGBLValueNode(AGBNode parent) {
		super(parent);
	}

}
