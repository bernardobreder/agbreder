package com.agbreder.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Stream de arquivo somente quando for usado
 * 
 * @author bernardobreder
 * 
 */
public class LazyFileInputStream extends InputStream {

	/** Arquivo */
	private final File file;
	/** Stream */
	private FileInputStream input;

	/**
	 * Construtor
	 * 
	 * @param file
	 */
	public LazyFileInputStream(File file) {
		this.file = file;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read() throws IOException {
		if (this.input == null) {
			this.input = new FileInputStream(file);
		}
		return this.input.read();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		if (this.input != null) {
			this.input.close();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.file.toString();
	}

}
