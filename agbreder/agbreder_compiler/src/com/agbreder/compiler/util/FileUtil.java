package com.agbreder.compiler.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitária para sistema de arquivo
 * 
 * @author bernardobreder
 */
public class FileUtil {

	/**
	 * Cria um arquivo temporário com um conteúdo
	 * 
	 * @param code
	 * @return arquivo temporario
	 * @throws IOException
	 */
	public static File createTempFile(String code) throws IOException {
		File file = File.createTempFile("agb", "temp");
		{
			file.deleteOnExit();
			FileOutputStream output = new FileOutputStream(file);
			try {
				output.write(code.getBytes());
			} finally {
				output.close();
			}
		}
		return file;
	}

	/**
	 * Lista os arquivos que possui uma extensão
	 * 
	 * @param dir
	 * @param ext
	 * @return arquivos que possui uma extensão
	 */
	public static List<File> list(File dir, String ext) {
		List<File> list = new ArrayList<File>();
		list(dir, ext, list);
		return list;
	}

	private static void list(File dir, String ext, List<File> list) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					list(file, ext, list);
				} else if (file.isFile()) {
					if (file.getName().endsWith("." + ext)) {
						list.add(file);
					}
				}
			}
		}
	}

}
