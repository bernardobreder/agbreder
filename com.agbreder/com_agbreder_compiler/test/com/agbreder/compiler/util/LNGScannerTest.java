package com.agbreder.compiler.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.agbreder.compiler.util.LNGScanner;
import com.agbreder.compiler.util.LNGToken;

/**
 * Testador de classe
 * 
 * 
 * @author Bernardo Breder
 */
public class LNGScannerTest {

  /**
   * Testa
   * 
   * @throws IOException
   */
  @Test
  public void testFire() throws IOException {
    new LNGScanner(new ByteArrayInputStream("a bc d ".getBytes()));
    new LNGScanner(new ByteArrayInputStream(" a bc".getBytes()));
    new LNGScanner(new ByteArrayInputStream("".getBytes()));
    new LNGScanner(new ByteArrayInputStream("a".getBytes()));
    new LNGScanner(new ByteArrayInputStream(" ".getBytes()));
    new LNGScanner(new ByteArrayInputStream("a\nb\nc".getBytes()));
    new LNGScanner(new ByteArrayInputStream("a\nb\nc\n".getBytes()));
  }

  /**
   * Testa
   * 
   * @throws IOException
   */
  @Test
  public void test1() throws IOException {
    Assert.assertEquals(Arrays.asList(new LNGToken("a", 1, 1, 0), new LNGToken("bc",
      1, 3, 2), new LNGToken("d", 1, 6, 5)), new LNGScanner(
      new ByteArrayInputStream("a bc d ".getBytes())).getTokens());
  }

  /**
   * Testa
   * 
   * @throws IOException
   */
  @Test
  public void test2() throws IOException {
    Assert.assertEquals(Arrays.asList(new LNGToken("a", 1, 2, 1), new LNGToken("bc",
      1, 4, 3)), new LNGScanner(new ByteArrayInputStream(" a bc".getBytes()))
      .getTokens());
  }

  /**
   * Testa
   * 
   * @throws IOException
   */
  @Test
  public void test3() throws IOException {
    Assert.assertEquals(new ArrayList<LNGToken>(), new LNGScanner(
      new ByteArrayInputStream("".getBytes())).getTokens());
  }

  /**
   * Testa
   * 
   * @throws IOException
   */
  @Test
  public void test4() throws IOException {
    Assert.assertEquals(Arrays.asList(new LNGToken("a", 1, 1, 0)), new LNGScanner(
      new ByteArrayInputStream("a".getBytes())).getTokens());
  }

  /**
   * Testa
   * 
   * @throws IOException
   */
  @Test
  public void test5() throws IOException {
    Assert.assertEquals(new ArrayList<LNGToken>(), new LNGScanner(
      new ByteArrayInputStream(" ".getBytes())).getTokens());
  }

  /**
   * Testa
   * 
   * @throws IOException
   */
  @Test
  public void test6() throws IOException {
    Assert.assertEquals(Arrays.asList(new LNGToken("a", 1, 1, 0), new LNGToken("b",
      2, 1, 2), new LNGToken("c", 3, 1, 4)), new LNGScanner(
      new ByteArrayInputStream("a\nb\nc".getBytes())).getTokens());
  }

  /**
   * Testa
   * 
   * @throws IOException
   */
  @Test
  public void test7() throws IOException {
    Assert.assertEquals(Arrays.asList(new LNGToken("a", 1, 1, 0), new LNGToken("b",
      2, 1, 2), new LNGToken("c", 3, 1, 4)), new LNGScanner(
      new ByteArrayInputStream("a\nb\nc\n".getBytes())).getTokens());

    Assert.assertEquals(Arrays.asList(new LNGToken("a", 1, 1, 0), new LNGToken("b",
      2, 1, 3), new LNGToken("c", 3, 1, 6)), new LNGScanner(
      new ByteArrayInputStream("a \nb \nc \n".getBytes())).getTokens());

    Assert.assertEquals(Arrays.asList(new LNGToken("a", 1, 2, 1), new LNGToken("b",
      2, 2, 4), new LNGToken("c", 3, 2, 7)), new LNGScanner(
      new ByteArrayInputStream(" a\n b\n c\n".getBytes())).getTokens());

    Assert.assertEquals(Arrays.asList(new LNGToken("a", 1, 2, 1), new LNGToken("b",
      2, 2, 5), new LNGToken("c", 3, 2, 9)), new LNGScanner(
      new ByteArrayInputStream(" a \n b \n c \n  ".getBytes())).getTokens());
  }

  /**
   * Testa
   * 
   * @throws IOException
   */
  @Test
  public void test8() throws IOException {
    Assert.assertEquals(Arrays.asList(new LNGToken("a", 1, 1, 0), new LNGToken("{",
      1, 2, 1), new LNGToken("}", 1, 3, 2), new LNGToken("b", 1, 4, 3), new LNGToken(
      "{", 1, 5, 4), new LNGToken("c", 1, 6, 5), new LNGToken("}", 1, 7, 6),
      new LNGToken("}", 1, 8, 7)), new LNGScanner(new ByteArrayInputStream(
      "a{}b{c}}".getBytes())).getTokens());
  }

}
