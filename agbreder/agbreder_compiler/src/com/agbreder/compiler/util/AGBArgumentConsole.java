package com.agbreder.compiler.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Console de Compilador
 * 
 * @author bernardobreder
 */
public class AGBArgumentConsole {

	/** Mapa */
	private Map<String, String> map = new HashMap<String, String>();

	/** Lista */
	private List<String> list = new ArrayList<String>();

	/**
	 * Construtor
	 * 
	 * @param args
	 */
	public AGBArgumentConsole(String[] args) {
		for (int n = 0; n < args.length; n++) {
			String arg = args[n];
			if (arg.startsWith("-") && n != args.length - 1) {
				String key = arg.substring(1);
				String value = args[++n];
				map.put(key, value);
			} else {
				list.add(arg);
			}
		}
		map = Collections.unmodifiableMap(map);
		list = Collections.unmodifiableList(list);
	}

	/**
	 * Retorna o valor de uma chave
	 * 
	 * @param key
	 * @return valor de uma chave
	 */
	public String get(String key) {
		return map.get(key);
	}

	/**
	 * Indica se exite um valor de uma chave
	 * 
	 * @param key
	 * @return exite um valor de uma chave
	 */
	public boolean has(String key) {
		return map.containsKey(key);
	}

	/**
	 * Retorna os valores que não estão associado a uma chave
	 * 
	 * @return valores que não estão associado a uma chave
	 */
	public List<String> gets() {
		return list;
	}

}
