import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.agwmail.api.AgentMy;

/**
 * Testador da janela de Index
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class OfflineTest {

  /** Driver */
  public AgentMy d;

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
  }

  /**
   * Finaliza o teste
   */
  @After
  public void after() {
    d.close();
  }

}
