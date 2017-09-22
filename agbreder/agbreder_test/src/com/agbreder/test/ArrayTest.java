package com.agbreder.test;

import org.junit.Test;

/**
 * Testador de Compilador
 * 
 * @author bernardobreder
 */
public class ArrayTest extends BasicTest {
	
	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNew() throws Exception {
		ex("class Main do static this main() do |array.alloc,10| return this end end");
		ex("class Main do static this main() do |array.alloc,0| return this end end");
		ex("class Main do static this main() do |array.alloc,-10| return this end end");
	}
	
	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLen() throws Exception {
		eq(
			ex("class Main do static this main() do |log.printn,|array.size,|array.alloc,10||| return this end end"),
			"0");
	}
	
	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAdd() throws Exception {
		eq(ex("class Main do static this main() do "
			+ "obj a = |array.alloc,10| |array.add,a,a| |log.printn,|array.size,a|| "
			+ "return this end end"), "1");
		eq(
			ex("class Main do static this main() do "
				+ "obj a = |array.alloc,10| |array.add,a,a| |array.add,a,a| |array.add,a,a| |log.printn,|array.size,a|| "
				+ "return this end end"), "3");
		eq(
			ex("class Main do static this main() do "
				+ "obj a = |array.alloc,1| |array.add,a,a| |array.add,a,a| |array.add,a,a| |array.add,a,a| |log.printn,|array.size,a|| "
				+ "return this end end"), "4");
	}
	
	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testInsert() throws Exception {
		eq(
			ex("class Main do static this main() do "
				+ "obj a = |array.alloc,1| |array.add,a,a| |array.add,a,a| |log.printn,|array.size,a|| |log.printb,|array.get,a,1|==null|"
				+ "|array.insert,a,1,null| |log.printn,|array.size,a|| |log.printb,|array.get,a,1|==null|"
				+ "return this end end"), "2false3true");
	}
	
	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGet() throws Exception {
		eq(
			ex("class Main do static this main() do "
				+ "obj a = |array.alloc,10| |array.add,a,a| |log.printb,|array.get,a,1|==a| "
				+ "return this end end"), "true");
		eq(
			ex("class Main do static this main() do "
				+ "obj a = |array.alloc,10| |array.add,a,a| |log.printb,|array.get,a,0|==null| "
				+ "return this end end"), "true");
		eq(
			ex("class Main do static this main() do "
				+ "obj a = |array.alloc,10| |array.add,a,a| |log.printb,|array.get,a,-1|==null| "
				+ "return this end end"), "true");
		eq(
			ex("class Main do static this main() do "
				+ "obj a = |array.alloc,10| |array.add,a,a| |log.printb,|array.get,a,2|==null| "
				+ "return this end end"), "true");
		eq(
			ex("class Main do static this main() do "
				+ "obj a = |array.alloc,1| |array.add,a,a| |array.add,a,a| |log.printb,|array.get,a,2|==a| "
				+ "return this end end"), "true");
	}
	
	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSet() throws Exception {
		eq(
			ex("class Main do static this main() do "
				+ "obj a = |array.alloc,10| |array.add,a,a| |log.printb,|array.get,a,1|==a|"
				+ "|array.set,a,1,null| |log.printb,|array.get,a,1|==a| |log.printb,|array.get,a,1|==null| "
				+ "return this end end"), "truefalsetrue");
	}
	
	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRemove() throws Exception {
		eq(
			ex("class Main do static this main() do "
				+ "obj a = |array.alloc,10| |array.add,a,a| |array.add,a,a| |log.printb,|array.get,a,1|==a| |log.printn,|array.size,a||"
				+ "|array.remove,a,1| |log.printn,|array.size,a|| |log.printb,|array.get,a,1|==a| "
				+ "return this end end"), "true21true");
	}
	
}
