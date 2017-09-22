import java.io.IOException;

import org.junit.Test;

import com.agbreder.compiler.AGBCompiler;

/**
 * Testador lexical
 * 
 * @author Bernardo Breder
 */
public class CompileTest extends BasicTest {
	
	/**
	 * Construtor
	 * 
	 * @throws IOException
	 */
	public CompileTest() throws IOException {
		AGBCompiler.initFromWeb("localhost", 8080);
	}
	
	/**
	 * Testador
	 */
	@Test
	public void testInit() {
	}
	
	/**
	 * Testador
	 */
	@Test
	public void testExp() {
		eq(1d, ex("return 1"));
		eq(11d, ex("return 11"));
		eq(11d, ex("22 return 11"));
		eq(true, ex("return true"));
		eq(false, ex("return false"));
		eq(1.1d, ex("return 1.1"));
		eq("a", ex("return 'a'"));
		eq("é", ex("return 'é'"));
		eq("abc", ex("return 'abc'"));
		eq(true, ex("return true and true"));
		eq(false, ex("return true and false"));
		eq(false, ex("return false and true"));
		eq(false, ex("return false and false"));
		eq(true, ex("return true or true"));
		eq(true, ex("return true or false"));
		eq(true, ex("return false or true"));
		eq(false, ex("return false or false"));
		eq(true, ex("return 1==1"));
		eq(true, ex("return 1!=0"));
		eq(false, ex("return 1==0"));
		eq(true, ex("return 1>0"));
		eq(false, ex("return 1<0"));
		eq(true, ex("return 1>=0"));
		eq(false, ex("return 1<=0"));
		eq(true, ex("return 1==0 or 1==1"));
		eq(true, ex("return 1==1 or 1==0"));
		eq(false, ex("return 1==0 or 1==0"));
		eq(false, ex("return 1==0 and 1==1"));
		eq(false, ex("return 1==1 and 1==0"));
		eq(true, ex("return 1==1 and 1==1"));
		eq(true, ex("return 1==0 or 1==1"));
		eq(true, ex("return 'a' == 'a'"));
		eq(false, ex("return 'a' == 'b'"));
		eq(true, ex("return 'b' != 'a'"));
		eq(false, ex("return 'a' != 'a'"));
		eq(false, ex("return 'a' == 1"));
		eq(false, ex("return 'a' == false"));
		eq(false, ex("return false == 'a'"));
		eq(false, ex("return 1 == 'a'"));
		eq(false, ex("return false == true"));
		eq(true, ex("return false == false"));
		eq(true, ex("return true == true"));
		eq(false, ex("return true == 1"));
		eq(false, ex("return 1 == true"));
		// eq(1d, ex("return true ? 1 : 0"));
		// eq(0d, ex("return false ? 1 : 0"));
		// eq(2d, ex("return 1==1 ? 1==1 ? 2 : 1 : 0"));
		// eq("ab", ex("return 'a' + 'b'"));
		// eq("a1", ex("return 'a' + 1"));
		// eq("a11", ex("return 'a' + 1 + 1"));
		// eq("a2", ex("return 'a' + (1+1)"));
		eq(2d, ex("return 1+1"));
		eq(12d, ex("return 10+2"));
		eq(3d, ex("return 1+1+1"));
		eq(0d, ex("return 1-1"));
		eq(-1d, ex("return 1-1-1"));
		eq(6d, ex("return 2*3"));
		eq(24d, ex("return 2*3*4"));
		eq(8d, ex("return 2*3+2"));
		eq(14d, ex("return 2+3*4"));
		eq(5d, ex("return 10/2"));
		eq(4d, ex("return 16/2/2"));
		eq(true, ex("return 1==1"));
		eq(false, ex("return 1==2"));
		eq(false, ex("return 1!=1"));
		eq(true, ex("return 1!=2"));
		eq(false, ex("return 1>1"));
		eq(true, ex("return 2>1"));
		eq(true, ex("return 1>=1"));
		eq(false, ex("return 1>=2"));
		eq(false, ex("return 1<1"));
		eq(true, ex("return 1<2"));
		eq(true, ex("return 1<=1"));
		eq(false, ex("return 2<=1"));
		eq(1d, ex("num a = 1 return 1"));
		eq(1d, ex("num a = 1 return a"));
		eq(true, ex("num a = 0 return a == 0"));
		eq(2d, ex("num a = 1 a=2 return a"));
		eq(1d, ex("if 1==1 return 1 return 0"));
		eq(1d, ex("num a = 0 if a == 0 a = 1 return a"));
		eq(0d, ex("num a = 5 while a > 0 a = a - 1 return a"));
		eq(0d, ex("num a = 5 repeat a = a - 1 while a > 0 return a"));
	}
	
	/**
	 * Testa a estrutura de for
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeclare() throws Exception {
		eq(1d, ex("num n = 1 return n"));
		eq(2d, ex("num n = 1+1 return n"));
		eq(2d, ex("num n = 1 n = 2 return n"));
		eq(2d, ex("num n = 1 num m = 2 return m"));
		eq(1d, ex("num n = 1 num m = 2 return n"));
		eq(3d, ex("num n = 1 num m = 2 num p = 3 return p"));
		eq(2d, ex("num n = 1 num m = 2 num p = 3 return m"));
		eq(1d, ex("num n = 1 num m = 2 num p = 3 return n"));
		// fail("num n = 1 return m"));
		// fail("return n"));
	}
	
	/**
	 * Testa a estrutura de for
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIncDec() throws Exception {
		eq(1d, ex("num n = 1 return n++"));
		eq(2d, ex("num n = 1 return ++n"));
		eq(1d, ex("num n = 1 return n--"));
		eq(0d, ex("num n = 1 return --n"));
		eq(3d, ex("num n = 1 return n++ + n++"));
		eq(1d, ex("num n = 1 return n-- + n--"));
		eq(5d, ex("num n = 1 return ++n + ++n"));
		eq(-1d, ex("num n = 1 return --n + --n"));
	}
	
	/**
	 * Testa a estrutura de for
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFor() throws Exception {
		eq(null, ex("for (num n = 1; n < 64 ; n++) {}"));
		eq(null, ex("for (num n = 1 num m = 1; n < 64 and m < 128  ; n++ m++) {}"));
		eq(64d, ex("num n = 1 for (n = 1; n < 64 ; n++) {} return n"));
		eq(0d, ex("num n = 64 for (n = 64; n > 0 ; n--) {} return n"));
		eq(64d, ex("num n = 1 for (; n < 64 ;) {n++} return n"));
	}
	
	/**
	 * Testa a estrutura de for
	 * 
	 * @throws Exception
	 */
	@Test
	public void testWhile() throws Exception {
		eq(1d, ex("num n = 1 while (n < 1) {n++} return n"));
		eq(64d, ex("num n = 1 while (n < 64) {n++} return n"));
		eq(0d, ex("num n = 64 while (n > 0) {n--} return n"));
	}
	
	/**
	 * Testa a estrutura de for
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRepeat() throws Exception {
		eq(2d, ex("num n = 1 repeat {n++} while (n < 1) return n"));
		eq(64d, ex("num n = 1 repeat {n++} while (n < 64) return n"));
		eq(0d, ex("num n = 64 repeat {n--} while (n > 0) return n"));
	}
	
	/**
	 * Testa a estrutura de for
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSwitch() throws Exception {
		eq(1d, ex("switch 1 case 1 return 1 return 0"));
		eq(1d, ex("switch 1 case 1 return 1 case 2 return 2"));
		eq(1d, ex("switch 1 case 1 1 case 2 2 return 1"));
		eq(10d, ex("num n = 1 switch n case 1 n = 10 case 2 n = 20 return n"));
		eq(10d, ex("num n = 1 switch n case 1 n = 10 case 2 n = 20 return n"));
		eq(10d, ex("num n = 1 switch n case 1 n = 10 return n"));
		eq(20d, ex("num n = 2 switch n case 1 n = 10 else n = 20 return n"));
		eq(2d, ex("num n = 2 switch n case 1 n = 10 return n"));
		eq(20d, ex("num n = 2 switch n case 1 n = 10 case 2 n = 20 return n"));
		eq(3d, ex("num n = 3 switch n case 1 n = 10 case 2 n = 20 return n"));
		eq(
			20d,
			ex("num n = 2 switch n case 1 n = 10 case 2 n = 20 case 3 n = 30 return n"));
		eq(
			4d,
			ex("num n = 4 switch n case 1 n = 10 case 2 n = 20 case 3 n = 30 return n"));
		eq(30d,
			ex("num n = 3 switch n case 1 n = 10 case 2 n = 20 else n = 30 return n"));
		// fail("num n = 0 switch (n) case 'a'n = 10 return n"));
	}
	
	/**
	 * Testa a estrutura de if
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIf() throws Exception {
		eq(1d, ex("if (true) return 1"));
		eq(0d, ex("if (false) return 1 return 0"));
		eq(1d, ex("if (true) return 1 else return 0"));
		eq(1d, ex("if (false) return 0 else return 1"));
		eq(0d, ex("if (false) return 1 else return 0"));
		eq(1d, ex("if (1==1) return 1"));
	}
	
	/**
	 * Testa a estrutura de metodo
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMethod() throws Exception {
		eq(null, ex("num a () {return 1}"));
		eq(1d, ex("num a () {return 1} return a()"));
		eq(2d, ex("num a () {return 2} return a()"));
		eq("a", ex("str a () {return 'a'} return a()"));
		eq(true, ex("bool a () {return true} return a()"));
		eq(2d, ex("num a () {return 1} return a()+1"));
		eq(2d, ex("num a () {return 1} return a()+1"));
		eq(2d, ex("num a () {num a = 2 return a} return a()"));
		eq(3d, ex("num a () {num a = 2 num b = 3 return b} return a()"));
	}
	
	/**
	 * Testa a estrutura de metodo
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMethodParam() throws Exception {
		eq(null, ex("num a (num a) {return a} a(1)"));
		eq(null, ex("void a (num a) {return null} a(1)"));
		eq(null, ex("void a (num a) {return null} return a(1)"));
		eq(1d, ex("num a (num a) {return a} return a(1)"));
		eq(2d, ex("num a (num a, num b) {return b} return a(1,2)"));
		eq(true, ex("num a (num a, num b, num c) "
			+ "{return a==1 and b==2 and c==3} return a(1,2,3)"));
		eq(true, ex("num a (num a) {num b = 2 return a==1 and b==2} return a(1)"));
		eq(true, ex("num a (num a, num b, num c) " + "{ num d=4 num e=5 num f=6 "
			+ "return a==1 and b==2 and c==3 and d==4 and e==5 and f==6"
			+ "} return a(1,2,3)"));
	}
	
	/**
	 * Testa a estrutura de metodo
	 * 
	 * @throws Exception
	 */
	@Test
	public void testClass() throws Exception {
		eq(null, ex("class a {}"));
		eq(null, ex("class a {} class b {}"));
		eq(null, ex("class a { num a }"));
		eq(null, ex("class a { num a num b }"));
		eq(null, ex("class a { num a num b num c }"));
		eq(null, ex("class a { void a () {} }"));
		eq(null, ex("class a { num a void b () {} num c }"));
		eq(null, ex("class a { void a () {} void b () {} }"));
		eq(null, ex("class a { void a () {} }"));
	}
	
	/**
	 * Testa a estrutura de metodo
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNew() throws Exception {
		eq(null, ex("class a {} new a()"));
		eq(null, ex("class a {} a a = new a()"));
		eq(null, ex("class a { num a } a a = new a() return a.a"));
	}
	
	/**
	 * Executa o código
	 * 
	 * @param code
	 * @return resultado
	 */
	public static Object ex(String code) {
		return AGBCompiler.execute(AGBCompiler.compile(code));
	}
	
}
