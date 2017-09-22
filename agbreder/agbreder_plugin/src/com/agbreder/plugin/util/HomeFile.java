package com.agbreder.plugin.util;

import java.io.File;

public class HomeFile extends File {

	private static final File DIR;

	static {
		if (SoUtil.isUnix()) {
			String env = null;
			if (new File("~").exists()) {
				env = new File("~/").getAbsolutePath();
			}
			if (env == null) {
				env = System.getenv("HOME");
				if (env == null) {
					env = System.getenv("home");
				}
			}
			if (env == null) {
				DIR = new File("./");
			} else {
				DIR = new File(env);
			}
		} else if (SoUtil.isWindow()) {
			DIR = new File("c:\\");
		} else {
			throw new RuntimeException("OS not supported");
		}
	}

	public HomeFile() {
		super(DIR.getAbsolutePath());
	}

}
