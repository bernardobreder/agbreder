package com.agbreder.plugin.util;

import java.awt.event.KeyEvent;
import java.io.File;

public class SoUtil {

	public static final int CTRL_MASK = isMacOs() ? KeyEvent.META_MASK : KeyEvent.CTRL_MASK;

	public static final int SHIFT_MASK = KeyEvent.SHIFT_MASK;

	public static boolean isMacOs() {
		return isUnix() && new File("/Applications").exists();
	}

	public static boolean isWindow() {
		return !isUnix() && new File("C:\\").exists();
	}

	public static boolean isLinux() {
		return isUnix() && !isMacOs() && !isWindow();
	}

	public static boolean isUnix() {
		return File.listRoots().length == 1 && File.listRoots()[0].getAbsolutePath().equals("/") && new File("/usr").exists();
	}

	public static String getExtension() {
		if (isWindow()) {
			return ".exe";
		} else {
			return "";
		}
	}

	public static String getLibraryExtension() {
		return "." + getLibraryExtensionWithoutDot();
	}

	public static String getLibraryExtensionWithoutDot() {
		if (isWindow()) {
			return "dll";
		} else if (isMacOs()) {
			return "dylib";
		} else {
			return "so";
		}
	}

}
