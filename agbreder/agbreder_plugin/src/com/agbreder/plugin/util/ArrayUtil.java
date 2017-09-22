package com.agbreder.plugin.util;

public abstract class ArrayUtil {

	public static int find(Object[] objects, Object object) {
		for (int n = 0; n < objects.length; n++) {
			if (objects[n].equals(object))
				return n;
		}
		return -1;
	}

}
