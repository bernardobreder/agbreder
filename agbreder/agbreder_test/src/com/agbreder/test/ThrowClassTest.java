package com.agbreder.test;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Testador de Compilador
 * 
 * @author bernardobreder
 */
public class ThrowClassTest extends BasicTest {

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testClass() throws Exception {
		ex("throw Throwable do end class Main do static this main() do return this end end");
		ex("throw Throwable do end throw Exception extends Throwable do end class Main do static this main() do return this end end");
		ex("throw Throwable do end class Main do static this main() do new Throwable() return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTry() throws Exception {
		ex("throw Throwable do end class Main do static this main() do try do end return this end end");
		ex("throw Throwable do end class Main do static this main() do try do num a = 1 end return this end end");
		ex("throw Throwable do end class Main do static this main() do try do end catch Throwable e do end return this end end");
		fail("throw Throwable do end class Main do static this main() do try do end catch Main e do end return this end end");
		ex("throw Throwable do end class Main do static this main() do try do end catch Throwable e do end catch Throwable e do end return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testThrow() throws Exception {
		ex("throw runtime Throwable do end class Main do static this main() do try do throw new Throwable() end catch Throwable e do end return this end end");
		fail("throw runtime Throwable do end class Main do static this main() do try do throw new Throwable() end return this end end",
				1);
		ex("throw runtime Throwable do end class Main do static this main() do try do try throw new Throwable() catch Throwable e do end end return this end end");
		ex("throw runtime Throwable do end class Main do static this main() do Throwable t = new Throwable() try do throw t end catch Throwable e do end return this end end");
		ex("throw runtime Throwable do end class Main do static this main() do try do throw new Throwable() end catch Throwable e do |log.printb,e!=null| end return this end end");
		ex("throw runtime Throwable do end class Main do static this main() do Throwable t = new Throwable() try do throw t end catch Throwable e do |log.printb,t==e| end return this end end");
		ex("throw runtime A do end throw runtime B do end class Main do static this main() do try do throw new A() end catch A e do |log.printb,e!=null| end catch B e do |log.printb,e!=null| end return this end end");
		ex("throw runtime A do end throw runtime B do end class Main do static this main() do try do throw new A() end catch A e do |log.printb,|object.classname,e|==\"A\"| end catch B e do |log.printb,|object.classname,e|==\"B\"| end return this end end");
		ex("throw runtime A do end throw runtime B do end class Main do static this main() do try do throw new B() end catch A e do |log.printb,|object.classname,e|==\"A\"| end catch B e do |log.printb,|object.classname,e|==\"B\"| end return this end end");
		fail("throw runtime A do end throw runtime B do end throw runtime C do end class Main do static this main() do try do throw new C() end catch A e do |log.printb,|object.classname,e|==\"A\"| end catch B e do |log.printb,|object.classname,e|==\"B\"| end return this end end",
				1);
		eq(ex("throw runtime A do end class Main do static this main() do try do Main.a() end catch A e do |log.printn,1| end return this end static this a () do throw new A() return this end end"),
				"1");
		eq(ex("throw runtime A do end class Main do static this main() do try do Main.a() end catch A e do |log.printn,1| end return this end static this a () do Main.b() return this end static this b () do throw new A() return this end end"),
				"1");
		eq(ex("throw runtime A do end class Main do static this main() do try do Main.a() |log.printn,2| end catch A e do |log.printn,1| end return this end static this a () do throw new A() return this end end"),
				"1");
		eq(ex("throw runtime A do end class Main do static this main() do try do |log.printn,3| Main.a() |log.printn,2| end catch A e do |log.printn,1| end return this end static this a () do throw new A() return this end end"),
				"31");
		eq(ex("throw runtime A do end class Main do static this main() do try do Main.a() end catch A e do |log.printn,1| end return this end static this a () do Main.b() return this end static this b () do throw new A() |log.printn,4| return this end end"),
				"1");
		eq(ex("throw runtime A do end class Main do static this main() do try do Main.a() end catch A e do |log.printn,1| end return this end static this a () do Main.b() return this end static this b () do |log.printn,4| throw new A() return this end end"),
				"41");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testThrows() throws Exception {
		ex("throw Throwable do end class Main do static this main() throw Throwable do try do throw new Throwable() end catch Throwable e do end return this end end");
		fail("throw Throwable do end class Main do static this main() do throw new Throwable() return this end end");
		fail("throw Throwable do end class Main do static this main() do try do throw new Throwable() end return this end end",
				1);
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHeadMethod() throws Exception {
		fail("throw Throwable do end class Main do static this main() do new Main() return this end this Main () throw Throwable return this end");
		ex("throw runtime Throwable do end class Main do static this main() do new Main() return this end this Main () throw Throwable do return this end end");
		ex("throw Throwable do end class Main do static this main() throw Throwable do Main.a() return this end static this a () throw Throwable do return this end end");
		fail("throw Throwable do end class Main do static this main() do Main.a() return this end static this a () throw Throwable do return this end end");
		ex("throw A do end throw B do end class Main do static this main() throw A do Main.a() return this end static this a () throw A do return this end end");
		fail("throw A do end throw B do end class Main do static this main() throw A do Main.a() return this end static this a () throw B do return this end end");
		runtimeFail("throw A do end throw B extends A do end class Main do static this main() throw A do Main.a() return this end static this a () throw B do throw B return this end end");
		fail("throw A do end throw B extends A do end class Main do static this main() throw B do Main.a() return this end static this a () do throw A return this end end");
	}

	@Test
	public void testNullPoint() throws Exception {
		eq(ex("class Main do this a () do return this end static this main () do ((Main)null).a() |log.printn,1| return this end end"),
				"1");
	}

}
