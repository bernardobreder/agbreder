package com.agbreder.vm;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.agbreder.vm.AGBOpcode;
import com.agbreder.vm.AGBVm;

/**
 * Testador de classe
 * 
 * 
 * @author Bernardo Breder
 */
public class RootLangVmTest {

  /**
   * Teste
   */
  @Test
  public void test() {
    AGBVm
      .loop("aaaaaaadaaaaaaaiaaaaaaacaaaaaaadaaaaaaajaaaaaaaaaaaaaaadaaaaaaacaaaaaaadaaaaaaaaaaaaaadfaaaaaacoaaaaaadaaaaaaaaaaaaaaaadaaaaaaacaaaaaaadaaaaaaaaaaaaaadcaaaaaacoaaaaaadcaaaaaaaaaaaaaaaeaaaaaaaeaaaaaaaaaaaaaaaeaaaaaaaeaaaaaaabaaaaaaaiaaaaaaacaaaaaaabaaaaaaab");
  }

  /**
   * Teste normal
   * 
   * @throws IOException
   */
  public void test1() throws IOException {
    Assert.assertEquals(1, AGBVm.loop(new AGBOpcode().opStackPush(1)
      .opStackInt(1).opStackStore(1).opStackLoad(0).opStackStore(1).opVmHalf()
      .toString()));
    Assert.assertEquals(10d, AGBVm.loop(new AGBOpcode().opDefNumInit(
      1).opDefNum(5).opStackPush(2).opLoadNum(0).opStackStore(2).opStackLoad(1)
      .opLoadNum(0).opNumSum().opStackStore(1).opVmHalf().toString()));
    Assert.assertArrayEquals(new Object[] { 1d, 2d }, (Object[]) AGBVm
      .loop(new AGBOpcode().opDefNumInit(3).opDefNum(1).opDefNum(2)
        .opDefNum(0).opStackPush(1).opLoadNum(1).opArrayNew().opStackStore(1)
        .opLoadNum(0).opStackLoad(1).opLoadNum(2).opArraySet().opLoadNum(1)
        .opStackLoad(1).opLoadNum(0).opArraySet().opVmHalf().toString()));
    Assert.assertArrayEquals(new Object[] { new Object[] { 1d, null, null },
        null }, (Object[]) AGBVm.loop(new AGBOpcode().opDefNumInit(4)
      .opDefNum(1).opDefNum(2).opDefNum(3).opDefNum(0).opStackPush(2)
      .opLoadNum(1).opArrayNew().opStackStore(1).opLoadNum(2).opArrayNew()
      .opStackLoad(1).opLoadNum(3).opArraySet().opLoadNum(0).opStackLoad(1)
      .opLoadNum(3).opArrayGet().opLoadNum(3).opArraySet().opStackStore(1)
      .opVmHalf().toString()));
  }

}
