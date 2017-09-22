package com.agbreder.plugin.builder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BStream extends Thread {

	private final InputStream input;

	public BStream(InputStream input) {
		super();
		this.input = input;
	}

	@Override
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(input);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				System.out.println(line);
			br.close();
		} catch (Throwable ioe) {
			ioe.printStackTrace();
		}

	}

}
