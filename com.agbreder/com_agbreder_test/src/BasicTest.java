import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

/**
 * Testador Básico
 * 
 * @author Bernardo Breder
 */
public abstract class BasicTest {
	
	/**
	 * Cria um mapa
	 * 
	 * @param objects
	 * @return mapa
	 */
	protected static Map<Object, Object> map(Object... objects) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		for (int n = 0; n < objects.length / 2; n++) {
			map.put(objects[2 * n], objects[2 * n + 1]);
		}
		return map;
	}
	
	/**
	 * Cria um mapa
	 * 
	 * @param objects
	 * @return mapa
	 */
	protected static List<Object> list(Object... objects) {
		List<Object> list = new ArrayList<Object>(objects.length);
		for (int n = 0; n < objects.length; n++) {
			list.add(objects[n]);
		}
		return list;
	}
	
	/**
	 * Cria um token
	 * 
	 * @param value
	 * @return node
	 */
	protected static Map<Object, Object> token(String text, long type, long col) {
		return token(text, type, 1, col);
	}
	
	/**
	 * Cria um token
	 * 
	 * @param value
	 * @return node
	 */
	protected static Map<Object, Object> token(String text, long type, long lin,
		long col) {
		return map("text", text, "type", type, "lin", lin, "col", col);
	}
	
	/**
	 * Compara a igualdade
	 * 
	 * @param expected
	 * @param atual
	 */
	protected static void eq(Object expected, Object atual) {
		if ((expected == null && atual != null)
			|| (expected != null && atual == null)
			|| (expected != null && atual != null && !expected.equals(atual))) {
			System.out.println("Expected: " + expected);
			System.out.println("Atual   : " + atual);
		}
		Assert.assertEquals(expected, atual);
	}
	
}
