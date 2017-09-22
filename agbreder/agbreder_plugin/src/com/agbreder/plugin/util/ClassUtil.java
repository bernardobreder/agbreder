package com.agbreder.plugin.util;

public class ClassUtil {

	public static boolean isInstanceof(Class<?> c1, Class<?> c2) {
		try {
			return c1.asSubclass(c2) != null;
		} catch (ClassCastException e) {
			return false;
		}
	}

	public static boolean isPrimitiveSql(Class<?> c) {
		return c.isPrimitive() || c == String.class || c == Number.class || c == Integer.class || c == Long.class || c == Float.class || c == Double.class || c == Boolean.class || c == Character.class || c == Byte.class || c == Short.class;
	}

}
