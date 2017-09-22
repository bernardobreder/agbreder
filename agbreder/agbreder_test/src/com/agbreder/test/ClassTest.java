package com.agbreder.test;

import junit.framework.Assert;

import org.junit.Test;

import com.agbreder.compiler.exception.AGBTokenException;

/**
 * Testador de Compilador
 * 
 * @author bernardobreder
 */
public class ClassTest extends BasicTest {

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void primitive() throws Exception {
		runtimeFail("class Main do end");
		ex("class Main do static this main() do ; return this end end");
		ex("class Main do static this main() do null return this end end");
		ex("class Main do static this main() do return this end end");
		ex("class Main do static this main() do 1 return this end end");
		ex("class Main do static this main() do 1.1 return this end end");
		ex("class Main do static this main() do true return this end end");
		ex("class Main do static this main() do false return this end end");
		ex("class Main do static this main() do \"a\" return this end end");
		ex("class Main do static this main() do 1+1 return this end end");
		fail("class Main do static this main() do true+1 return this end end");
		fail("class Main do static this main() do 1+true return this end end");
		ex("class Main do static this main() do 1-1 return this end end");
		fail("class Main do static this main() do true-1 return this end end");
		fail("class Main do static this main() do 1-true return this end end");
		ex("class Main do static this main() do 1*1 return this end end");
		fail("class Main do static this main() do true*1 return this end end");
		fail("class Main do static this main() do 1*true return this end end");
		ex("class Main do static this main() do 1/1 return this end end");
		fail("class Main do static this main() do true/1 return this end end");
		fail("class Main do static this main() do 1/true return this end end");
		ex("class Main do static this main() do true or true return this end end");
		ex("class Main do static this main() do true or false return this end end");
		ex("class Main do static this main() do false or true return this end end");
		ex("class Main do static this main() do false or false return this end end");
		ex("class Main do static this main() do true and true return this end end");
		ex("class Main do static this main() do true and false return this end end");
		ex("class Main do static this main() do false and true return this end end");
		ex("class Main do static this main() do false and false return this end end");
		fail("class Main do static this main() do true or 1 return this end end");
		fail("class Main do static this main() do 1 or true return this end end");
		fail("class Main do static this main() do true and 1 return this end end");
		fail("class Main do static this main() do 1 and true return this end end");
		fail("class Main do static this main() do 1.1.\n return this end end");
		ex("class Main do static this main() do new Main() == new Main() return this end end");
		ex("class Main do static this main() do new Main() != new Main() return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTernary() throws Exception {
		eq(ex("class Main do static this main() do |log.printn,1>2?1:2| return this end end"), "2");
		eq(ex("class Main do static this main() do |log.printn,1<2?1:2| return this end end"), "1");
		eq(ex("class Main do static this main() do |log.printn,Main.max(1,2)| return this end static num max(num a, num b) do return a > b ? a : b end end"), "2");
		eq(ex("class Main do static this main() do |log.printn,Main.max(2,1)| return this end static num max(num a, num b) do return a > b ? a : b end end"), "2");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNew() throws Exception {
		eq(ex("class Main do static this main() do new Main() return this end this Main () do |log.prints,\"a\"| return this end end"), "a");
		fail("class Main do static this main() do new Main().main() return this end this Main () do return this end end");
		fail("class Main extends object do static this main() do new Main().object() return this end this Main () do return this end end class object do end");
		eq(ex("class Main extends object do static this main() do new Main() return this end end class object do this object() do |log.prints,\"a\"| return this end end"), "a");
		eq(ex("class Main extends object do static this main() do new Main() return this end this Main () do super() |log.prints,\"b\"| return this end end class object do this object() do |log.prints,\"a\"| return this end end"), "ab");
		eq(ex("class Main extends abc do static this main() do new Main() return this end this Main () do super() |log.prints,\"c\"| return this end end class abc extends object do this abc() do super() |log.prints,\"b\"| return this end end class object do this object() do |log.prints,\"a\"| return this end end "), "abc");
		fail("class Main extends abc do static this main() do new Main() return this end this Main () do return this end end class abc extends object do this abc() do super() return this end end class object do this object() do return this end end ");
		fail("class Main extends abc do static this main() do new Main() return this end this Main () do super() return this end end class abc extends object do this abc() do return this end end class object do this object() do return this end end ");
		fail("class Main extends abc do static this main() do new Main() return this end this Main () do super() return this end end class abc extends object do this abc() do super() return this end end class object do this object() do super() return this end end ");
		fail("class Main extends object do static this main() do new Main() return this end this Main () do super() |log.prints,\"b\"| return this end end class object do this object() do super() |log.prints,\"a\"| return this end end");
		fail("class Main extends object do static this main() do new Main() return this end this Main () do |log.prints,\"b\"| return this end end class object do this object() do |log.prints,\"a\"| return this end end");
		fail("class Main extends object do static this main() do new Main() return this end this test () do super() |log.prints,\"b\"| return this end end class object do this object() do |log.prints,\"a\"| return this end end");
		fail("class Main extends object do static this main() do new Main() return this end this Main () do do end super() |log.prints,\"b\"| return this end end class object do this object() do |log.prints,\"a\"| return this end end");
		eq(ex("class Main extends object do static this main() do new Main() return this end this Main () do super(1) |log.prints,\"b\"| return this end end class object do this object(num a) do |log.printn,a| return this end end"), "1b");
		eq(ex("class Main extends object do static this main() do new Main(1,2) return this end this Main (num a, num b) do super(b) |log.printn,a| return this end end class object do this object(num a) do |log.printn,a| return this end end"), "21");
		fail("class Main extends object do static this main() do new Main(1,2) return this end end class object do this object(num a) do return this end end");
		fail("class Main extends object do static this main() do new Main(1,2) return this end end class object do this object(str a) do return this end end");
		fail("class Main extends object do static this main() do new Main(1,2) return this end this Main (num a, num b) do super(\"\") return this end end class object do this object(num a) do return this end end");
		fail("class Main do this Main (num a) do return this end static this main() do new Main() return this end end");
		eq(ex("class Main do str native this Main(str native) do this.native = native return this end static this main() do Main a = new Main(\"\") Main string = new Main(|object.classname,a|) |log.prints,string.native| return this end end"), "Main");
		eq(ex("class Main do this Main () do |log.printn,1| return this end static this main () do new Main() return this end end"), "1");
		eq(ex("class Main do this Main (num a) do |log.printn,a| return this end static this main () do new Main(1) return this end end"), "1");
		fail("class Main do this Main () do return this end static this main () do new Main(1) return this end end");
		fail("class Main do this Main (num a) do return this end static this main () do new Main() return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSuper() throws Exception {
		eq(ex("class Main extends MainA do static this main() do new Main() return this end this Main () do super() this.a() return this end this a () do |log.prints,\"main\"| return this end end class MainA extends MainB do this MainA() do super() return this end this a () do |log.prints,\"MainA\"| return this end end class MainB do this MainB () do return this end this a () do |log.prints,\"MainB\"| return this end end "), "main");
		eq(ex("class Main extends MainA do static this main() do new Main() return this end this Main () do super() super.a() return this end this a () do |log.prints,\"main\"| return this end end class MainA extends MainB do this MainA() do super() return this end this a () do |log.prints,\"MainA\"| return this end end class MainB do this MainB () do return this end this a () do |log.prints,\"MainB\"| return this end end "), "MainA");
		eq(ex("class Main extends MainA do static this main() do new Main() return this end this Main () do super() super.a() return this end this a () do |log.prints,\"main\"| return this end end class MainA do this MainA() do return this end this a () do return this end end "), "");
		eq(ex("class Main extends MainA do static this main() do new Main() return this end this Main () do super() super.a() return this end this a () do |log.prints,\"main\"| return this end end class MainA extends MainB do this MainA() do super() return this end this a () do return super.a() end end class MainB do this MainB () do return this end this a () do |log.prints,\"MainB\"| return this end end "), "MainB");
		fail("class Main extends MainA do static this main() do new Main() return this end this Main () do super() super.b() return this end this a () do |log.prints,\"main\"| return this end end class MainA extends MainB do this MainA() do super() return this end this a () do |log.prints,\"MainA\"| return this end end class MainB do this MainB () do return this end this a () do |log.prints,\"MainB\"| return this end end ");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNative() throws Exception {
		eq(ex("class Main do static this main() do |log.printn,1| return this end end"), "1");
		eq(ex("class Main do static this main() do |log.printn,1.1| return this end end"), "1.1");
		eq(ex("class Main do static this main() do |log.printn,1.11| return this end end"), "1.11");
		eq(ex("class Main do static this main() do |log.printn,1.111| return this end end"), "1.111");
		eq(ex("class Main do static this main() do |log.printn,1.1111| return this end end"), "1.1111");
		eq(ex("class Main do static this main() do |log.printn,1.11111| return this end end"), "1.11111");
		eq(ex("class Main do static this main() do |log.printb,true| return this end end"), "true");
		eq(ex("class Main do static this main() do |log.prints,\"a\"| return this end end"), "a");
		eq(ex("class Main do static this main() do |log.prints,\"a\"+\"b\"| return this end end"), "ab");
		eq(ex("class Main do static this main() do |log.prints,1+\"b\"| return this end end"), "1b");
		eq(ex("class Main do static this main() do |log.prints,\"a\"+1| return this end end"), "a1");
		fail("class Main do static this main() do |log.prints,1| return this end end");
		ex("class Main do static this main() do |vm.half,0| return this end end");
		ex("class Main do static this main() do |vm.gc| return this end end");
		eq(ex("class Main do static this main() do Main a = new Main() str value = |object.classname,a| |log.prints,value| return this end end"), "Main");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNot() throws Exception {
		eq(ex("class Main do static this main() do |log.printb,!true| return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,!false| return this end end"), "true");
		fail("class Main do static this main() do |log.printb,!1| return this end end");
		fail("class Main do static this main() do |log.printb,!\"\"| return this end end");
		fail("class Main do static this main() do |log.printb,!null| return this end end");
		fail("class Main do static this main() do |log.printb,!new Main()| return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCall() throws Exception {
		ex("class Main do static this main() do new Main().a() return this end this a() do return this end end");
		eq(ex("class Main do static this main() do new Main().a() return this end this a() do |log.printn,1| return this end end"), "1");
		ex("class Main do static this main() do new Main().a(1) return this end this a(num a) do return this end end");
		eq(ex("class Main do static this main() do new Main().a(1) return this end this a(num a) do |log.printn,a| return this end end"), "1");
		eq(ex("class Main do static this main() do Main.a(1) return this end static this a(num a) do |log.printn,a| return this end end"), "1");
		eq(ex("class Main do static this main() do Main.a(1).a(2).a(3) return this end static this a(num a) do |log.printn,a| return this end end"), "123");
		eq(ex("class Main do static this main() do Main.a(1,2,3) return this end static this a(num a,num b,num c) do |log.printn,a||log.printn,b||log.printn,c| return this end end"), "123");
		fail("class Main do static this main() do new Main().a() return this end this b() do return this end end");
		fail("class Main do static this main () do Main.b(null) return this end static this b(num a) do return this end end");
		eq(ex("class Main do static this main() do Main a = new Main() |log.printb,a == a.a().a()| return this end this a() do return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,Main.a() != null| return this end static Main a() do return new Main() end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,Main.a() != null| return this end static Main a() do return new Main().b() end this b () do return this end end"), "true");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFor() throws Exception {
		eq(ex("class Main do static this main() do for (num n = 0; n < 10 ; n++) do |log.printn,n| end return this end end"), "0123456789");
		eq(ex("class Main do static this main() do for (num n = 9; n >= 0 ; n--) do |log.printn,n| end return this end end"), "9876543210");
		eq(ex("class Main do static this main() do for (num n = 0; n > 0 ; n--) do |log.printn,n| end return this end end"), "");
		ex("class Main do static this main() do for (num n = 0; n < 1 ; n++) do end for (num n = 0; n < 1 ; n++) do end return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testForN() throws Exception {
		eq(ex("class Main do static this main() do for n = 0, 9 do |log.printn,n| end return this end end"), "0123456789");
		ex("class Main do static this main() do for n = 0, 1 do end for n = 0, 1 do end return this end end");
		// eq(ex("class Main do static this main() do "
		// + "for n = 9, 0 do |log.printn,n| end return this end end"),
		// "9876543210");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testField() throws Exception {
		eq(ex("class Main do num a num b num c static this main() do Main m = new Main() m.a=1 m.b=2 m.c=3 |log.printn,Main.a| |log.printn,Main.b| |log.printn,Main.c| return this end end"), "123");
		eq(ex("class Main extends object do static this main() do Main m = new Main() m.a=1 m.b=2 m.c=3 |log.printn,Main.a| |log.printn,Main.b| |log.printn,Main.c| return this end end class object do num a num b num c end"), "123");
		eq(ex("class Main extends object do num c num d static this main() do Main m = new Main() m.a=1 m.b=2 m.c=3 m.d=4 |log.printn,Main.a| |log.printn,Main.b| |log.printn,Main.c| |log.printn,Main.d| return this end end class object do num a num b end"), "1234");
		fail("class Main do num a static this main() do new Main().b return this end end");
		fail("class Main do num a static this main() do new Main().a.b return this end end");
		eq(ex("class Main do num a static this main() do |log.printn,new Main().a| return this end this Main () do this.a = 1 return this end end"), "1");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSwitch() throws Exception {
		fail("class Main do static this main() do switch 1 end return this end end");
		eq(ex("class Main do static this main() do switch 1 do case 1 do |log.printn,1| end end return this end end"), "1");
		eq(ex("class Main do static this main() do switch 1 do case 2 do |log.printn,1| end end return this end end"), "");
		eq(ex("class Main do static this main() do switch 1 do case 1 do |log.printn,1| end case 2 do |log.printn,2| end end return this end end"), "1");
		eq(ex("class Main do static this main() do switch 2 do case 1 do |log.printn,1| end case 2 do |log.printn,2| end end return this end end"), "2");
		eq(ex("class Main do static this main() do switch 2 do case 1 do |log.printn,1| end case 2 do |log.printn,2| end case 3 do |log.printn,3| end end return this end end"), "2");
		eq(ex("class Main do static this main() do switch 1 do case 1 do |log.printn,1| end case 2 do |log.printn,2| end case 3 do |log.printn,3| end end return this end end"), "1");
		eq(ex("class Main do static this main() do switch 3 do case 1 do |log.printn,1| end case 2 do |log.printn,2| end case 3 do |log.printn,3| end end return this end end"), "3");
		eq(ex("class Main do static this main() do switch 4 do case 1 do |log.printn,1| end case 2 do |log.printn,2| end case 3 do |log.printn,3| end else do |log.printn,4| end end return this end end"), "4");
		eq(ex("class Main do static this main() do switch 1 do case 1 do |log.printn,1| end case 2 do |log.printn,2| end case 3 do |log.printn,3| end else do |log.printn,4| end end return this end end"), "1");
		eq(ex("class Main do static this main() do switch 2 do case 1 do |log.printn,1| end case 2 do |log.printn,2| end case 3 do |log.printn,3| end else do |log.printn,4| end end return this end end"), "2");
		eq(ex("class Main do static this main() do switch 3 do case 1 do |log.printn,1| end case 2 do |log.printn,2| end case 3 do |log.printn,3| end else do |log.printn,4| end end return this end end"), "3");
		eq(ex("class Main do static this main() do switch 4 do case 1 do |log.printn,1| end else do |log.printn,4| end end return this end end"), "4");
		fail("class Main do static this main() do switch 4 do else do |log.printn,4| end end return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIncDec() throws Exception {
		eq(ex("class Main do static this main() do num a = 5 |log.printn,a|return this end end"), "5");
		eq(ex("class Main do static this main() do num a = 5 |log.printn,a++| |log.printn,a|return this end end"), "56");
		eq(ex("class Main do static this main() do num a = 5 |log.printn,a--| |log.printn,a|return this end end"), "54");
		eq(ex("class Main do static this main() do num a = 5 |log.printn,++a| |log.printn,a|return this end end"), "66");
		eq(ex("class Main do static this main() do num a = 5 |log.printn,--a| |log.printn,a|return this end end"), "44");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAsk() throws Exception {
		eq(ex("class Main do static this main() do |log.printn,1==1?1:2|return this end end"), "1");
		eq(ex("class Main do static this main() do |log.printn,1==2?1:2|return this end end"), "2");
		fail("class Main do static this main() do |log.printn,1?1:2|return this end end");
		fail("class Main do static this main() do |log.printn,1==1?1:\"a\"|return this end end");
		ex("class Main extends object do static this main() do 1==1?new Main():new object()return this end end class object do end");
		ex("class Main extends object do static this main() do 1==1?new object():new Main()return this end end class object do end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCompare() throws Exception {
		eq(ex("class Main do static this main() do |log.printb,1==1|return this end end"), "true");
		fail("class Main do static this main() do |log.printb,0==null|return this end end");
		eq(ex("class Main do static this main() do |log.printb,1!=1|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,true==true|return this end end"), "true");
		fail("class Main do static this main() do |log.printb,false==null|return this end end");
		fail("class Main do static this main() do |log.printb,null==false|return this end end");
		eq(ex("class Main do static this main() do |log.printb,true==false|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,true!=true|return this end end"), "false");
		fail("class Main do static this main() do |log.printb,true!=null|return this end end");
		eq(ex("class Main do static this main() do |log.printb,true!=false|return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,1==true|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,true==false|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,\"a\"==true|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,\"a\"==1|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,1!=true|return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,\"a\"!=true|return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,\"a\"!=1|return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,1>1|return this end end"), "false");
		fail("class Main do static this main() do |log.printb,1>null|return this end end");
		fail("class Main do static this main() do |log.printb,1>=null|return this end end");
		fail("class Main do static this main() do |log.printb,1<null|return this end end");
		fail("class Main do static this main() do |log.printb,1<=null|return this end end");
		eq(ex("class Main do static this main() do |log.printb,1>=1|return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,1<1|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,1<=1|return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,2>1|return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,2>=1|return this end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,2<1|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,2<=1|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,\"\">1|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,\"\">=1|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,\"\"<1|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,\"\"<=1|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,1>\"\"|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,1>=\"\"|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,1<\"\"|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,1<=\"\"|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,true>1|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,true>=1|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,true<1|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,true<=1|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,new Main()>1|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,new Main()>=1|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,new Main()<1|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,new Main()<=1|return this end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,null == Main.a()| return this end static Main a () do return null end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,Main.a() == null| return this end static Main a () do return null end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,Main.a() == Main.a()| return this end static Main a () do return null end end"), "true");
		eq(ex("class Main do static this main() do |log.printb,null != Main.a()| return this end static Main a () do return null end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,Main.a() != null| return this end static Main a () do return null end end"), "false");
		eq(ex("class Main do static this main() do |log.printb,Main.a() != Main.a()| return this end static Main a () do return null end end"), "false");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIf() throws Exception {
		eq(ex("class Main do static this main() do if true do |log.printn,1| end return this end end"), "1");
		eq(ex("class Main do static this main() do if false do |log.printn,1| end return this end end"), "");
		eq(ex("class Main do static this main() do if true do |log.printn,1| end else do |log.printn,2| end return this end end"), "1");
		eq(ex("class Main do static this main() do if false do |log.printn,1| end else do |log.printn,2| end return this end end"), "2");
		ex("class Main do static this main() do if true do return this end return this end end");
		fail("class Main do static this main() do if false do |log.printn,1| return this end end end");
		fail("class Main do static this main() do if true do return this end else do end end end");
		ex("class Main do static this main() do if true do return this end else do return this end return this end end");
		ex("class Main do static this main() do if false do return this end else do return this end return this end end");
		fail("class Main do static this main() do if false do end else do return this end end end");
		fail("class Main do static this main() do if false do end else do end end end");
		ex("class Main do static this main() do if false do return this end else do return this end end end");
		fail("class Main do static this main() do if false do end else return this end end");
		fail("class Main do static this main() do if false return this else do end end end");
		ex("class Main do static this main() do if false do return this end else do if true do return this end else do return this end end end end");
		fail("class Main do static this main() do if false do return this end else do if true do return this end end end end");
		fail("class Main do static this main() do if false return this else if true return this else do end end end");
		fail("class Main do static this main() do if false return this else if true do end else return this end end");
		ex("class Main do static this main() do if false do return this end else do if true do return this end else do return this end end end end");
		eq(ex("class Main do static this main() do if true==1 do return this end else do |log.printn,1| end return this end end"), "1");
		fail("class Main do static this main() do if null==1 do end else do |log.printn,1| end return this end end");
		fail("class Main do static this main() do if 1==null do end else do |log.printn,1| end return this end end");
		eq(ex("class Main do static this main() do if null==null do |log.printn,1| end return this end end"), "1");
		fail("class Main do static this main() do if null!=1 do |log.printn,1| end return this end end");
		fail("class Main do static this main() do if 1!=null do |log.printn,1| end return this end end");
		eq(ex("class Main do static this main() do if null!=null do end else do |log.printn,1| end return this end end"), "1");
		eq(ex("class Main do static this main() do if true!=1 do |log.printn,1| end return this end end"), "1");
		fail("class Main do static this main() do if null!=1 do |log.printn,1| end return this end end");
		fail("class Main do static this main() do num a = null return this end end");
		fail("class Main do static this main() do if 1 do end return this end end");
		fail("class Main do static this main() do if 0 do end return this end end");
		fail("class Main do static this main() do if \"\" do end return this end end");
		fail("class Main do static this main() do if null do end return this end end");
		fail("class Main do static this main() do if new Main() do end return this end end");
		fail("class Main do static this main() do if (true do end return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCast() throws Exception {
		ex("class Main extends object do static this main() do (object)new Main() return this end end class object do end");
		ex("class Main do static this main() do (Main)null return this end end");
		ex("class Main extends object do num a static this main() do ((object)new Main()).b=1 return this end end class object do num b end");
		fail("class Main extends object do num a static this main() do ((object)new Main()).a=1return this end end class object do num b end");
		runtimeFail("class Main extends object do static this main() do (Main) new object() return this end end class object do end");
		fail("class Main extends object do static this main() do (Main)1 return this end end class object do end");
		fail("class Main extends object do static this main() do (str)1 return this end end");
		fail("class Main extends object do static this main() do (bool)1 return this end end");
		fail("class Main do static this main() do (num)1 return this end end");
		fail("class Main do static this main() do (str)\"\" return this end end");
		fail("class Main do static this main() do (bool)\"\" return this end end");
		fail("class Main do static this main() do (num)\"\" return this end end");
		fail("class Main do static this main() do (str)true return this end end");
		fail("class Main do static this main() do (bool)true return this end end");
		fail("class Main do static this main() do (num)true return this end end");
		fail("class Main do static this main() do (str)null return this end end");
		fail("class Main do static this main() do (bool)null return this end end");
		fail("class Main do static this main() do (num)null return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRepeat() throws Exception {
		eq(ex("class Main do static this main() do repeat do |log.printn,1| end while false return this end end"), "1");
		eq(ex("class Main do static this main() do num a = 2 repeat do a-- |log.printn,1| end while a >= 1 return this end end"), "11");
		fail("class Main do static this main() do repeat do end while 1 return this end end");
		fail("class Main do static this main() do repeat do end while 0 return this end end");
		fail("class Main do static this main() do repeat do end while \"\" return this end end");
		fail("class Main do static this main() do repeat do end while null return this end end");
		fail("class Main do static this main() do repeat do end while new Main() return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testWhile() throws Exception {
		eq(ex("class Main do static this main() do while false do |log.printn,1| end return this end end"), "");
		eq(ex("class Main do static this main() do num a = 2 while a >= 1 do a-- |log.printn,1| end return this end end"), "11");
		fail("class Main do static this main() do while 1 do end return this end end");
		fail("class Main do static this main() do while 0 do end return this end end");
		fail("class Main do static this main() do while \"\" do end return this end end");
		fail("class Main do static this main() do while null do end return this end end");
		fail("class Main do static this main() do while new Main() do end return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBreak() throws Exception {
		eq(ex("class Main do static this main() do for (;;) do break end |log.printn,1| return this end end"), "1");
		eq(ex("class Main do static this main() do for (;;) do 1==1 break 1==1 end |log.printn,1| return this end end"), "1");
		eq(ex("class Main do static this main() do for (;;) do 1==1 break |log.printn,1| 1==1 end |log.printn,1| return this end end"), "1");
		eq(ex("class Main do static this main() do for (;;) do 1==1 for (;;) do break end |log.printn,3| break |log.printn,2| 1==1 end |log.printn,1| return this end end"), "31");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAssign() throws Exception {
		eq(ex("class Main do static this main() do num a = 1 |log.printn,a| return this end end"), "1");
		eq(ex("class Main do static this main() do num a = 1 num b = 2 num c = 3 |log.printn,a||log.printn,b||log.printn,c| return this end end"), "123");
		eq(ex("class Main do static this main() do num a = 1 num b = 2 a = b = 3 |log.printn,a| |log.printn,b| return this end end"), "33");
		fail("class Main do static this main() do num a = true |log.printn,a| return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNegative() throws Exception {
		eq(ex("class Main do static this main() do |log.printn,-1| return this end end"), "-1");
		fail("class Main do static this main() do |log.printn,-true| return this end end");
		fail("class Main do static this main() do |log.printn,-\"\"| return this end end");
		fail("class Main do static this main() do |log.printn,-new Main()| return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIdentify() throws Exception {
		fail("class Main do static this main() do |log.printn,a| return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPrimtive() throws Exception {
		fail("class Main do static this main() do new Main().a().a() return this end str a () return \"\" end");
		fail("class Main do static this main() do new Main().a().a() return this end num a () return 1 end");
		fail("class Main do static this main() do new Main().a().a() return this end bool a () return true end");
		fail("class Main do static this main() do new Main().a().a() return this end obj a () return |array.alloc,1| end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReturn() throws Exception {
		fail("class Main do static this main() do return this end str a () return this end");
		fail("class Main do static this main() do return this end num a () return this end");
		ex("class Main do static this main() do return this end obj a () do return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeclare() throws Exception {
		eq(ex("class Main do static this main() do num a = 1 num b = 2 num c = 3 |log.printn,a| |log.printn,b| |log.printn,c| return this end end"), "123");
		fail("class Main do static this main() do |log.printn,a| num a = 1 return this end end");
		fail("class Main do static this main() do num a = 1 num a = 2 return this end end");
		fail("class Main do static this main() do num a = 1 do num a = 2 end return this end end");
		fail("class Main do static this main() do return this end static this a (num a) do num a = 1 return this end end");
		fail("class Main do static this main() do return this end static this a (num a) do do num a = 1 end return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSum() throws Exception {
		eq(ex("class Main do static this main() do |log.prints,\"a\"+\"-\"+\"b\"+\"/\"+\"c\"+\":\"+\"d\"| return this end end"), "a-b/c:d");
		eq(ex("class Main do static this main() do |log.printn,5 % 2| return this end end"), "1");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMethod() throws Exception {
		fail("class Main do static this main() do return this end this a(A a) do return this end end");
		ex("class Main do static this main() do return this end this a(Main a) do return this end end");
		fail("class Main do static this main() do return this end A a() do return null end end");
		ex("class Main do static this main() do return this end Main a() do return null end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testShiftLeft() throws Exception {
		eq(ex("class Main do static this main() do num a = 1 |log.printn,0+(a<<1)| return this end end"), "2");
		eq(ex("class Main do static this main() do |log.printn,0+(1<<1)| return this end end"), "2");
		eq(ex("class Main do static this main() do |log.printn,1<<1| return this end end"), "2");
		eq(ex("class Main do static this main() do |log.printn,1<<2| return this end end"), "4");
		eq(ex("class Main do static this main() do |log.printn,1<<3| return this end end"), "8");
		fail("class Main do static this main() do |log.printn,1<<true| return this end end");
		fail("class Main do static this main() do |log.printn,true<<1| return this end end");
		fail("class Main do static this main() do |log.printn,1<<new Main()| return this end end");
		fail("class Main do static this main() do |log.printn,new Main()<<1| return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testShiftRight() throws Exception {
		eq(ex("class Main do static this main() do |log.printn,8>>1| return this end end"), "4");
		eq(ex("class Main do static this main() do |log.printn,8>>2| return this end end"), "2");
		eq(ex("class Main do static this main() do |log.printn,8>>3| return this end end"), "1");
		fail("class Main do static this main() do |log.printn,1>>true| return this end end");
		fail("class Main do static this main() do |log.printn,true>>1| return this end end");
		fail("class Main do static this main() do |log.printn,1>>new Main()| return this end end");
		fail("class Main do static this main() do |log.printn,new Main()>>1| return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAndBit() throws Exception {
		eq(ex("class Main do static this main() do |log.printn,8 andbit 1| return this end end"), "0");
		eq(ex("class Main do static this main() do |log.printn,9 andbit 1| return this end end"), "1");
		eq(ex("class Main do static this main() do |log.printn,10 andbit 2| return this end end"), "2");
		eq(ex("class Main do static this main() do |log.printn,8 andbit 2| return this end end"), "0");
		fail("class Main do static this main() do |log.printn,1 andbit true| return this end end");
		fail("class Main do static this main() do |log.printn,true andbit 1| return this end end");
		fail("class Main do static this main() do |log.printn,1 andbit new Main()| return this end end");
		fail("class Main do static this main() do |log.printn,new Main() andbit 1| return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testOrBit() throws Exception {
		eq(ex("class Main do static this main() do |log.printn,8 orbit 1| return this end end"), "9");
		eq(ex("class Main do static this main() do |log.printn,9 orbit 1| return this end end"), "9");
		eq(ex("class Main do static this main() do |log.printn,10 orbit 2| return this end end"), "10");
		eq(ex("class Main do static this main() do |log.printn,8 orbit 2| return this end end"), "10");
		fail("class Main do static this main() do |log.printn,1 orbit true| return this end end");
		fail("class Main do static this main() do |log.printn,true orbit 1| return this end end");
		fail("class Main do static this main() do |log.printn,1 orbit new Main()| return this end end");
		fail("class Main do static this main() do |log.printn,new Main() orbit 1| return this end end");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testClass() throws Exception {
		try {
			ex("class Main do num a num a end");
			Assert.fail();
		} catch (AGBTokenException.AGBDuplicateFieldTokenException e) {
		}
		try {
			ex("class Main do num a () do return 1 end num a () do return 1 end end");
			Assert.fail();
		} catch (AGBTokenException.AGBDuplicateMethodTokenException e) {
		}
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFunction() throws Exception {
		eq(ex("class Main do static this test() do Main a = null function num () (Main a = a) do return 1 end return this end static this main () do return this end end"), "");
		eq(ex("class Main do static this test(Main a) do function num () (Main a = a) do return 1 end return this end static this main () do return this end end"), "");
		eq(ex("class Main do static this main () do Main a = new Main() function str (num, num) func = function str (num a, num b) (Main c = a) do |log.prints,|object.classname,c|| return null end func(1,2) return this end end"), "Main");
		eq(ex("class Main do static this main () do Main a = new Main() function str (num, num) func = function str (num a, num b) (Main c = null) do |log.printb,c==null| return null end func(1,2) return this end end"), "true");
		eq(ex("class Main do static this main () do Main a = new Main() function str (num, num) func = function str (num a, num b) (Main c = a, Main d = null) do |log.prints,|object.classname,c|| |log.printb,d==null| return null end func(1,2) return this end end"), "Maintrue");
		eq(ex("class Main do static this main () do Main a = new Main() function str (num, num) func = function str (num a, num b) (Main c = a, Main d = null, Main e = a) do |log.prints,|object.classname,c|| |log.printb,d==null| |log.prints,|object.classname,c|| return null end func(1,2) return this end end"), "MaintrueMain");
		eq(ex("class Main do static this main () do Main a = new Main() function str (num, num) func = function str (num a, num b) (Main c = a, Main d = null, num e = 1) do |log.prints,|object.classname,c|| |log.printb,d==null| |log.printn,e| return null end func(1,2) return this end end"), "Maintrue1");
		eq(ex("class Main do static this main () do Main a = new Main() function str (num, num) func = function str (num a, num b) (Main c = a, Main d = null, num e = 1) do |log.prints,|object.classname,c|| |log.printb,d==null| |log.printn,e| |log.printn,a| |log.printn,b| return null end func(2,3) return this end end"), "Maintrue123");
		eq(ex("class Main do static this main () do Main a = new Main() function str () func = function str () (Main c = a, Main d = null, num e = 1) do |log.prints,|object.classname,c|| |log.printb,d==null| |log.printn,e| return null end func() return this end end"), "Maintrue1");
		fail("class Main do static this main () do Main a = new Main() function str () func = function str () (Main c = a, Main d = null, num e = 1) do |log.prints,|object.classname,c|| |log.printb,d==null| |log.printn,e| return null end func(1,2) return this end end");
		fail("class Main do static this main () do Main a = new Main() function str () func = function str () (Main c = a, Main d = null, num e = 1) do |log.prints,|object.classname,c|| |log.printb,d==null| |log.printn,e| return null end func(2) return this end end");
		fail("class Main do static this main () do Main a = new Main() function str () func = function str () (Main c = a, Main d = null, num e = 1) do |log.prints,|object.classname,c|| |log.printb,d==null| |log.printn,e| return null end a(1,2) return this end end");
		eq(ex("class Main do static this main () do Main a = new Main() function str (num, num) func = (function str (num, num)) function str (num a, num b) (Main c = a) do |log.prints,|object.classname,c|| return null end func(1,2) return this end end"), "Main");
		eq(ex("class Main do static this main () do function this () func = function this () () do return this end |log.printb,func() == func| return this end end"), "true");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNumArray() throws Exception {
		eq(ex("class Main do static this main () do num[] a = null return this end end"), "");
		fail("class Main do static this main () do num[] a = 1 return this end end");
		fail("class Main do static this main () do num[] a = true return this end end");
		ex("class Main do static this main () do num[] a = |nums.alloc,10| return this end end");
		eq(ex("class Main do static this main () do |log.printn,|nums.size,|nums.alloc,10||| return this end end"), "10");
		eq(ex("class Main do static this main () do num[] a = |nums.alloc,10| |nums.set,a,1,10| |log.printn,|nums.get,a,1|| return this end end"), "10");
		eq(ex("class Main do static this main () do num[] a = |nums.alloc,10| |log.printn,|nums.get,a,1|| return this end end"), "0");
		eq(ex("class Main do static this main () do num[] a = |nums.alloc,10| |log.printn,|nums.get,a,0|| return this end end"), "0");
		eq(ex("class Main do static this main () do num[] a = |nums.alloc,10| |log.printn,|nums.get,a,11|| return this end end"), "0");
		eq(ex("class Main do static this main () do num[] a = |nums.alloc,10| |nums.sets,a,2,5,10| |nums.sets,a,6,9,5| |log.printn,|nums.get,a,1|| |log.printn,|nums.get,a,2|| |log.printn,|nums.get,a,5|| |log.printn,|nums.get,a,6|| |log.printn,|nums.get,a,9|| |log.printn,|nums.get,a,10|| return this end end"), "01010550");
		eq(ex("class Main do static this main () do num[] a = |nums.alloc,10| num[] b = |nums.alloc,10| |nums.sets,a,1,10,5| |nums.copy,a,5,10,b,5| |log.printn,|nums.get,b,1|| |log.printn,|nums.get,b,4|| |log.printn,|nums.get,b,5|| |log.printn,|nums.get,b,9|| |log.printn,|nums.get,b,10|| return this end end"), "00555");
		eq(ex("class Main do static this main () do num[] a = |nums.alloc,10| num[] b = |nums.alloc,10| |nums.sets,a,1,10,5| |nums.copy,a,6,8,b,2| |log.printn,|nums.get,b,1|| |log.printn,|nums.get,b,2|| |log.printn,|nums.get,b,3|| |log.printn,|nums.get,b,4|| |log.printn,|nums.get,b,5|| return this end end"), "05550");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBoolArray() throws Exception {
		eq(ex("class Main do static this main () do bool[] a = null return this end end"), "");
		fail("class Main do static this main () do bool[] a = 1 return this end end");
		ex("class Main do static this main () do bool[] a = |bools.alloc,10| return this end end");
		eq(ex("class Main do static this main () do |log.printn,|bools.size,|bools.alloc,10||| return this end end"), "10");
		eq(ex("class Main do static this main () do bool[] a = |bools.alloc,10| |bools.set,a,1,true| |log.printb,|bools.get,a,1|| return this end end"), "true");
		eq(ex("class Main do static this main () do bool[] a = |bools.alloc,10| |log.printb,|bools.get,a,1|| return this end end"), "false");
		eq(ex("class Main do static this main () do bool[] a = |bools.alloc,10| |log.printb,|bools.get,a,0|| return this end end"), "false");
		eq(ex("class Main do static this main () do bool[] a = |bools.alloc,10| |log.printb,|bools.get,a,11|| return this end end"), "false");
		eq(ex("class Main do static this main () do bool[] a = |bools.alloc,10| |bools.sets,a,2,5,true| |bools.sets,a,6,9,true| |log.printb,|bools.get,a,1|| |log.printb,|bools.get,a,2|| |log.printb,|bools.get,a,5|| |log.printb,|bools.get,a,6|| |log.printb,|bools.get,a,9|| |log.printb,|bools.get,a,10|| return this end end"), "falsetruetruetruetruefalse");
		eq(ex("class Main do static this main () do bool[] a = |bools.alloc,10| bool[] b = |bools.alloc,10| |bools.sets,a,1,10,true| |bools.copy,a,5,10,b,5| |log.printb,|bools.get,b,1|| |log.printb,|bools.get,b,4|| |log.printb,|bools.get,b,5|| |log.printb,|bools.get,b,9|| |log.printb,|bools.get,b,10|| return this end end"), "falsefalsetruetruetrue");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testByte() throws Exception {
		ex("class Main do static this main () do byte a = 1 return this end end");
		eq(ex("class Main do static this main () do byte a = 1 |log.printn, a| return this end end"), "1");
		ex("class Main do static this main () do byte[] a = |bytes.alloc,10| return this end end");
		ex("class Main do static this main () do byte[] a = |bytes.alloc,10| |bytes.set,a,1,1| return this end end");
		eq(ex("class Main do static this main () do byte[] a = |bytes.alloc,10| |bytes.set,a,1,1| |log.printn,|bytes.get,a,1|| return this end end"), "1");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBase64() throws Exception {
		eq(ex("class Main do static this main () do |log.prints,|string.base64_encode,\"Bernardo Breder\"|| return this end end"), "QmVybmFyZG8gQnJlZGVy");
		eq(ex("class Main do static this main () do |log.prints,|string.base64_encode,\"\"|| return this end end"), "");
		eq(ex("class Main do static this main () do |log.prints,|string.base64_encode,null|| return this end end"), "");
		eq(ex("class Main do static this main () do |log.prints,|string.base64_decode,\"QmVybmFyZG8gQnJlZGVy\"|| return this end end"), "Bernardo Breder");
		eq(ex("class Main do static this main () do |log.prints,|string.base64_decode,\"\"|| return this end end"), "");
		eq(ex("class Main do static this main () do |log.prints,|string.base64_decode,null|| return this end end"), "");
		eq(ex("class Main do static this main () do |log.prints,|string.base64_decode,\"abc\"|| return this end end"), "");
	}

	/**
	 * Testador
	 * 
	 * @throws Exception
	 */
	@Test
	public void testExpLeftRight() throws Exception {
		eq(ex("class Main do static this main () do num a = Main.a() + Main.b() return this end static num a () do |log.printn,1| return 1 end static num b () do |log.printn,2| return 2 end end"), "12");
	}

}
