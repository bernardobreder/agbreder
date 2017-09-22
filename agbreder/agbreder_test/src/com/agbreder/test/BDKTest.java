package com.agbreder.test;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import com.agbreder.compiler.AGBCompilerHelper;

/**
 * Testador de Compilador
 * 
 * @author bernardobreder
 */
public class BDKTest extends BasicTest {
	
	/** Fonte do BDK */
	private static final File SOURCE_DIR = new File("../agbreder_bdk/src");
	
	/**
	 * Testa o arquivo
	 * 
	 * @throws Exception
	 */
	@Test
	public void execute() throws Exception {
		File binaryFile = File.createTempFile("agb", "binary");
		try {
			AGBCompilerHelper console =
				new AGBCompilerHelper(Arrays.asList(SOURCE_DIR));
			binaryFile.deleteOnExit();
			console.setOutputFile(binaryFile);
			console.start();
			Process process =
				new ProcessBuilder(getAgbFile(), /*"-e",*/ binaryFile.getAbsolutePath(), "Test")
					.start();
			InputStream input = process.getInputStream();
			InputStream error = process.getErrorStream();
			for (int n; ((n = input.read()) != -1);) {
				System.out.print((char) n);
			}
			for (int n; ((n = error.read()) != -1);) {
				System.out.print((char) n);
			}
			Assert.assertEquals(0, process.waitFor());
		} finally {
			binaryFile.delete();
		}
	}
	
}
