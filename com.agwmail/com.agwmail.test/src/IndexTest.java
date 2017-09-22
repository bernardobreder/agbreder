import org.junit.Test;

/**
 * Testador da pagina inicial
 * 
 * 
 * @author Bernardo Breder
 */
public class IndexTest extends OnlineTest {

  /**
   * Teste de carga da pagina
   */
  @Test
  public void logout() {
    u.clickLogout();
  }

  /**
   * Teste de carga da pagina
   */
  @Test
  public void initInbox() {
    u.clickInbox();
  }

  /**
   * Teste de carga da pagina
   */
  @Test
  public void initAgent() {
    u.clickAgent();
  }

  /**
   * Teste de carga da pagina
   */
  @Test
  public void initCompose() {
    u.clickCompose();
  }

  /**
   * Teste de carga da pagina
   */
  @Test
  public void initScript() {
    u.clickScript();
  }

  /**
   * Teste de carga das paginas
   */
  @Test
  public void initAll() {
    u.clickInbox();
    u.clickAgent();
    u.clickCompose();
    u.clickScript();
  }

  /**
   * Teste de carga das paginas
   */
  @Test
  public void sendMail() {
    u.clickCompose().typeTo("breder").typeSubject("subject")
      .typeText("message").send();
  }

  /**
   * Teste de carga das paginas
   */
  @Test
  public void sendMailRepeat() {
    for (int n = 0; n < 10; n++) {
      u.clickCompose().typeTo("breder").typeSubject("subject_" + n).typeText(
        "message_" + n).send();
    }
  }

  /**
   * Testador
   * 
   * @param args
   */
  public static void main(String[] args) {
    IndexTest d = new IndexTest();
    d.before();
    d.u.clickCompose().typeTo("breder").typeSubject("script").typeText(
      "1+1 = {{return 1+1}} e {{return 1+1}}.").send();
  }

}
