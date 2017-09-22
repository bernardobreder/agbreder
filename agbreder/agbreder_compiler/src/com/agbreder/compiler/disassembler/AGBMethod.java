package com.agbreder.compiler.disassembler;

/**
 * Estrutura de Classe de uma binário
 * 
 * @author bernardobreder
 */
public class AGBMethod {

	private final String name;

	private final int pc;

	private final int pcs;

	private final boolean isStatic;

	/**
	 * Construtor
	 * 
	 * @param name
	 * @param isStatic
	 * @param pc
	 * @param pcs
	 */
	public AGBMethod(String name, boolean isStatic, int pc, int pcs) {
		super();
		this.name = name;
		this.isStatic = isStatic;
		this.pc = pc;
		this.pcs = pcs;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the pc
	 */
	public int getPc() {
		return pc;
	}

	/**
	 * @return the pcs
	 */
	public int getPcs() {
		return pcs;
	}

	/**
	 * @return the isStatic
	 */
	public boolean isStatic() {
		return isStatic;
	}

}
