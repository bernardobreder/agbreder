package com.agbreder.test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

/**
 * Classe base de teste
 * 
 * @author bernardobreder
 */
public abstract class AbstractTest {

	/**
	 * Testa a igualdade
	 * 
	 * @param actual
	 * @param array
	 */
	public static void eq(Object actual, Object... array) {
		if (actual.getClass().isArray() || actual instanceof List<?>
				|| array.length > 1) {
			Object expected = Arrays.asList(array);
			if (!expected.toString().equals(actual.toString())) {
				Assert.assertEquals(expected, actual);
			}
		} else {
			Assert.assertEquals(array[0], actual);
		}
	}

	protected String getAgbBrowserFile() {
		File file = new File("../agbreder_browser/Debug/agbreder_browser");
		if (file.exists()) {
			return file.getAbsolutePath();
		}
		file = new File("../agbreder_browser/Debug/agbreder_browser.exe");
		return file.getAbsolutePath();
	}
	
	protected String getAgbFile() {
		File file = new File("../agbreder_vm/Debug/agb");
		if (file.exists()) {
			return file.getAbsolutePath();
		}
		file = new File("../agbreder_vm/Debug/agb.exe");
		return file.getAbsolutePath();
	}

}
