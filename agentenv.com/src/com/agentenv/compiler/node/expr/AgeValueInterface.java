package com.agentenv.compiler.node.expr;

import com.agentenv.compiler.node.AgeNodeInterface;

/**
 * Estrututa de um valor
 * 
 * @author bernardobreder
 * 
 */
public interface AgeValueInterface extends AgeNodeInterface {

	/**
	 * Indica se a instrução de carga é unica
	 * 
	 * @return
	 */
	public boolean isPrimitive();

}
