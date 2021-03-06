package com.agbreder.ide.model.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface de arquivo
 * 
 * 
 * @author Bernardo Breder
 */
public interface IFile extends IResource {

  /**
   * Retorna o conteúdo do arquivo
   * 
   * @return conteúdo
   * @throws IOException
   */
  public InputStream getInputStream() throws IOException;

  /**
   * Retorna o conteúdo do arquivo
   * 
   * @return conteúdo
   */
  public String getInputString();

  /**
   * Retorna a saída do arquivo
   * 
   * @return saída do arquivo
   * @throws IOException
   */
  public OutputStream getOuputStream() throws IOException;

  /**
   * Escreve no arquivo
   * 
   * @param text
   */
  public void setOutputString(String text);

  /**
   * Indica se é executável
   * 
   * @return é executável
   */
  public boolean isExecutable();

  /**
   * Retorna a data da última modificação
   * 
   * @return data da última modificação
   */
  public long lastModified();

}
