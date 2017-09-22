package com.agbreder.plugin.wizard.component;

public class ClassUtil {

	public static boolean isValid(String classname) {
		char[] name = classname.toCharArray();
		if (name.length == 0)
			return false;
		if (!Character.isJavaIdentifierStart(name[0]))
			return false;
		for (int n = 1; n < name.length; n++) {
			char c = name[n];
			if (!Character.isJavaIdentifierPart(c))
				return false;
		}
		return true;
	}

}
