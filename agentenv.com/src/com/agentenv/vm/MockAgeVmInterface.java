package com.agentenv.vm;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Mock da interface para fazer testes
 * 
 * @author bernardobreder
 * 
 */
public class MockAgeVmInterface implements AgeVmInterface {

	/** Saida do Console */
	private ByteArrayOutputStream output = new ByteArrayOutputStream();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void log(char c) {
		output.write(c);
	}

	/**
	 * @return the output
	 */
	public String getOutput() {
		try {
			return new String(output.toByteArray(), "utf8");
		} catch (UnsupportedEncodingException e) {
			return new String(output.toByteArray());
		}
	}

}
