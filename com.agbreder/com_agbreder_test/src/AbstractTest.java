import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Testador Básico
 * 
 * @author Bernardo Breder
 */
public abstract class AbstractTest extends BasicTest {
	
	/** Cliente */
	protected HtmlUnitDriver c;
	
	/**
	 * Executa antes do teste
	 */
	@Before
	public void before() {
		c = new HtmlUnitDriver();
		c.setJavascriptEnabled(true);
		c.get("http://localhost:8080/agbreder/index");
	}
	
	/**
	 * Executa depois do teste
	 */
	@After
	public void after() {
		c.close();
	}
	
	/**
	 * Executa o codigo
	 * 
	 * @param code
	 * @return objeto
	 */
	public Object js(String code) {
		return c.executeScript(code);
	}
	
}
