package com.agbreder.api.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Classe utilitária de InputStream
 * 
 * 
 * @author Bernardo Breder
 */
public class InputStreamUtil {

  /**
   * Retorna os bytes
   * 
   * @param input
   * @return bytes
   * @throws IOException
   */
  public static byte[] getBytes(InputStream input) throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    InputStreamUtil.copyStream(input, output);
    return output.toByteArray();
  }

  /**
   * Realiza uma cópia de stream
   * 
   * @param input
   * @param output
   * @throws IOException
   */
  public static void copyStream(InputStream input, OutputStream output)
    throws IOException {
    byte[] bytes = new byte[1024];
    while (true) {
      int n = input.read(bytes);
      if (n == -1) {
        break;
      }
      output.write(bytes, 0, n);
    }
  }

  /**
   * Dezipa uma stream para um diretório
   * 
   * @param input
   * @param dir
   * @throws IOException
   */
  public static void extractZip(ZipInputStream input, File dir)
    throws IOException {
    if (dir.exists() && !dir.isDirectory()) {
      throw new IllegalArgumentException("dir is not a folder : "
        + dir.getAbsolutePath());
    }
    for (;;) {
      ZipEntry entity = input.getNextEntry();
      if (entity == null) {
        break;
      }
      File file = new File(dir, entity.getName());
      if (entity.isDirectory()) {
        file.mkdirs();
      }
      else {
        FileOutputStream output = new FileOutputStream(file);
        InputStreamUtil.copyStream(input, output);
        output.close();
      }
      input.closeEntry();
    }
  }

  /**
   * Retorna o recurso
   * 
   * @param resource
   * @return recurso
   */
  public static InputStream getResource(String resource) {
    InputStream input = InputStreamUtil.class.getResourceAsStream(resource);
    if (input == null) {
      input =
        InputStreamUtil.class.getClassLoader().getResourceAsStream(resource);
    }
    return input;
  }

}
