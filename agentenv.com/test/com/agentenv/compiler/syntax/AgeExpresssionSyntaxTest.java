package com.agentenv.compiler.syntax;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.agentenv.compiler.AgeCompiler;
import com.agentenv.vm.AgeVm;
import com.agentenv.vm.MockAgeVmInterface;

public class AgeExpresssionSyntaxTest {

	@Test
	public void test() throws Exception {
		eq(ex("1"), 1.0);
		eq(ex("1+2"), 3.0);
		eq(ex("1+2+3"), 6.0);
		eq(ex("1+2+3+4"), 10.0);
		eq(ex("1+2+3+4+5"), 15.0);
		eq(ex("1+2*3*4+5"), 30.0);
		eq(ex("1+2*3+4*5"), 27.0);
		eq(ex("1+2+3*4*5"), 63.0);
		eq(ex("1*2*3*4+5"), 29.0);
	}

	/**
	 * Executa o código
	 * 
	 * @param code
	 * @return
	 * @throws IOException
	 * @throws AgeSyntaxException
	 */
	protected static Object ex(String code) throws IOException, AgeSyntaxException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		AgeCompiler.compile(output, new ByteArrayInputStream(code.getBytes("utf8")));
		MockAgeVmInterface library = new MockAgeVmInterface();
		AgeVm vm = new AgeVm(new ByteArrayInputStream(output.toByteArray()), library);
		return vm.execute(0);
	}

	/**
	 * Compara os valores
	 * 
	 * @param atual
	 * @param expected
	 */
	protected static void eq(Object atual, Object expected) {
		Assert.assertEquals(expected, atual);
	}

	/**
	 * Testador
	 * 
	 * @param args
	 * @throws AgeSyntaxException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, AgeSyntaxException {
		ex("1+2*3+4*5");
	}

}
