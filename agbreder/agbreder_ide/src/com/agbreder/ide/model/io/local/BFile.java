package com.agbreder.ide.model.io.local;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.agbreder.ide.model.io.IFile;
import com.agbreder.ide.model.io.IFileEvent;
import com.agbreder.ide.model.io.IFolder;

/**
 * Recurso de arquivo
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
  public String getInputString() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    InputStream input = null;
    try {
      input = this.getInputStream();
      for (int n; ((n = input.read()) != -1);) {
        output.write(n);
      }
    }
    catch (IOException e) {
      e.printStackTrace();
      try {
        input.close();
      }
      catch (Exception e1) {
      }
    }
    try {
      return new String(output.toByteArray(), "utf-8");
    }
    catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return new String(output.toByteArray());
    }
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
  public void setOutputString(String text) {
    try {
      OutputStream output = this.getOuputStream();
      output.write(text.getBytes("utf-8"));
      output.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
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
    IFileEvent.DEFAULT.fireDeletedFile(this);
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
    IFile result = BFile.newInstance(file);
    IFileEvent.DEFAULT.fireChangedFile(this);
    return result;
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
    IFile createFile = dest.createFile(this.getName());
    IFileEvent.DEFAULT.fireCreatedFile(createFile);
    OutputStream output = createFile.getOuputStream();
    InputStream input = this.getInputStream();
    try {
      byte[] b = new byte[1024];
      while (true) {
        int n = input.read(b);
        if (n == -1) {
          break;
        }
        output.write(b, 0, n);
      }
    }
    finally {
      output.close();
      input.close();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long lastModified() {
    return this.file.lastModified();
  }

}
