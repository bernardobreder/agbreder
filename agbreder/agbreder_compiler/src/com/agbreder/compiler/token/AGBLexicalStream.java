package com.agbreder.compiler.token;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Realiza a leitura da stream
 * 
 * @author bernardobreder
 */
public class AGBLexicalStream {

	private final StringBuilder text;

	private int index;

	private int mark;

	private int line;

	private int column;

	/**
	 * Construtor
	 * 
	 * @param input
	 * @throws IOException
	 */
	public AGBLexicalStream(InputStream input) throws IOException {
		text = new StringBuilder();
		InputStreamReader reader = new InputStreamReader(input, "utf-8");
		for (int n; (n = reader.read()) != -1;) {
			text.append((char) n);
		}
		input.close();
		this.line = 1;
		this.column = 1;
	}

	/**
	 * Realiza a leitura de um char
	 * 
	 * @return char
	 */
	public char read() {
		if (this.isEof()) {
			return 0;
		}
		char charAt = this.text.charAt(index++);
		if (charAt == '\n') {
			this.line++;
			this.column = 0;
		} else {
			this.column++;
		}
		return charAt;
	}

	/**
	 * Realiza a leitura de um char
	 * 
	 * @return char
	 */
	public char get() {
		if (this.isEof()) {
			return 0;
		}
		return this.text.charAt(index);
	}

	/**
	 * Realiza a leitura de um char
	 * 
	 * @param index
	 * @return char
	 */
	public char get(int index) {
		if (this.isEof(index)) {
			return 0;
		}
		return this.text.charAt(this.index + index);
	}

	/**
	 * Volta um indice
	 * 
	 * @return this
	 */
	public AGBLexicalStream back() {
		this.index--;
		this.column--;
		return this;
	}

	/**
	 * Indica se chegou no final do arquivo
	 * 
	 * @return final do arquivo
	 */
	public boolean isEof() {
		return this.index >= this.text.length();
	}

	/**
	 * Indica se chegou no final do arquivo
	 * 
	 * @param index
	 * @return final do arquivo
	 */
	public boolean isEof(int index) {
		return this.index + index >= this.text.length();
	}

	/**
	 * Marca a posição atual
	 * 
	 * @return posição atual
	 */
	public AGBLexicalStream mark() {
		this.mark = this.index;
		return this;
	}

	/**
	 * Desmarca e retorna a string
	 * 
	 * @return Desmarca e retorna a string
	 */
	public String substring() {
		return this.text.substring(mark, this.index);
	}

	/**
	 * @return the line
	 */
	public int getLine() {
		return line;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @return the text
	 */
	public StringBuilder getText() {
		return text;
	}

}
