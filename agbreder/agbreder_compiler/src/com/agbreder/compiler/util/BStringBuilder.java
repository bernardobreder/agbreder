package com.agbreder.compiler.util;

import java.util.List;

/**
 * StringBuilder mais eficiente para criar uma string
 * 
 * @author bernardobreder
 */
public class BStringBuilder {

	private int len;

	private List<String> values = new LightArrayList<String>();

	/**
	 * Adiciona uma String
	 * 
	 * @param text
	 */
	public void append(String text) {
		this.len += text.length();
		this.values.add(text);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		char[] chars = new char[this.len];
		for (int n = 0, m = 0; n < values.size(); n++) {
			String value = this.values.get(n);
			value.getChars(0, value.length(), chars, m);
			m += value.length();
		}
		return new String(chars);
	}

	/**
	 * Retorna o tamanho
	 * 
	 * @return tamanho
	 */
	public int length() {
		return this.len;
	}

}
