package com.agbreder.test;

import java.io.ByteArrayInputStream;
import java.io.File;

import org.junit.Test;

import com.agbreder.compiler.token.AGBLexical;

/**
 * Testador de Compilador
 * 
 * @author bernardobreder
 */
public class TokenTest extends AbstractTest {
	
	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		eq(ex("aa[bb"), "aa", "[", "bb");
		eq(ex("a"), "a");
		eq(ex("1"), "1");
		eq(ex("1.1"), "1.1");
		eq(ex("1.11"), "1.11");
		eq(ex("ab"), "ab");
		eq(ex("a;b"), "a", ";", "b");
		eq(ex("a;1"), "a", ";", "1");
		eq(ex("a;;"), "a", ";", ";");
		eq(ex("a;; "), "a", ";", ";");
		eq(ex("a; ;"), "a", ";", ";");
		eq(ex("123"), "123");
		eq(ex("123."), "123.");
		eq(ex("// AA"));
		eq(ex("//AA"));
		eq(ex("//AA//BB"));
		eq(ex("/ /A"), "/", "/", "A");
		eq(ex("/*A"));
		eq(ex("/ *A"), "/", "*", "A");
		eq(ex("/*A*"));
		eq(ex("/*A*/"));
		eq(ex("/*A*/A"), "A");
		eq(ex("a.1"), "a", ".", "1");
		eq(ex("a.11"), "a", ".", "11");
		eq(ex("a.11.1"), "a", ".", "11.1");
		eq(ex("a.11.1."), "a", ".", "11.1", ".");
		eq(ex("a[b"), "a", "[", "b");
		eq(ex("a\"[\"b"), "a", "[", "b");
		eq(ex("a\"\"b"), "a", "", "b");
		eq(ex("a\"\\t\"b"), "a", "\t", "b");
		eq(ex("a[];"), "a", "[", "]", ";");
		eq(ex("a[1]=1;"), "a", "[", "1", "]", "=", "1", ";");
		eq(ex(" a[1]=1; "), "a", "[", "1", "]", "=", "1", ";");
		eq(ex(" a [ 1 ] = 1 ; "), "a", "[", "1", "]", "=", "1", ";");
		eq(ex("\tstatic this main () do\n"), "static", "this", "main", "(", ")",
			"do");
		eq(ex("class TestColor do\nstatic this main () do\n"), "class",
			"TestColor", "do", "static", "this", "main", "(", ")", "do");
		eq(ex("TestColor.test1()"), "TestColor", ".", "test1", "(", ")");
		eq(ex("\"\\\"a\\\"\""), "\"a\"");
		eq(ex("\"\\na\\t\""), "\na\t");
		eq(ex("\"\\\\a\\r\""), "\\a\r");
		eq(ex("\"\\a\\\""), "\\a\"");
		eq(ex("\"\\a\\a\""), "\\a\\a");
	}
	
	/**
	 * Execute
	 * 
	 * @param code
	 * @return value
	 * @throws Exception
	 */
	public Object ex(String code) throws Exception {
		return AGBLexical.execute(new File("test.agb").toString(), new ByteArrayInputStream(
			code.getBytes()));
	}
	
}
