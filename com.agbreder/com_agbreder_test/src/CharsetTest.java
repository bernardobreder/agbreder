import org.junit.Test;

/**
 * Testador lexical
 * 
 * @author Bernardo Breder
 */
public class CharsetTest extends AbstractTest {
	
	/**
	 * Testador
	 */
	@Test
	public void test() {
		eq("é", ex("é"));
		eq("ação", ex("ação"));
		eq("123", ex("123"));
	}
	
	/**
	 * Executa o código
	 * 
	 * @param code
	 * @return resultado
	 */
	public Object ex(String code) {
		return js("return _agb_utf8_decode(base64.decode(base64.encode(_agb_utf8_encode('"
			+ code + "'))))");
	}
	
}
