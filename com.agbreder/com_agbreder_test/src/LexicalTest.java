import org.junit.Test;

/**
 * Testador lexical
 * 
 * 
 * @author Bernardo Breder
 */
public class LexicalTest extends AbstractTest {

  /**
   * Testa identificador
   * 
   * @throws Exception
   */
  @Test
  public void identify() throws Exception {
    eq(list(token("aaa", 1, 1)), ex("aaa"));
    eq(list(token("aaa", 1, 1), token("bbb", 1, 5)), ex("aaa bbb"));
    eq(list(token("num", 5, 1)), ex("num"));
  }

  /**
   * Testa o ignore
   */
  @Test
  public void ignore() {
    eq(list(token("aaa", 1, 1), token("bbb", 1, 7)), ex("aaa   bbb"));
    eq(list(token("aaa", 1, 1), token("bbb", 1, 7)), ex("aaa\t\t\tbbb"));
    eq(list(token("aaa", 1, 2), token("bbb", 1, 6)), ex("\taaa bbb"));
    eq(list(token("aaa", 1, 2), token("bbb", 1, 6)), ex("\taaa\tbbb"));
    eq(list(token("aaa", 1, 2), token("bbb", 1, 6)), ex("\taaa\tbbb\t"));
  }

  /**
   * Testa simbolo
   */
  @Test
  public void symbol() {
    eq(
      list(token("aaa", 1, 1), token("[", 19, 4), token("b", 1, 5),
        token("]", 20, 6), token("ccc", 1, 7)), ex("aaa[b]ccc"));
  }

  /**
   * Testa Number
   */
  @Test
  public void number() {
    eq(list(token("a", 1, 1), token("1", 3, 2), token("b", 1, 3)), ex("a1b"));
    eq(list(token("a", 1, 1), token("1.1", 3, 2), token("b", 1, 5)),
      ex("a1.1b"));
    eq(
      list(token("a", 1, 1), token("1.1", 3, 2), token(".", 11, 5),
        token("b", 1, 6)), ex("a1.1.b"));
  }

  /**
   * Testa String
   */
  @Test
  public void string() {
    eq(list(token("", 4, 1)), ex("''"));
    eq(list(token("a", 4, 1)), ex("'a'"));
    eq(list(token("aa", 4, 1)), ex("'aa'"));
    eq(list(token("", 4, 1), token("", 4, 3)), ex("''''"));
    eq(list(token("a", 4, 1), token("b", 4, 4)), ex("'a''b'"));
    eq(list(token("aa", 4, 1), token("bb", 4, 5)), ex("'aa''bb'"));
  }

  /**
   * Testa String
   */
  @Test
  public void both() {
    eq(
      list(token("a", 1, 2), token("b", 4, 4), token("+", 32, 8),
        token("1", 3, 10)), ex(" a 'b' + 1 "));
    eq(
      list(token("a", 1, 2, 2), token("b", 4, 3, 2), token("+", 32, 4, 1),
        token("1", 3, 6, 2)), ex("\\n a\\n 'b' \\n+ \\n\\n 1 "));
  }

  /**
   * Executa o código
   * 
   * @param code
   * @return resultado
   */
  public Object ex(String code) {
    return js("return JSON.parse(JSON.stringify(_agb_lexical(\"" + code + "\")))");
  }

}
