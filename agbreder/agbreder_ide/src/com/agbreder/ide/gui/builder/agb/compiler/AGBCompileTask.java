package com.agbreder.ide.gui.builder.agb.compiler;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.agbreder.compiler.AGBCompilerHelper;
import com.agbreder.compiler.exception.AGBException;
import com.agbreder.ide.gui.destkop.IDesktop;
import com.agbreder.ide.gui.util.task.WriteTask;
import com.agbreder.ide.model.io.IFile;
import com.agbreder.ide.model.io.IFileEvent;
import com.agbreder.ide.model.io.IFileSystem;
import com.agbreder.ide.model.io.IFolder;

/**
 * Compila o código fonte
 * 
 * @author bernardobreder
 */
public class AGBCompileTask extends WriteTask {

  /** Project */
  private final IFolder project;

  /**
   * Construtor
   * 
   * @param project
   */
  public AGBCompileTask(IFolder project) {
    this.project = project;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void action() throws Throwable {
    IFile binaryFile = project.getFolder("bin").getFile("binary.agbc");
    binaryFile.delete();
    String binaryPath = binaryFile.getAbsoluteName();
    String sourcePath = project.getFolder("src").getAbsoluteName();
    try {
      AGBCompilerHelper.main(new String[] { "-o", binaryPath, sourcePath });
      if (binaryFile.exist()) {
        IFileEvent.DEFAULT.fireCreatedFile(binaryFile);
      }
      IDesktop.DEFAULT.getConsole().clean();
    }
    catch (AGBException e) {
      String message = e.getMessage();
      this.process(message);
    }
    // Process process =
    // new ProcessBuilder("/Users/bernardobreder/agblng/bin/agbc", "-o",
    // binaryPath, sourcePath).start();
    // process.waitFor();
    // this.read(process.getInputStream());
    // this.read(process.getErrorStream());
  }

  /**
   * Realiza a leitura da stream
   * 
   * @param input
   * @throws Exception
   */
  @SuppressWarnings("unused")
  private void read(InputStream input) throws Exception {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    for (int n; ((n = input.read()) != -1);) {
      output.write((char) n);
    }
    String[] lines = new String(output.toByteArray(), "utf-8").split("\n");
    for (String line : lines) {
      try {
        this.process(line);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Processa a resposta
   * 
   * @param line
   * @throws Exception
   */
  private void process(String line) throws Exception {
    int index = line.indexOf(':');
    if (index >= 0) {
      String prefix = line.substring(0, index);
      prefix = prefix.substring(1, prefix.length() - 1);
      String[] args = prefix.split(",");
      String file = args[0].substring(1, args[0].length() - 1);
      String token = args[1].substring(1, args[1].length() - 1);
      int lin = new Integer(args[2]);
      int col = new Integer(args[3]);
      String message = line.substring(index + 1);
      this.mark(file, token, lin, col, message);
    }
    IDesktop.DEFAULT.getConsole().setText(line);
  }

  /**
   * Marca um token
   * 
   * @param filename
   * @param token
   * @param lin
   * @param col
   * @param message
   */
  private void mark(String filename, String token, int lin, int col,
    String message) {
    IFile file = IFileSystem.DEFAULT.getFile(filename);
    IDesktop.DEFAULT.addMarker(file, lin, col, token.length(), message);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateUI() {
    project.getFolder("bin").refresh();
  }

}
