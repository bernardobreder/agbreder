package com.agbreder.test;

import org.junit.Test;

/**
 * Testador de Compilador
 * 
 * @author bernardobreder
 */
public class StringTest extends BasicTest {

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLen() throws Exception {
		eq(ex("class Main do static this main() do |log.printn,|string.len,\"a\"|| return this end end"), "1");
		eq(ex("class Main do static this main() do |log.printn,|string.len,\"abc\"|| return this end end"), "3");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testConcat() throws Exception {
		eq(ex("class Main do static this main() do |log.prints,|string.concat,\"a\",\"b\"|| return this end end"), "ab");
		eq(ex("class Main do static this main() do |log.prints,|string.concat,\"b\",\"a\"|| return this end end"), "ba");
		eq(ex("class Main do static this main() do |log.prints,|string.concat,\"ab\",\"ba\"|| return this end end"), "abba");
		eq(ex("class Main do static this main() do |log.prints,|string.concat,\"a\",|string.concat,\"b\",\"c\"||| return this end end"), "abc");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEqual() throws Exception {
		eq(ex("class Main do static this main() do |log.printb,|string.equal,\"a\",\"b\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.equal,\"a\",\"a\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,|string.equal,\"aa\",\"a\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.equal,\"a\",\"aa\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,\"a\"==\"aa\"| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,\"a\"==\"a\"| return this end end"), "true");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNotEqual() throws Exception {
		eq(ex("class Main do static this main() do |log.printb,|string.nequal,\"a\",\"b\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,|string.nequal,\"a\",\"a\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.nequal,\"aa\",\"a\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,|string.nequal,\"a\",\"aa\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,\"a\"!=\"aa\"| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,\"a\"!=\"a\"| return this end end"), "false");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGreater() throws Exception {
		eq(ex("class Main do static this main() do |log.printb,|string.gt,\"a\",\"b\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.gt,\"a\",\"a\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.gt,\"aa\",\"a\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,|string.gt,\"a\",\"aa\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,\"a\">\"aa\"| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,\"a\">\"a\"| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,\"aa\">\"a\"| return this end end"), "true");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGreaterEqual() throws Exception {
		eq(ex("class Main do static this main() do |log.printb,|string.egt,\"a\",\"b\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.egt,\"a\",\"a\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,|string.egt,\"aa\",\"a\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,|string.egt,\"a\",\"aa\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,\"a\">=\"aa\"| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,\"a\">=\"a\"| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,\"aa\">=\"a\"| return this end end"), "true");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLower() throws Exception {
		eq(ex("class Main do static this main() do |log.printb,|string.lt,\"a\",\"b\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,|string.lt,\"a\",\"a\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.lt,\"aa\",\"a\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.lt,\"a\",\"aa\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,\"a\"<\"aa\"| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,\"a\"<\"a\"| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,\"aa\"<\"a\"| return this end end"), "false");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLowerEqual() throws Exception {
		eq(ex("class Main do static this main() do |log.printb,|string.elt,\"a\",\"b\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,|string.elt,\"a\",\"a\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,|string.elt,\"aa\",\"a\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.elt,\"a\",\"aa\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,\"a\"<=\"aa\"| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,\"a\"<=\"a\"| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,\"aa\"<=\"a\"| return this end end"), "false");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSubstring() throws Exception {
		eq(ex("class Main do static this main() do |log.prints,|string.substring,\"abc\",1,1|| return this end end"), "a");
		eq(ex("class Main do static this main() do |log.prints,|string.substring,\"abc\",1,0|| return this end end"), "");
		eq(ex("class Main do static this main() do |log.prints,|string.substring,\"abc\",1,2|| return this end end"), "ab");
		eq(ex("class Main do static this main() do |log.prints,|string.substring,\"abc\",1,3|| return this end end"), "abc");
		eq(ex("class Main do static this main() do |log.prints,|string.substring,\"abc\",2,3|| return this end end"), "bc");
		eq(ex("class Main do static this main() do |log.prints,|string.substring,\"abc\",3,3|| return this end end"), "c");
		eq(ex("class Main do static this main() do |log.prints,|string.substring,\"abc\",2,2|| return this end end"), "b");
		eq(ex("class Main do static this main() do |log.prints,|string.substring,\"abc\",3,3|| return this end end"), "c");
		eq(ex("class Main do static this main() do |log.prints,|string.substring,\"abc\",4,4|| return this end end"), "");
		eq(ex("class Main do static this main() do |log.prints,|string.substring,\"abc\",3,4|| return this end end"), "c");
		eq(ex("class Main do static this main() do |log.prints,|string.substring,\"abc\",0,1|| return this end end"), "a");
		eq(ex("class Main do static this main() do |log.prints,|string.substring,\"abc\",0,0|| return this end end"), "");
		runtimeFail("class Main do static this main() do |log.prints,|string.substring,null,1,1|| return this end end");
		eq(ex("class Main do static this main() do |log.prints,|string.substring,\"abc\",-1,1|| return this end end"), "a");
		eq(ex("class Main do static this main() do |log.prints,|string.substring,\"abc\",0,4|| return this end end"), "abc");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCodeToChar() throws Exception {
		eq(ex("class Main do static this main() do |log.prints,|string.code_to_char,32|| return this end end"), " ");
		eq(ex("class Main do static this main() do |log.prints,|string.code_to_char,97|| return this end end"), "a");
		eq(ex("class Main do static this main() do |log.prints,|string.code_to_char,-200|| return this end end"), "null");
		eq(ex("class Main do static this main() do |log.prints,|string.code_to_char,3000000000|| return this end end"), "null");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCharToCode() throws Exception {
		eq(ex("class Main do static this main() do |log.printn,|string.char_to_code,\"a\"|| return this end end"), "97");
		eq(ex("class Main do static this main() do |log.printn,|string.char_to_code,\" \"|| return this end end"), "32");
		eq(ex("class Main do static this main() do |log.printn,|string.char_to_code,\"ab\"|| return this end end"), "97");
		eq(ex("class Main do static this main() do |log.printn,|string.char_to_code,null|| return this end end"), "0");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTrim() throws Exception {
		eq(ex("class Main do static this main() do |log.prints,|string.trim,\"abc\"|| return this end end"), "abc");
		eq(ex("class Main do static this main() do |log.prints,|string.trim,\" abc\"|| return this end end"), "abc");
		eq(ex("class Main do static this main() do |log.prints,|string.trim,\"abc \"|| return this end end"), "abc");
		eq(ex("class Main do static this main() do |log.prints,|string.trim,\" abc \"|| return this end end"), "abc");
		eq(ex("class Main do static this main() do |log.prints,|string.trim,\"  abc  \"|| return this end end"), "abc");
		eq(ex("class Main do static this main() do |log.prints,|string.trim,\"\tabc\t\"|| return this end end"), "abc");
		eq(ex("class Main do static this main() do |log.prints,|string.trim,\" \tabc\t \"|| return this end end"), "abc");
		eq(ex("class Main do static this main() do |log.prints,|string.trim,\"\"|| return this end end"), "");
		eq(ex("class Main do static this main() do |log.prints,|string.trim,\"   \"|| return this end end"), "");
		eq(ex("class Main do static this main() do |log.prints,|string.trim,null|| return this end end"), "null");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCharAt() throws Exception {
		eq(ex("class Main do static this main() do |log.printn,|string.charat,\"abc\",0|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.charat,\"abc\",1|| return this end end"), "97");
		eq(ex("class Main do static this main() do |log.printn,|string.charat,\"abc\",2|| return this end end"), "98");
		eq(ex("class Main do static this main() do |log.printn,|string.charat,\"abc\",3|| return this end end"), "99");
		eq(ex("class Main do static this main() do |log.printn,|string.charat,\"abc\",4|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.charat,null,0|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.charat,\"\",1|| return this end end"), "-1");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testStartWith() throws Exception {
		eq(ex("class Main do static this main() do |log.printb,|string.startwith,\"abc\",\"a\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,|string.startwith,\"abc\",\"b\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.startwith,\"abc\",\"c\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.startwith,\"\",\"a\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.startwith,\"\",\"\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,|string.startwith,\"abc\",\"ab\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,|string.startwith,\"abc\",\"bc\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.startwith,\"abc\",\"abc\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,|string.startwith,\"abc\",\"abcd\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.startwith,null,\"\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.startwith,\"\",null|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.startwith,null,null|| return this end end"), "false");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEndWith() throws Exception {
		eq(ex("class Main do static this main() do |log.printb,|string.endwith,\"abc\",\"a\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.endwith,\"abc\",\"b\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.endwith,\"abc\",\"c\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,|string.endwith,\"\",\"a\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.endwith,\"\",\"\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,|string.endwith,\"abc\",\"ab\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.endwith,\"abc\",\"bc\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,|string.endwith,\"abc\",\"abc\"|| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,|string.endwith,\"abc\",\"abcd\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.endwith,null,\"\"|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.endwith,\"\",null|| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,|string.endwith,null,null|| return this end end"), "false");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIndexOf() throws Exception {
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abc\",\"a\"|| return this end end"), "1");
		eq(ex("class Main do static this main() do |log.printn,|string.indexofn,\"abc\",\"a\", 1|| return this end end"), "1");
		eq(ex("class Main do static this main() do |log.printn,|string.indexofn,\"abc\",\"a\", -1|| return this end end"), "1");
		eq(ex("class Main do static this main() do |log.printn,|string.indexofn,\"abc\",\"a\", 2|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.indexofn,\"abc\",\"a\", 20|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abc\",\"b\"|| return this end end"), "2");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abc\",\"c\"|| return this end end"), "3");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abc\",\"ab\"|| return this end end"), "1");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abc\",\"bc\"|| return this end end"), "2");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abc\",\"abc\"|| return this end end"), "1");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abc\",\"d\"|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abc\",\"de\"|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abc\",\"bd\"|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abc\",\"\"|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abc\",\"ac\"|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abcccc\",\"c\"|| return this end end"), "3");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abcccc\",\"cc\"|| return this end end"), "3");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abcccc\",\"ccc\"|| return this end end"), "3");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abcccc\",\"cccc\"|| return this end end"), "3");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abcccc\",\"ccccc\"|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abcabc\",\"c\"|| return this end end"), "3");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,null,\"c\"|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,\"abcabc\",null|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.indexof,null,null|| return this end end"), "-1");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLastIndexOf() throws Exception {
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abc\",\"a\"|| return this end end"), "1");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abca\",\"a\"|| return this end end"), "4");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abc\",\"b\"|| return this end end"), "2");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abcb\",\"b\"|| return this end end"), "4");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abc\",\"c\"|| return this end end"), "3");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abcc\",\"c\"|| return this end end"), "4");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abc\",\"ab\"|| return this end end"), "1");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abcab\",\"ab\"|| return this end end"), "4");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abc\",\"bc\"|| return this end end"), "2");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abcbc\",\"bc\"|| return this end end"), "4");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abc\",\"abc\"|| return this end end"), "1");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abcabc\",\"abc\"|| return this end end"), "4");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abc\",\"d\"|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abc\",\"de\"|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abc\",\"bd\"|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abc\",\"\"|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abc\",\"ac\"|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abcccc\",\"c\"|| return this end end"), "6");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abcccc\",\"cc\"|| return this end end"), "5");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abcccc\",\"ccc\"|| return this end end"), "4");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abcccc\",\"cccc\"|| return this end end"), "3");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abcccc\",\"ccccc\"|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abcabc\",\"c\"|| return this end end"), "6");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,null,\"c\"|| return this end end"), "-1");
		eq(ex("class Main do static this main() do |log.printn,|string.lastindexof,\"abcabc\",null|| return this end end"), "-1");
		runtimeFail("class Main do static this main() do |log.prints,|string.lastindexof,null,null|| return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testToLowerCase() throws Exception {
		eq(ex("class Main do static this main() do |log.prints,|string.tolowercase,\"abc\"|| return this end end"), "abc");
		eq(ex("class Main do static this main() do |log.prints,|string.tolowercase,\"ABC\"|| return this end end"), "abc");
		eq(ex("class Main do static this main() do |log.prints,|string.tolowercase,\"aBc\"|| return this end end"), "abc");
		eq(ex("class Main do static this main() do |log.prints,|string.tolowercase,\"Abc\"|| return this end end"), "abc");
		eq(ex("class Main do static this main() do |log.prints,|string.tolowercase,\"abC\"|| return this end end"), "abc");
		eq(ex("class Main do static this main() do |log.prints,|string.tolowercase,\"ABc\"|| return this end end"), "abc");
		eq(ex("class Main do static this main() do |log.prints,|string.tolowercase,\"aBC\"|| return this end end"), "abc");
		eq(ex("class Main do static this main() do |log.prints,|string.tolowercase,\"\"|| return this end end"), "");
		eq(ex("class Main do static this main() do |log.prints,|string.tolowercase,\"a\"|| return this end end"), "a");
		eq(ex("class Main do static this main() do |log.prints,|string.tolowercase,\"A\"|| return this end end"), "a");
		eq(ex("class Main do static this main() do |log.prints,|string.tolowercase,null|| return this end end"), "null");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testToUpperCase() throws Exception {
		eq(ex("class Main do static this main() do |log.prints,|string.touppercase,\"abc\"|| return this end end"), "ABC");
		eq(ex("class Main do static this main() do |log.prints,|string.touppercase,\"ABC\"|| return this end end"), "ABC");
		eq(ex("class Main do static this main() do |log.prints,|string.touppercase,\"aBc\"|| return this end end"), "ABC");
		eq(ex("class Main do static this main() do |log.prints,|string.touppercase,\"Abc\"|| return this end end"), "ABC");
		eq(ex("class Main do static this main() do |log.prints,|string.touppercase,\"abC\"|| return this end end"), "ABC");
		eq(ex("class Main do static this main() do |log.prints,|string.touppercase,\"ABc\"|| return this end end"), "ABC");
		eq(ex("class Main do static this main() do |log.prints,|string.touppercase,\"aBC\"|| return this end end"), "ABC");
		eq(ex("class Main do static this main() do |log.prints,|string.touppercase,\"\"|| return this end end"), "");
		eq(ex("class Main do static this main() do |log.prints,|string.touppercase,\"a\"|| return this end end"), "A");
		eq(ex("class Main do static this main() do |log.prints,|string.touppercase,\"A\"|| return this end end"), "A");
		eq(ex("class Main do static this main() do |log.prints,|string.touppercase,null|| return this end end"), "null");
	}

}
