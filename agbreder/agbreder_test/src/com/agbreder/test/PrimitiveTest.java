package com.agbreder.test;

import org.junit.Test;

/**
 * Testador de Compilador
 * 
 * @author bernardobreder
 */
public class PrimitiveTest extends BasicTest {
	
	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void primitive() throws Exception {
		eq(ex(exps("\"aa\"")), "aa");
		eq(ex(expn("1")), "1");
		eq(ex(expb("true")), "true");
		eq(ex(expb("false")), "false");
	}
	
	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void operator() throws Exception {
		eq(ex(expn("1+1")), "2");
		eq(ex(expn("5-2")), "3");
		eq(ex(expn("1+1*5")), "6");
		eq(ex(expn("10/2")), "5");
		eq(ex(expn("10/2+1")), "6");
		eq(ex(expb("true or true")), "true");
		eq(ex(expb("true or false")), "true");
		eq(ex(expb("false or true")), "true");
		eq(ex(expb("false or false")), "false");
		eq(ex(expb("true and true")), "true");
		eq(ex(expb("true and false")), "false");
		eq(ex(expb("true and false")), "false");
		eq(ex(expb("false and false")), "false");
	}
	
	/**
	 * Executa a expressão
	 * 
	 * @param code
	 * @return code
	 */
	private String exps(String code) {
		return exp("|log.prints," + code + "|");
	}
	
	/**
	 * Executa a expressão
	 * 
	 * @param code
	 * @return code
	 */
	private String expn(String code) {
		return exp("|log.printn," + code + "|");
	}
	
	/**
	 * Executa a expressão
	 * 
	 * @param code
	 * @return code
	 */
	private String expb(String code) {
		return exp("|log.printb," + code + "|");
	}
	
	/**
	 * Executa a expressão
	 * 
	 * @param code
	 * @return code
	 */
	private String exp(String code) {
		return "class Main do static this main() do " + code
			+ " return this end end";
	}
	
}
