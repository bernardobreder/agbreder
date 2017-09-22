import org.junit.Test;

/**
 * Testa a performance
 * 
 * @author bernardobreder
 * 
 */
public class PerformanceTest extends AbstractTest {

  @Test
  public void test() {
    double emu, ntv;
    {
      long timer = System.currentTimeMillis();
      ex("for (num n = 1 ; n < 1024 ;n++) { "
        + "for (num m = 1 ; m < 128 ; m++) { "
        + "for (num p = 1 ; p < 1 ; p++) {} } }");
      emu = System.currentTimeMillis() - timer;
    }
    {
      long timer = System.currentTimeMillis();
      exntv("for (var n = 1 ; n < 1024 ;n++) { "
        + "for (var m = 1 ; m < 128 ; m++) { "
        + "for (var p = 1 ; p < 1 ; p++) {} } }");
      ntv = System.currentTimeMillis() - timer;
    }
    System.out.println(emu / ntv);
  }

  /**
   * Executa o código
   * 
   * @param code
   * @return resultado
   */
  public Object ex(String code) {
    return js("return agb_eval(\"" + code + "\")");
  }

  /**
   * Executa o código
   * 
   * @param code
   * @return resultado
   */
  public Object exntv(String code) {
    return js("return eval(\"" + code + "\")");
  }

}
