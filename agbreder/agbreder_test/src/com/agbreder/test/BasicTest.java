package com.agbreder.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import junit.framework.Assert;

import com.agbreder.compiler.AGBCompiler;
import com.agbreder.compiler.disassembler.AGBDesassemblerConsole;
import com.agbreder.compiler.parser.AGBParser;
import com.agbreder.compiler.parser.node.AGBCommandNode;
import com.agbreder.compiler.parser.node.AGBNode;
import com.agbreder.compiler.token.AGBLexical;
import com.agbreder.compiler.util.FileUtil;
import com.agbreder.vm.AGBVm;

/**
 * Testador de Compilador
 * 
 * @author bernardobreder
 */
public class BasicTest extends AbstractTest {

	/**
	 * Executa e falha
	 * 
	 * @param code
	 */
	protected void fail(String code) {
		try {
			compile(code);
		} catch (Throwable e) {
			return;
		}
		Assert.fail();
	}

	/**
	 * Executa e falha
	 * 
	 * @param code
	 */
	protected void runtimeFail(String code) {
		boolean fail = false;
		try {
			ex(code);
		} catch (Throwable e) {
			fail = true;
		}
		Assert.assertEquals(true, fail);
	}

	/**
	 * Executa e falha
	 * 
	 * @param code
	 */
	protected void fail(String code, int codeExit) {
		boolean fail = false;
		try {
			ex(code, codeExit, true);
		} catch (Throwable e) {
			fail = true;
		}
		Assert.assertEquals(true, fail);
	}

	/**
	 * Execute
	 * 
	 * @param code
	 * @return stream
	 * @throws Exception
	 */
	protected String ex(String code) throws Exception {
		return ex(code, 0, true);
	}

	/**
	 * Execute
	 * 
	 * @param code
	 * @return stream
	 * @throws Exception
	 */
	protected String ex(String code, int expectedCodeExit, boolean compareWithJava) throws Exception {
		byte[] bytes = this.compile(code);
		String resultJava;
		String resultC;
		{
			AGBVm vm = new AGBVm(new ByteArrayInputStream(bytes));
			int index = vm.getOpcodeIndex("Main", "main()");
			if (index >= 0) {
				vm.execute(index);
			} else {
				throw new IllegalArgumentException();
			}
			resultJava = new String(vm.getOutputStream().toByteArray(), "utf-8");
		}
		{
			File binary = File.createTempFile("agb", "binary");
			try {
				{
					FileOutputStream output = new FileOutputStream(binary);
					output.write(bytes);
					output.close();
				}
				{
					Process process = new ProcessBuilder(getAgbFile(), /* "-e", */binary.getAbsolutePath()).start();
					Assert.assertEquals(0, process.waitFor());
					InputStream input = process.getInputStream();
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					for (int n; (n = input.read()) != -1;) {
						output.write((char) n);
					}
					resultC = new String(output.toByteArray(), "utf-8");
				}
			} finally {
				binary.delete();
			}
		}
		if (compareWithJava) {
			Assert.assertEquals(resultJava, resultC);
		}
		return resultC;
	}

	/**
	 * Execute
	 * 
	 * @param code
	 * @return stream
	 * @throws Exception
	 */
	protected byte[] compile(String code) throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ByteArrayInputStream input = new ByteArrayInputStream(code.getBytes("utf8"));
		AGBCompiler.compile(output, input);
		return output.toByteArray();
	}

}
