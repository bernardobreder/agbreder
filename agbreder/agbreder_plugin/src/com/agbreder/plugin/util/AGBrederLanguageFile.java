package com.agbreder.plugin.util;

import java.io.File;

public class AGBrederLanguageFile extends File {

	public AGBrederLanguageFile() {
		super(new HomeFile(), "agblng");
	}

	public AGBrederLanguageFile(String... paths) {
		super(FileUtil.build(new File(new HomeFile(), "agblng"), paths).toString());
	}

}
