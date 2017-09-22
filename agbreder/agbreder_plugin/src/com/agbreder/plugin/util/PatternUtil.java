package com.agbreder.plugin.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {

	public static String get(String format, String value) {
		return get(format, value, 1);
	}

	public static String get(String format, String value, int index) {
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(value);
		if (matcher.find()) {
			return matcher.group(index);
		} else {
			return null;
		}
	}

}
