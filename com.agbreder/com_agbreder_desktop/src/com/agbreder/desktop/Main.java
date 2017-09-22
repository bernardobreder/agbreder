package com.agbreder.desktop;

import breder.util.task.EventTask;

import com.agbreder.api.AGBreder;
import com.agbreder.api.AGListener;
import com.agbreder.api.AGMail;
import com.agbreder.desktop.gui.trayicon.AGBTrayIcon;
import com.agbreder.desktop.model.AGBrederLocator;
import com.agbreder.desktop.task.MessageTask;

/**
 * Inicializador
 * 
 * 
 * @author Bernardo Breder
 */
public class Main {

  /**
   * Inicializa
   * 
   * @param args
   */
  public static void main(String[] args) {
    Main.init();
    for (;;) {
      try {
        AGBrederLocator.instance = new AGBreder("bbreder", "bbreder");
        AGBrederLocator.instance.addListener(new AGListener() {
          @Override
          public void busy(AGMail mail) {
            new MessageTask(mail).start();
          }

          @Override
          public void offline() {
            AGBTrayIcon.offline();
          }

          @Override
          public void online() {
            AGBTrayIcon.online();
          }

          @Override
          public void handler(Throwable t) {
          }
        });
        break;
      }
      catch (Throwable e) {
        try {
          Thread.sleep(1000);
        }
        catch (InterruptedException e1) {
        }
      }
    }
  }

  /**
   * Inicializa o TrayIcon
   */
  public static void init() {
    EventTask.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        AGBTrayIcon.init();
      }
    });
  }

}
