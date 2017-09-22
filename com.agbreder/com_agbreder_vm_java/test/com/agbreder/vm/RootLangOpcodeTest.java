package com.agbreder.vm;
import junit.framework.Assert;

import org.junit.Test;

import com.agbreder.vm.AGBOpcode;

/**
 * Testador de clase
 * 
 * 
 * @author Bernardo Breder
 */
public class RootLangOpcodeTest {

  /**
   * Testador de metodo
   */
  @Test
  public void str() {
    Assert.assertEquals("aaaaaaaa", AGBOpcode.str(""));
    Assert.assertEquals("aaaaaagb" + "aaaaaaaa", AGBOpcode.str("a"));
    Assert.assertEquals("aaaaaagb" + "aaaaaagc" + "aaaaaaaa", AGBOpcode
      .str("ab"));
    Assert.assertEquals("aaaaaagb" + "aaaaaagc" + "aaaaaagd" + "aaaaaaaa",
      AGBOpcode.str("abc"));
    Assert.assertEquals("aaaaaagb" + "aaaaaagc" + "aaaaaagd" + "aaaaaage"
      + "aaaaaaaa", AGBOpcode.str("abcd"));
    Assert.assertEquals("aaaaaagb" + "aaaaaagc" + "aaaaaagd" + "aaaaaage"
      + "aaaaaagf" + "aaaaaaaa", AGBOpcode.str("abcde"));
    Assert.assertEquals("aaaaaagb" + "aaaaaagc" + "aaaaaagd" + "aaaaaage"
      + "aaaaaagf" + "aaaaaagg" + "aaaaaagh" + "aaaaaagi" + "aaaaaaaa",
      AGBOpcode.str("abcdefgh"));
    Assert.assertEquals("aaaaaagb" + "aaaaaagc" + "aaaaaagd" + "aaaaaage"
      + "aaaaaagf" + "aaaaaagg" + "aaaaaagh" + "aaaaaagi" + "aaaaaagj"
      + "aaaaaaaa", AGBOpcode.str("abcdefghi"));
    Assert.assertEquals("aaaaaagc" + "aaaaaagf" + "aaaaaahc" + "aaaaaago"
      + "aaaaaagb" + "aaaaaahc" + "aaaaaage" + "aaaaaagp" + "aaaaaaca"
      + "aaaaaagc" + "aaaaaahc" + "aaaaaagf" + "aaaaaage" + "aaaaaagf"
      + "aaaaaahc" + "aaaaaaaa", AGBOpcode.str("bernardo breder"));
  }

}
