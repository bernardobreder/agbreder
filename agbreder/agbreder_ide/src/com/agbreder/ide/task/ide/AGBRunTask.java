package com.agbreder.ide.task.ide;

import java.io.InputStream;

import breder.util.task.EventTask;

import com.agbreder.ide.gui.destkop.IDesktop;
import com.agbreder.ide.gui.util.task.ReadTask;
import com.agbreder.ide.model.io.IFile;
import com.agbreder.ide.model.io.IFolder;

/**
 * Executa o binario
 * 
 * 
 * @author bbreder
 */
public class AGBRunTask extends ReadTask {

  /** Projeto */
  private final IFolder project;

  /**
   * Construtor
   * 
   * @param project
   */
  public AGBRunTask(IFolder project) {
    this.project = project;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean accept() {
    IDesktop.DEFAULT.getConsole().clean();
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void action() throws Throwable {
    IFolder binaryFolder = this.project.getFolder("bin");
    IFile binaryFile = binaryFolder.getFile("binary.agbc");
    if (binaryFile.exist()) {
      final StringBuilder sb = new StringBuilder();
      Thread thread = new AGBRunThread(sb);
      try {
        thread.start();
        Process process =
          new ProcessBuilder("../agbreder_vm/Debug/agb", binaryFile
            .getAbsoluteName(), "Main").start();
        {
          InputStream input = process.getInputStream();
          for (int n; ((n = input.read()) != -1);) {
            synchronized (sb) {
              sb.append((char) n);
            }
          }
        }
        process.waitFor();
      }
      finally {
        thread.interrupt();
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateUI() {
  }

  /**
   * Classe que transfere a stream do processo para o console da ide
   * 
   * 
   * @author bbreder
   */
  private final class AGBRunThread extends Thread {

    /** Buffer */
    private final StringBuilder sb;

    /**
     * Construtor
     * 
     * @param sb
     */
    private AGBRunThread(StringBuilder sb) {
      super(AGBRunTask.class.getSimpleName() + ".Console");
      this.sb = sb;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
      for (;;) {
        sendChars();
        if (Thread.interrupted()) {
          sendChars();
          break;
        }
        try {
          Thread.sleep(100);
        }
        catch (InterruptedException e) {
          this.interrupt();
        }
      }
    }

    /**
     * Envia para o console
     */
    private void sendChars() {
      final String text;
      synchronized (sb) {
        text = sb.toString();
        sb.delete(0, sb.length());
      }
      EventTask.invokeLater(new Runnable() {
        @Override
        public void run() {
          IDesktop.DEFAULT.getConsole().append(text);
        }
      });
    }
  }

}
