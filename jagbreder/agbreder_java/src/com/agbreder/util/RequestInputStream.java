package com.agbreder.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class RequestInputStream extends DataInputStream {
	
	/**
	 * @param in
	 */
	public RequestInputStream(InputStream in) {
		super(in);
	}
	
	/**
	 * Escreve uma String compactada
	 * 
	 * @return string
	 * @throws IOException
	 */
	public String readZipUTF() throws IOException {
		int size = this.readInt();
		byte[] bytes = new byte[size];
		this.read(bytes);
		GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(bytes));
		ByteArrayOutputStream output = new ByteArrayOutputStream(bytes.length);
		for (int n; ((n = gzip.read()) != -1);) {
			output.write((char) n);
		}
		return new String(output.toByteArray(), "utf-8");
	}
	
}
