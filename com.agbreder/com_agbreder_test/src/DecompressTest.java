import org.junit.Test;

/**
 * Testador lexical
 * 
 * 
 * @author Bernardo Breder
 */
public class DecompressTest extends AbstractTest {

  /**
   * Testador
   * 
   * @throws Exception
   */
  @Test
  public void unyTest() throws Exception {
    eq("", ex(""));
    eq("A", ex("A"));
    eq("AA", ex("2A"));
    eq("AAA", ex("3A"));
    eq("AAAB", ex("3AB"));
    eq("AAABB", ex("3A2B"));
    eq("AAABBC", ex("3A2BC"));
    eq("AAABBCC", ex("3A2B2C"));
    eq("AAABBBCC", ex("3A3B2C"));
  }

  /**
   * Executa o código
   * 
   * @param code
   * @return resultado
   */
  public Object ex(String code) {
    return js("return _agb_decompress('" + code + "')");
  }

}
