import java.util.Map;

import org.junit.Test;

import com.agbreder.compiler.AGBCompiler;

/**
 * Testador lexical
 * 
 * @author Bernardo Breder
 */
public class SyntaxTest extends AbstractTest {
	
	public static final int ID = 1;
	
	public static final int NUMBER = 3;
	
	public static final int STRING = 4;
	
	public static final int TRUE = 43;
	
	public static final int FALSE = 44;
	
	/**
	 * Testa identificador
	 * 
	 * @throws Exception
	 */
	@Test
	public void expTest() throws Exception {
		eq(list(), ex(""));
		eq(list(exp(num(token("1", NUMBER, 1)))), ex("1"));
		eq(list(exp(num(token("1.1", NUMBER, 1)))), ex("1.1"));
		eq(list(exp(str(token("a", STRING, 1)))), ex("'a'"));
		eq(list(exp(str(token("", 4, 1)))), ex("''"));
		eq(list(exp(bool(token("true", TRUE, 1), true))), ex("true"));
		eq(list(exp(bool(token("false", FALSE, 1), false))), ex("false"));
		eq(list(exp(nil(1))), ex("null"));
		eq(list(exp(rid("a", 1))), ex("a"));
		eq(list(exp(call("a", 1))), ex("a()"));
		eq(list(exp(call("a", 1, num(token("1", NUMBER, 3))))), ex("a(1)"));
		eq(
			list(exp(call("a", 1, num(token("1", NUMBER, 3)),
				num(token("2", NUMBER, 5))))), ex("a(1,2)"));
		eq(list(exp(assign(lid("a", 1), num(token("1", NUMBER, 3))))), ex("a=1"));
		eq(
			list(exp(def(tid("a", 5), numtypeNode("num", 1), lid("a", 5),
				num(token("1", NUMBER, 7))))), ex("num a=1"));
		eq(list(exp(uny(id("a", 3)))), ex("++a"));
		eq(list(exp(uny(id("a", 3)))), ex("--a"));
		eq(list(exp(uny(id("a", 1)))), ex("a++"));
		eq(list(exp(uny(id("a", 1)))), ex("a--"));
		eq(list(exp(uny(num(token("1", NUMBER, 2))))), ex("-1"));
		eq(
			list(exp(bin(token("+", 32, 2), num(token("1", NUMBER, 1)),
				num(token("2", NUMBER, 3))))), ex("1+2"));
		eq(
			list(exp(bin(token("-", 33, 2), num(token("1", NUMBER, 1)),
				num(token("2", NUMBER, 3))))), ex("1-2"));
		eq(
			list(exp(bin(token("*", 34, 2), num(token("1", NUMBER, 1)),
				num(token("2", NUMBER, 3))))), ex("1*2"));
		eq(
			list(exp(bin(token("/", 35, 2), num(token("1", NUMBER, 1)),
				num(token("2", NUMBER, 3))))), ex("1/2"));
		eq(
			list(exp(bin(token("=", 39, 2), num(token("1", NUMBER, 1)),
				num(token("2", NUMBER, 4))))), ex("1==2"));
		eq(
			list(exp(bin(token("!", 36, 2), num(token("1", NUMBER, 1)),
				num(token("2", NUMBER, 4))))), ex("1!=2"));
		eq(
			list(exp(bin(token(">", 40, 2), num(token("1", NUMBER, 1)),
				num(token("2", NUMBER, 4))))), ex("1>=2"));
		eq(
			list(exp(bin(token("<", 41, 2), num(token("1", NUMBER, 1)),
				num(token("2", NUMBER, 4))))), ex("1<=2"));
		eq(
			list(exp(bin(token(">", 40, 2), num(token("1", NUMBER, 1)),
				num(token("2", NUMBER, 3))))), ex("1>2"));
		eq(
			list(exp(bin(token("<", 41, 2), num(token("1", NUMBER, 1)),
				num(token("2", NUMBER, 3))))), ex("1<2"));
		eq(
			list(exp(bin(token("and", 37, 3), num(token("1", NUMBER, 1)),
				num(token("2", NUMBER, 7))))), ex("1 and 2"));
		eq(
			list(exp(bin(token("or", 38, 3), num(token("1", NUMBER, 1)),
				num(token("2", NUMBER, 6))))), ex("1 or 2"));
		eq(
			list(exp(ter(token("?", 42, 2), num(token("1", NUMBER, 1)),
				num(token("3", NUMBER, 5)), num(token("2", NUMBER, 3))))), ex("1?2:3"));
	}
	
	/**
	 * Testa identificador
	 * 
	 * @throws Exception
	 */
	@Test
	public void returnCmd() throws Exception {
		eq(list(map("left", num(token("1", NUMBER, 8)))), ex("return 1"));
		eq(list(map("left", bool(token("true", TRUE, 8), true))), ex("return true"));
	}
	
	/**
	 * Testa identificador
	 * 
	 * @throws Exception
	 */
	@Test
	public void forCmd() {
		eq(
			list(map(
				"inits",
				list(exp(def(tid("n", 10), numtypeNode("num", 6), lid("n", 10),
					num(token("1", NUMBER, 14))))), "cond",
				bin(token("=", 39, 20), rid("n", 18), num(token("1", NUMBER, 23))),
				"nexts", list(exp(uny(id("n", 27)))), "cmd",
				map("left", num(token("1", NUMBER, 39))))),
			ex("for (num n = 1 ; n == 1 ; n++) return 1"));
		eq(
			list(map(
				"inits",
				list(exp(def(tid("n", 10), numtypeNode("num", 6), lid("n", 10),
					num(token("1", NUMBER, 14))))), "cond",
				bin(token("=", 39, 20), rid("n", 18), num(token("1", NUMBER, 23))),
				"nexts", list(exp(uny(id("n", 27))), exp(uny(id("n", 31)))), "cmd",
				map("left", num(token("1", NUMBER, 43))))),
			ex("for (num n = 1 ; n == 1 ; n++ n++) return 1"));
		eq(
			list(map("inits", list(), "cond", bool(token(";", 13, 6), true), "nexts",
				list(), "cmd", map("left", num(token("1", NUMBER, 17))))),
			ex("for (;;) return 1"));
	}
	
	/**
	 * Testador
	 */
	@Test
	public void switchCmd() {
		eq(
			list(map("cond", num(token("1", NUMBER, 8)), "cases",
				list(map("cond", num(token("2", NUMBER, 15)), "cmd", exp(nil(17)))),
				"elcmd", null)), ex("switch 1 case 2 null"));
		eq(
			list(map("cond", num(token("1", NUMBER, 8)), "cases",
				list(map("cond", num(token("2", NUMBER, 15)), "cmd", exp(nil(17)))),
				"elcmd", exp(nil(27)))), ex("switch 1 case 2 null else null"));
		eq(
			list(map(
				"cond",
				num(token("1", NUMBER, 8)),
				"cases",
				list(map("cond", num(token("2", NUMBER, 15)), "cmd", exp(nil(17))),
					map("cond", num(token("3", NUMBER, 27)), "cmd", exp(nil(29)))),
				"elcmd", null)), ex("switch 1 case 2 null case 3 null"));
	}
	
	/**
	 * Testador
	 */
	@Test
	public void whileCmd() {
		eq(
			list(map("cond", bool(token("true", TRUE, 7), true), "cmd", exp(nil(12)))),
			ex("while true null"));
	}
	
	/**
	 * Testador
	 */
	@Test
	public void ifCmd() {
		eq(
			list(map("cond", bool(token("true", TRUE, 4), true), "cmd", exp(nil(9)),
				"ecmd", null)), ex("if true null"));
	}
	
	/**
	 * Testador
	 */
	@Test
	public void blockCmd() {
		eq(list(map("cmds", list(exp(nil(2)), exp(nil(7))), "vars", list())),
			ex("{null null}"));
	}
	
	/**
	 * Cria um nó
	 * 
	 * @return node
	 */
	protected Map<Object, Object> exp(Map<Object, Object> exp) {
		Map<Object, Object> map = map("left", exp);
		return map;
	}
	
	/**
	 * Cria um nó
	 * 
	 * @return node
	 */
	protected Map<Object, Object> uny(Map<Object, Object> left) {
		@SuppressWarnings("unchecked")
		Map<Object, Object> token = (Map<Object, Object>) left.get("token");
		return uny(token, left);
	}
	
	/**
	 * Cria um nó
	 * 
	 * @return node
	 */
	protected Map<Object, Object> uny(Map<Object, Object> token,
		Map<Object, Object> left) {
		Map<Object, Object> map = rvalue(token);
		map.put("left", left);
		return map;
	}
	
	/**
	 * Cria um nó
	 * 
	 * @return node
	 */
	protected Map<Object, Object> bin(Map<Object, Object> token,
		Map<Object, Object> left, Map<Object, Object> right) {
		Map<Object, Object> map = uny(token, left);
		map.put("right", right);
		return map;
	}
	
	/**
	 * Cria um nó
	 * 
	 * @return node
	 */
	protected Map<Object, Object> ter(Map<Object, Object> token,
		Map<Object, Object> left, Map<Object, Object> right,
		Map<Object, Object> center) {
		Map<Object, Object> map = bin(token, left, right);
		map.put("center", center);
		return map;
	}
	
	/**
	 * Cria um nó de number
	 * 
	 * @param value
	 * @return node
	 */
	protected Map<Object, Object> num(Map<Object, Object> token) {
		Map<Object, Object> map = primitive(token);
		return map;
	}
	
	/**
	 * Primitive
	 * 
	 * @param value
	 * @param col
	 * @return
	 */
	private Map<Object, Object> primitive(Map<Object, Object> token) {
		Map<Object, Object> map = rvalue(token);
		return map;
	}
	
	/**
	 * RValue
	 * 
	 * @param value
	 * @param col
	 * @return
	 */
	private Map<Object, Object> rvalue(Map<Object, Object> token) {
		Map<Object, Object> map = value(token);
		return map;
	}
	
	/**
	 * RValue
	 * 
	 * @param value
	 * @param col
	 * @return
	 */
	private Map<Object, Object> lvalue(Map<Object, Object> token) {
		Map<Object, Object> map = value(token);
		return map;
	}
	
	/**
	 * Value
	 * 
	 * @param value
	 * @param col
	 * @return
	 */
	private Map<Object, Object> value(Map<Object, Object> token) {
		return map("token", token, "type", null);
	}
	
	/**
	 * Cria um nó de number
	 * 
	 * @param value
	 * @return node
	 */
	protected Map<Object, Object> id(String name, long col) {
		Map<Object, Object> map = map("token", tid(name, col));
		map.put("type", null);
		return map;
	}
	
	/**
	 * Cria um nó de number
	 * 
	 * @param value
	 * @return node
	 */
	protected Map<Object, Object> tid(String name, long col) {
		return token(name, 1, col);
	}
	
	/**
	 * Cria um nó de string
	 * 
	 * @param value
	 * @return node
	 */
	protected Map<Object, Object> str(Map<Object, Object> token) {
		Map<Object, Object> map = primitive(token);
		return map;
	}
	
	/**
	 * Cria um nó de boolean
	 * 
	 * @param value
	 * @return node
	 */
	protected Map<Object, Object> bool(Map<Object, Object> token, boolean flag) {
		Map<Object, Object> map = primitive(token);
		map.put("value", flag);
		return map;
	}
	
	/**
	 * Cria um nó de null
	 * 
	 * @param value
	 * @return node
	 */
	protected Map<Object, Object> nil(long col) {
		Map<Object, Object> nil = token("null", 45, col);
		return primitive(nil);
	}
	
	/**
	 * Cria um nó de ridentify
	 * 
	 * @param value
	 * @return node
	 */
	protected Map<Object, Object> rid(String name, long col) {
		Map<Object, Object> token = token(name, 1, col);
		return rvalue(token);
	}
	
	/**
	 * Cria um nó de ridentify
	 * 
	 * @param value
	 * @return node
	 */
	protected Map<Object, Object> lid(String name, long col) {
		Map<Object, Object> token = token(name, 1, col);
		return lid(token);
	}
	
	/**
	 * Cria um nó de ridentify
	 * 
	 * @param value
	 * @return node
	 */
	protected Map<Object, Object> lid(Map<Object, Object> token) {
		return lvalue(token);
	}
	
	/**
	 * Cria um nó de ridentify
	 * 
	 * @param value
	 * @return node
	 */
	protected Map<Object, Object> call(String name, long col, Object... params) {
		Map<Object, Object> token = token(name, 1, col);
		Map<Object, Object> map = rvalue(token);
		map.put("params", list(params));
		return map;
	}
	
	/**
	 * Cria um nó de ridentify
	 * 
	 * @param value
	 * @return node
	 */
	protected Map<Object, Object> numtypeNode(String name, long col) {
		return map("token", token("num", 5, col));
	}
	
	/**
	 * Cria um nó de ridentify
	 * 
	 * @param value
	 * @return node
	 */
	protected Map<Object, Object> assign(Map<Object, Object> left, Object value) {
		@SuppressWarnings("unchecked")
		Map<Object, Object> token = (Map<Object, Object>) left.get("token");
		Map<Object, Object> map = rvalue(token);
		map.put("left", left);
		map.put("right", value);
		return map;
	}
	
	/**
	 * Cria um nó de ridentify
	 * 
	 * @param value
	 * @return node
	 */
	protected Map<Object, Object> def(Object token, Object type, Object left,
		Object value) {
		return map("token", token, "type", type, "left", left, "right", value);
	}
	
	/**
	 * Executa o código
	 * 
	 * @param code
	 * @return resultado
	 */
	public Object ex(String code) {
		return c.executeScript("return JSON.parse(JSON.stringify(_agb_syntax(_agb_lexical(\""
				+ code + "\"))))");
	}
	
}
