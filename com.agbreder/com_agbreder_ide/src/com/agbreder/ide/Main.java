package com.agbreder.ide;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.MalformedURLException;

import com.agbreder.ide.gui.explorer.ExplorerPlugin;
import com.agbreder.ide.gui.ide.IdeFrame;
import com.agbreder.ide.gui.ide.IdeLoaderFrame;
import com.agbreder.ide.plugin.PluginStarter;
import com.agbreder.ide.plugin.agb.editor.AgbPlugin;

import breder.util.lookandfeel.LookAndFeel;
import breder.util.swing.BExceptonFrame;
import breder.util.task.EventTask;

/**
 * Inicializa a IDE
 * 
 * 
 * @author Bernardo Breder
 */
public class Main {

  /**
   * Inicializador
   * 
   * @param args
   * @throws IOException
   * @throws MalformedURLException
   * @throws IllegalStateException
   * @throws NullPointerException
   */
  public static void main(String[] args) throws NullPointerException,
    IllegalStateException, MalformedURLException, IOException {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
      @Override
      public void uncaughtException(Thread t, Throwable e) {
        new BExceptonFrame(e).setVisible(true);
      }
    });
    EventTask.invokeLater(new Runnable() {
      @Override
      public void run() {
        IdeLoaderFrame.getInstance().setVisible(true);
      }
    });
    EventTask.invokeLater(new Runnable() {
      @Override
      public void run() {
        LookAndFeel.getInstance().installMetal();
      }
    });
    {
      EventTask.invokeLater(new PluginStarter(new ExplorerPlugin()));
      EventTask.invokeLater(new PluginStarter(new AgbPlugin()));
    }
    EventTask.invokeLater(new Runnable() {
      @Override
      public void run() {
        IdeLoaderFrame.getInstance().close();
      }
    });
    EventTask.invokeLater(new Runnable() {
      @Override
      public void run() {
        IdeFrame.getInstance().setVisible(true);
      }
    });
  }
}
