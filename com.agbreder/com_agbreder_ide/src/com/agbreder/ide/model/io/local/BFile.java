package com.agbreder.ide.model.io.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.agbreder.ide.model.io.IFile;
import com.agbreder.ide.model.io.IFolder;


/**
 * Recurso de arquivo
 * 
 * 
 * @author Bernardo Breder
 */
public class BFile extends BResource implements IFile {

  /**
   * Construtor
   * 
   * @param file
   */
  private BFile(java.io.File file) {
    super(file);
  }

  /**
   * Cria uma instancia
   * 
   * @param file
   * @return instancia
   */
  public static IFile newInstance(File file) {
    IFile resource = new BFile(file);
    return resource;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InputStream getInputStream() throws IOException {
    return new FileInputStream(file);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OutputStream getOuputStream() throws IOException {
    return new FileOutputStream(file);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getLength() {
    return this.file.length();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete() {
    this.file.delete();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isExecutable() {
    return this.file.canExecute();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IFile rename(String name) throws IOException {
    File file = new File(this.file.getParent(), name);
    this.file.renameTo(file);
    return BFile.newInstance(file);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void move(IFolder dest) throws IOException {
    this.copy(dest);
    this.delete();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void copy(IFolder dest) throws IOException {
    OutputStream output = dest.createFile(this.getName()).getOuputStream();
    InputStream input = this.getInputStream();
    byte[] b = new byte[1024];
    while (true) {
      int n = input.read(b);
      if (n == -1) {
        break;
      }
      output.write(b, 0, n);
    }
    output.close();
    input.close();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long lastModified() {
    return this.file.lastModified();
  }

}
