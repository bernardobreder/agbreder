package com.agbreder.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class RequestOutputStream extends DataOutputStream {
	
	/**
	 * @param out
	 */
	public RequestOutputStream(OutputStream out) {
		super(out);
	}
	
	/**
	 * Escreve uma String compactada
	 * 
	 * @param text
	 * @throws IOException
	 */
	public void writeZipUTF(String text) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(bytes);
		gzip.write(text.getBytes("utf-8"));
		gzip.close();
		this.writeInt(bytes.size());
		this.write(bytes.toByteArray());
	}
	
}
