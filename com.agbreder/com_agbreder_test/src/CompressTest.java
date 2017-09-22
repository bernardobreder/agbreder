import org.junit.Test;

/**
 * Testador lexical
 * 
 * 
 * @author Bernardo Breder
 */
public class CompressTest extends AbstractTest {

  /**
   * Testador
   * 
   * @throws Exception
   */
  @Test
  public void unyTest() throws Exception {
    eq("", ex(""));
    eq("A", ex("A"));
    eq("2A", ex("AA"));
    eq("3A", ex("AAA"));
    eq("9A", ex("AAAAAAAAA"));
    eq("9AA", ex("AAAAAAAAAA"));
    eq("9A2A", ex("AAAAAAAAAAA"));
    eq("9A3A", ex("AAAAAAAAAAAA"));
    eq("9A4A", ex("AAAAAAAAAAAAA"));
    eq("9A5A", ex("AAAAAAAAAAAAAA"));
    eq("9A6A", ex("AAAAAAAAAAAAAAA"));
    eq("9A7A", ex("AAAAAAAAAAAAAAAA"));
    eq("9A8A", ex("AAAAAAAAAAAAAAAAA"));
    eq("9A9A", ex("AAAAAAAAAAAAAAAAAA"));
    eq("9A9AA",  ex("AAAAAAAAAAAAAAAAAAA"));
    eq("9A9A2A", ex("AAAAAAAAAAAAAAAAAAAA"));
    eq("9A9A3A", ex("AAAAAAAAAAAAAAAAAAAAA"));
  }
  
  /**
   * Testador
   * 
   * @throws Exception
   */
  @Test
  public void binTest() throws Exception {
    eq("AB", ex("AB"));
    eq("2AB", ex("AAB"));
    eq("3AB", ex("AAAB"));
    eq("9AB", ex("AAAAAAAAAB"));
    eq("9AAB", ex("AAAAAAAAAAB"));
    eq("9A2AB", ex("AAAAAAAAAAAB"));
    eq("9A3AB", ex("AAAAAAAAAAAAB"));
    eq("9A4A", ex("AAAAAAAAAAAAA"));
    eq("9A5A", ex("AAAAAAAAAAAAAA"));
    eq("9A6A", ex("AAAAAAAAAAAAAAA"));
    eq("9A7A", ex("AAAAAAAAAAAAAAAA"));
    eq("9A8A", ex("AAAAAAAAAAAAAAAAA"));
    eq("9A9A", ex("AAAAAAAAAAAAAAAAAA"));
    eq("9A9AA",  ex("AAAAAAAAAAAAAAAAAAA"));
    eq("9A9A2A", ex("AAAAAAAAAAAAAAAAAAAA"));
    eq("9A9A3A", ex("AAAAAAAAAAAAAAAAAAAAA"));
  }

  
  /**
   * Testador
   * 
   * @throws Exception
   */
  @Test
  public void mixTest() throws Exception {
    eq("AB", ex("AB"));
    eq("ABC", ex("ABC"));
    eq("A2BC", ex("ABBC"));
    eq("AB2C", ex("ABCC"));
    eq("2ABC", ex("AABC"));
    eq("A2B2C", ex("ABBCC"));
    eq("2A2BC", ex("AABBC"));
    eq("2AB2C", ex("AABCC"));
    eq("2A2B2C", ex("AABBCC"));
    eq("2A2B3C", ex("AABBCCC"));
    eq("2A2B9C", ex("AABBCCCCCCCCC"));
    eq("2A2B9CC", ex("AABBCCCCCCCCCC"));
    eq("2A2B9C2C", ex("AABBCCCCCCCCCCC"));
    eq("2A3B2C", ex("AABBBCC"));
    eq("2A9B2C", ex("AABBBBBBBBBCC"));
    eq("2A9BB2C", ex("AABBBBBBBBBBCC"));
    eq("2A9B2B2C", ex("AABBBBBBBBBBBCC"));
    eq("3A2B2C", ex("AAABBCC"));
    eq("9A2B2C", ex("AAAAAAAAABBCC"));
    eq("9AA2B2C", ex("AAAAAAAAAABBCC"));
    eq("9A2A2B2C", ex("AAAAAAAAAAABBCC"));
    eq("9A2A2B2C", ex("AAAAAAAAAAABBCC"));
    eq("9A2A9B2B9C2C", ex("AAAAAAAAAAABBBBBBBBBBBCCCCCCCCCCC"));
  }

  /**
   * Executa o código
   * 
   * @param code
   * @return resultado
   */
  public Object ex(String code) {
    return js("return _agb_compress('" + code + "')");
  }

}
