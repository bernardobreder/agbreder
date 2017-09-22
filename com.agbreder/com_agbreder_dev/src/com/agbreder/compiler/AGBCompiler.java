package com.agbreder.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import net.sourceforge.htmlunit.corejs.javascript.tools.shell.Global;
import net.sourceforge.htmlunit.corejs.javascript.tools.shell.ShellContextFactory;

/**
 * Compilador da Linguagem Agent Breder Language
 * 
 * @author bernardobreder
 */
public class AGBCompiler {
	
	/** Variável de ambiente do JavaScript */
	private static Global env;
	
	static {
		try {
			initFromResource();
		} catch (Throwable e) {
			throw new Error(e);
		}
	}
	
	/**
	 * Inicializa a classe
	 * 
	 * @param input
	 * @throws IOException
	 */
	public static void initFromResource() throws IOException {
		init(AGBCompiler.class
			.getResourceAsStream("/com/agbreder/compiler/compiler.pack.js"));
	}
	
	/**
	 * Inicializa a classe
	 * 
	 * @param input
	 * @throws IOException
	 */
	public static void initFromWeb() throws IOException {
		initFromWeb("www.breder.org", 80);
	}
	
	/**
	 * Inicializa a classe
	 * 
	 * @param input
	 * @throws IOException
	 */
	public static void initFromWeb(String host, int port) throws IOException {
		init(new URL("http://" + host + ":" + port + "/agbreder/pub/js/compiler.js")
			.openStream());
	}
	
	/**
	 * Inicializa a classe
	 * 
	 * @param input
	 * @throws IOException
	 */
	public static void init(InputStream input) throws IOException {
		if (input == null) {
			throw new IllegalArgumentException("input");
		}
		StringBuilder sb = new StringBuilder(8 * 1024);
		for (int n; (n = input.read()) != -1;) {
			sb.append((char) n);
		}
		final String CODE = sb.toString();
		ShellContextFactory shell = new ShellContextFactory();
		shell.call(new ContextAction() {
			
			@Override
			public Object run(Context c) {
				Script script = c.compileString(CODE, "compiler", 1, null);
				return script.exec(c, env = new Global(c));
			}
			
		});
	}
	
	/**
	 * Compila os códigos fontes
	 * 
	 * @param sources
	 * @return
	 */
	public static String compile(String... sources) {
		StringBuilder sb = new StringBuilder();
		sb.append("var sources = [];\n");
		for (String source : sources) {
			sb.append("sources.push(\"" + source + "\");\n");
		}
		sb.append("agbc(sources);\n");
		Object result = script(sb.toString());
		if (result == null) {
			return null;
		}
		return result.toString();
	}
	
	/**
	 * Compila os códigos fontes
	 * 
	 * @param bytecode
	 * @return bytecode
	 */
	public static Object execute(String bytecode) {
		return script("agb(\"" + bytecode + "\")");
	}
	
	/**
	 * Compila os códigos fontes
	 * 
	 * @param sources
	 * @return
	 */
	private static Object script(String... sources) {
		if (env == null) {
			throw new IllegalStateException("not inited");
		}
		StringBuilder sb = new StringBuilder();
		for (String source : sources) {
			sb.append(source);
		}
		final String code = sb.toString();
		ShellContextFactory shell = new ShellContextFactory();
		Object call = shell.call(new ContextAction() {
			
			@Override
			public Object run(Context c) {
				Script script = c.compileString(code, "script", 1, null);
				return script.exec(c, env);
			}
			
		});
		return cast(call);
	}
	
	/**
	 * Troca o objeto html para java
	 * 
	 * @param value
	 * @return novo objeto
	 */
	private static Object cast(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof ScriptableObject) {
			ScriptableObject object = (ScriptableObject) value;
			String className = object.getClassName();
			if (className.equals("Array")) {
				NativeArray old = (NativeArray) value;
				List<Object> result = new ArrayList<Object>(old.size());
				for (Object item : old) {
					result.add(cast(item));
				}
				return result;
			} else if (className.equals("Object")) {
				NativeObject old = (NativeObject) value;
				Map<Object, Object> result = new HashMap<Object, Object>();
				for (Object key : old.keySet()) {
					result.put(cast(key), cast(old.get(key)));
				}
				return result;
			} else if (className.equals("Boolean")) {
				return ScriptRuntime.toBoolean(object);
			} else if (className.equals("Integer")) {
				return ScriptRuntime.toInteger(object);
			} else if (className.equals("Number")) {
				return ScriptRuntime.toNumber(object);
			} else if (className.equals("String")) {
				return ScriptRuntime.toString(object);
			} else {
				return object.toString();
			}
		} else if (value.getClass().getName().equals(Undefined.class.getName())) {
			return null;
		} else if (value instanceof Integer) {
			value = ((Integer) value).doubleValue();
		} else if (value instanceof Long) {
			value = ((Long) value).doubleValue();
		} else if (value instanceof Float) {
			value = ((Float) value).doubleValue();
		}
		return value;
	}
	
	/**
	 * Testador
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		for (;;) {
			long timer = System.currentTimeMillis();
			Object value = execute(compile("return null"));
			timer = System.currentTimeMillis() - timer;
			System.out.println(String.format("%s in %d milisegs", value, timer));
		}
	}
	
}
