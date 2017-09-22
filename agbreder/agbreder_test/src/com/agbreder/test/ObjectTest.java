package com.agbreder.test;

import org.junit.Test;

/**
 * Testador de Compilador
 * 
 * @author bernardobreder
 */
public class ObjectTest extends BasicTest {

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHashcode() throws Exception {
		ex("class Main do static this main() do |log.printn,|object.hashcode,new Main()|| return this end end", 0, false);
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testClassname() throws Exception {
		eq(ex("class Main do static this main() do |log.prints,|object.classname,new Main()|| return this end end"), "Main");
		eq(ex("class Main do static this main() do |log.prints,new Main().classname()| return this end str classname() do return |object.classname,this| end end"), "Main");
		fail("class Main do static this main() do |log.prints,new Main().classname()| return this end num classname() do return |object.classname,this| end end");
	}

}
