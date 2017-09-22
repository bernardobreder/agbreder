package com.agbreder.compiler;

import org.junit.Test;

/**
 * Classe de teste
 * 
 * 
 * @author Bernardo Breder
 */
public class AGBrederCompilerTest extends AbstractTest {

  /**
   * Teste
   * 
   * @throws Exception
   */
  @Test
  public void init() throws Exception {
    eq(null, "");
  }

  /**
   * Teste
   * 
   * @throws Exception
   */
  @Test
  public void test() throws Exception {
    eq(1d, "return 1");
    eq(true, "return true");
    eq(false, "return false");
    eq("a", "return 'a'");
    eq("á", "return 'á'");
    //    eq("'", "return '\\''");
    fail("return '''");
    eq("\"", "return '\"'");
    eq("\"\"", "return '\"\"'");
    eq(true, "return 1==1");
    eq(true, "return 1!=0");
    eq(false, "return 1==0");
    eq(true, "return 1>0");
    eq(false, "return 1<0");
    eq(true, "return 1>=0");
    eq(false, "return 1<=0");
    eq(true, "return 1==0 or 1==1");
    eq(true, "return 1==1 or 1==0");
    eq(false, "return 1==0 or 1==0");
    eq(false, "return 1==0 and 1==1");
    eq(false, "return 1==1 and 1==0");
    eq(true, "return 1==1 and 1==1");
    eq(true, "return 1==0 or 1==1");
    eq(true, "return 'a' == 'a'");
    eq(false, "return 'a' == 'b'");
    eq(true, "return 'b' != 'a'");
    eq(false, "return 'a' != 'a'");
    eq(false, "return 'a' == 1");
    eq(false, "return 'a' == false");
    eq(false, "return false == 'a'");
    eq(false, "return 1 == 'a'");
    eq(false, "return false == true");
    eq(true, "return false == false");
    eq(true, "return true == true");
    eq(false, "return true == 1");
    eq(false, "return 1 == true");
    eq(1d, "return true ? 1 : 0");
    eq(0d, "return false ? 1 : 0");
    eq(2d, "return 1==1 ? 1==1 ? 2 : 1 : 0");
    eq("ab", "return 'a' + 'b'");
    eq("a1", "return 'a' + 1");
    eq("a11", "return 'a' + 1 + 1");
    eq("a2", "return 'a' + (1+1)");
  }

  /**
   * Testa a estrutura de for
   * 
   * @throws Exception
   */
  @Test
  public void testDeclare() throws Exception {
    eq(1d, "num n = 1 return n");
    eq(2d, "num n = 1+1 return n");
    eq(2d, "num n = 1 n = 2 return n");
    eq(2d, "num n = 1 num m = 2 return m");
    eq(1d, "num n = 1 num m = 2 return n");
    eq(3d, "num n = 1 num m = 2 num p = 3 return p");
    eq(2d, "num n = 1 num m = 2 num p = 3 return m");
    eq(1d, "num n = 1 num m = 2 num p = 3 return n");
    fail("num n = 1 return m");
    fail("return n");
  }

  /**
   * Testa a estrutura de for
   * 
   * @throws Exception
   */
  @Test
  public void testIncDec() throws Exception {
    eq(1d, "num n = 1 return n++");
    eq(2d, "num n = 1 return ++n");
    eq(1d, "num n = 1 return n--");
    eq(0d, "num n = 1 return --n");
    eq(3d, "num n = 1 return n++ + n++");
    eq(1d, "num n = 1 return n-- + n--");
    eq(5d, "num n = 1 return ++n + ++n");
    eq(-1d, "num n = 1 return --n + --n");
  }

  /**
   * Testa a estrutura de for
   * 
   * @throws Exception
   */
  @Test
  public void testFor() throws Exception {
    eq(null, "for (num n = 1; n < 64 ; n++) {}");
    eq(null, "for (num n = 1, num m = 1; n < 64 and m < 128  ; n++, m++) {}");
    eq(64d, "num n = 1 for (n = 1; n < 64 ; n++) {} return n");
    eq(0d, "num n = 64 for (n = 64; n > 0 ; n--) {} return n");
    eq(64d, "num n = 1 for (; n < 64 ;) {n++} return n");
  }

  /**
   * Testa a estrutura de for
   * 
   * @throws Exception
   */
  @Test
  public void testWhile() throws Exception {
    eq(1d, "num n = 1 while (n < 1) {n++} return n");
    eq(64d, "num n = 1 while (n < 64) {n++} return n");
    eq(0d, "num n = 64 while (n > 0) {n--} return n");
  }

  /**
   * Testa a estrutura de for
   * 
   * @throws Exception
   */
  @Test
  public void testRepeat() throws Exception {
    eq(2d, "num n = 1 repeat {n++} while (n < 1) return n");
    eq(64d, "num n = 1 repeat {n++} while (n < 64) return n");
    eq(0d, "num n = 64 repeat {n--} while (n > 0) return n");
  }

  /**
   * Testa a estrutura de for
   * 
   * @throws Exception
   */
  @Test
  public void testSwitch() throws Exception {
    eq(10d, "num n = 1 switch (n) case 1 n = 10 case 2 n = 20 return n");
    eq(10d, "num n = 1 switch (n) case 1 n = 10 return n");
    eq(20d, "num n = 2 switch (n) case 1 n = 10 else n = 20 return n");
    eq(2d, "num n = 2 switch (n) case 1 n = 10 return n");
    eq(20d, "num n = 2 switch (n) case 1 n = 10 case 2 n = 20 return n");
    eq(3d, "num n = 3 switch (n) case 1 n = 10 case 2 n = 20 return n");
    eq(20d,
      "num n = 2 switch (n) case 1 n = 10 case 2 n = 20 case 3 n = 30 return n");
    eq(4d,
      "num n = 4 switch (n) case 1 n = 10 case 2 n = 20 case 3 n = 30 return n");
    eq(30d,
      "num n = 3 switch (n) case 1 n = 10 case 2 n = 20 else n = 30 return n");
    fail("num n = 0 switch (n) case 'a'n = 10 return n");
  }

  /**
   * Testa a estrutura de if
   * 
   * @throws Exception
   */
  @Test
  public void testIf() throws Exception {
    eq(1d, "if (true) return 1");
    eq(0d, "if (false) return 1 return 0");
    eq(1d, "if (true) return 1 else return 0");
    eq(1d, "if (false) return 0 else return 1");
    eq(0d, "if (false) return 1 else return 0");
    eq(1d, "if (1==1) return 1");
  }

  /**
   * Testa a estrutura de metodo
   * 
   * @throws Exception
   */
  @Test
  public void testMethod() throws Exception {
    eq(null, "def num a () {return 1}");
    eq(1d, "def num a () {return 1} return a()");
    eq(2d, "def num a () {return 2} return a()");
    eq("a", "def str a () {return 'a'} return a()");
    eq(true, "def bool a () {return true} return a()");
    eq(2d, "def num a () {return 1} return a()+1");
    eq(2d, "def num a () {return 1} return a()+1");
    eq(2d, "def num a () {num a = 2 return a} return a()");
    eq(3d, "def num a () {num a = 2 num b = 3 return b} return a()");
  }

  /**
   * Testa a estrutura de metodo
   * 
   * @throws Exception
   */
  @Test
  public void testMethodParam() throws Exception {
    eq(null, "def num a (num a) {return a} a(1)");
    eq(null, "def void a (num a) {return null} a(1)");
    eq(null, "def void a (num a) {return null} return a(1)");
    eq(1d, "def num a (num a) {return a} return a(1)");
    eq(2d, "def num a (num a, num b) {return b} return a(1,2)");
    eq(true, "def num a (num a, num b, num c) "
      + "{return a==1 and b==2 and c==3} return a(1,2,3)");
    eq(true, "def num a (num a) {num b = 2 return a==1 and b==2} return a(1)");
    eq(true, "def num a (num a, num b, num c) " + "{num d=4 num e=5 num f=6 "
      + "return a==1 and b==2 and c==3 and d==4 and e==5 and f==6"
      + "} return a(1,2,3)");
  }

  /**
   * Testa a estrutura de metodo
   * 
   * @throws Exception
   */
  @Test
  public void testNative() throws Exception {
    fail("|1,1|");
  }

  /**
   * Testador
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    //    execute("for (num n = 0 ; n < 32 * 1024 * 1024 ; n++){}");
    //    execute("num n = 1 repeat {n++} while (n < 1) return n");
    print("num n = 3 switch (n) case 1 n = 10 case 2 n = 20 else n = 30 return n");
  }

}
