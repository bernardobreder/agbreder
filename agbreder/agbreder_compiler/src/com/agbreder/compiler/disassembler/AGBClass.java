package com.agbreder.compiler.disassembler;

import java.util.ArrayList;
import java.util.List;

/**
 * Estrutura de Classe de uma binário
 * 
 * @author bernardobreder
 */
public class AGBClass {

	private final String name;

	private final int extendIndex;

	private final int fields;

	private final List<AGBMethod> methods = new ArrayList<AGBMethod>();

	/**
	 * Construtor
	 * 
	 * @param name
	 * @param extendIndex
	 * @param fields
	 */
	public AGBClass(String name, int extendIndex, int fields) {
		super();
		this.name = name;
		this.extendIndex = extendIndex;
		this.fields = fields;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the methods
	 */
	public List<AGBMethod> getMethods() {
		return methods;
	}

	/**
	 * @return the extendIndex
	 */
	public int getExtendIndex() {
		return extendIndex;
	}

	/**
	 * @return the fields
	 */
	public int getFields() {
		return fields;
	}

}
