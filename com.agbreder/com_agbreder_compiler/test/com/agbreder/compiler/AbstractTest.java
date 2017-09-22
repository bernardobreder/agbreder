package com.agbreder.compiler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import com.agbreder.vm.AGBVm;
import com.agbreder.vm.AGBVmList;

/**
 * Classe base de teste
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class AbstractTest {

  /**
   * Assert Equal
   * 
   * @param expected
   * @param codes
   * @throws Exception
   */
  public static void eq(Object expected, String... codes) throws Exception {
    Assert.assertEquals(expected, execute(codes));
  }

  /**
   * Assert Fail
   * 
   * @param codes
   * @throws Exception
   */
  public static void fail(String... codes) throws Exception {
    try {
      execute(codes);
      Assert.fail();
    }
    catch (Throwable t) {
    }
  }

  /**
   * Executa
   * 
   * @param sources
   * @return resultado
   * @throws Exception
   */
  public static Object execute(String... sources) throws Exception {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    List<InputStream> list = new ArrayList<InputStream>();
    for (String source : sources) {
      list.add(new ByteArrayInputStream(source.getBytes()));
    }
    long timer = System.nanoTime();
    AGBCompiler.compile(output, list);
    String base16 = new String(output.toByteArray());
    Object value = AGBVm.loop(base16);
    System.out.println("Timer : "
      + String.format("%.3f",
        (((double) System.nanoTime() - timer)) / 1000 / 1000));
    return value;
  }

  /**
   * Executa
   * 
   * @param sources
   * @throws Exception
   */
  public static void print(String... sources) throws Exception {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    List<InputStream> list = new ArrayList<InputStream>();
    for (String source : sources) {
      list.add(new ByteArrayInputStream(source.getBytes()));
    }
    AGBCompiler.compile(output, list);
    String base64 = new String(output.toByteArray());
    new AGBVmList().loop(base64);
  }

}
