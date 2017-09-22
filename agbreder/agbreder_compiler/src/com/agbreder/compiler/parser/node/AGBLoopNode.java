package com.agbreder.compiler.parser.node;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public abstract class AGBLoopNode extends AGBCommandNode {

	/** Inicio do PC */
	private int beginPc;

	/** Inicio do PC */
	private int endPc;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param cond
	 * @param cmd
	 */
	public AGBLoopNode(AGBCommandNode parent) {
		super(parent);
	}

	/**
	 * @return the beginPc
	 */
	public int getBeginPc() {
		return beginPc;
	}

	/**
	 * @param beginPc
	 *            the beginPc to set
	 */
	public void setBeginPc(int beginPc) {
		this.beginPc = beginPc;
	}

	/**
	 * @return the endPc
	 */
	public int getEndPc() {
		return endPc;
	}

	/**
	 * @param endPc
	 *            the endPc to set
	 */
	public void setEndPc(int endPc) {
		this.endPc = endPc;
	}

}
