package com.agbreder.plugin.wizard.component;

public class PackageUtil {

	public static boolean isValid(String name) {
		if (name.length() == 0)
			return false;
		if (!Character.isJavaIdentifierStart(name.charAt(0)))
			return false;
		if (!Character.isJavaIdentifierPart(name.charAt(name.length() - 1)))
			return false;
		for (int n = 1; n < name.length(); n++) {
			char c = name.charAt(n);
			if (!(Character.isJavaIdentifierPart(c) || c == '.'))
				return false;
		}
		return true;
	}

}
