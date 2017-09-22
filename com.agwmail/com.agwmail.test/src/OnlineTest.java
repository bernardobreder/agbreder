import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.agwmail.api.AgentMy;
import com.agwmail.api.HeaderPage;
import com.agwmail.api.LoginPage;

/**
 * Testador da janela de Index
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class OnlineTest {

  /** Driver */
  public AgentMy d;
  /** Driver */
  public HeaderPage u;

  /**
   * Inicializa os testes
   */
  @BeforeClass
  public static void beforeClass() {
  }

  /**
   * Inicia o teste
   */
  @Before
  public void before() {
    d = new AgentMy();
    LoginPage page = d.getPage();
    page.clickCreateAccount();
    page.typeAccountUsername("breder");
    page.typeAccountEmail("bernardobreder@gmail.com");
    page.typeAccountPassword("breder");
    page.clickAccountSubmit();
    page.typeUserUsername("breder");
    page.typeUserPassword("breder");
    u = page.clickUserSubmit();
  }

  /**
   * Finaliza o teste
   */
  @After
  public void after() {
    d.close();
  }

}
