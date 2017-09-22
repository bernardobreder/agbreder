package com.agentenv.compiler.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * OutputStream que não escreve
 * 
 * @author bernardobreder
 */
public class EmptryOutputStream extends OutputStream {

	/** */
	private int size;

	@Override
	public void write(int b) throws IOException {
		this.size++;
	}

	/**
	 * @return the size
	 */
	public int size() {
		return size;
	}

	/**
	 * Reinicia o tamanho
	 */
	public void reset() {
		this.size = 0;
	}

}
