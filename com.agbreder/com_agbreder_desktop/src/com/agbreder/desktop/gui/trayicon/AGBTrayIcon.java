package com.agbreder.desktop.gui.trayicon;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import breder.util.task.EventTask;

import com.agbreder.desktop.gui.publish.PublishTask;
import com.agbreder.desktop.resource.Resource;

/**
 * TrayIcon do aplicativo
 * 
 * 
 * @author Bernardo Breder
 */
public class AGBTrayIcon {

  /** Trayicon */
  private static TrayIcon trayIcon;
  /** Busy Count */
  private static int count = 0;

  /**
   * Inicializa
   */
  public static void init() {
    try {
      Image image = Resource.getInstance().getTrayIconShutdown();
      String tooltip = "Agent Breder Desktop";
      PopupMenu popup = new PopupMenu();
      trayIcon = new TrayIcon(image, tooltip, popup);
      SystemTray.getSystemTray().add(trayIcon);
      {
        {
          MenuItem menu = new MenuItem("Publish");
          menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              new PublishTask().start();
            }
          });
          popup.add(menu);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Indica que o usuário está online
   */
  public static void online() {
    EventTask.invokeLater(new Runnable() {
      @Override
      public void run() {
        Image image = Resource.getInstance().getTrayIconOnline();
        trayIcon.setImage(image);
      }
    });
  }

  /**
   * Indica que o usuário está ocupado
   */
  public static void busy() {
    EventTask.invokeLater(new Runnable() {
      @Override
      public void run() {
        count++;
        Image image = Resource.getInstance().getTrayIconBusy();
        trayIcon.setImage(image);
      }
    });
  }

  /**
   * Indica que o usuário está ocupado
   */
  public static void unbusy() {
    EventTask.invokeLater(new Runnable() {
      @Override
      public void run() {
        if (--count == 0) {
          Image image = Resource.getInstance().getTrayIconOnline();
          trayIcon.setImage(image);
        }
      }
    });
  }

  /**
   * Indica que o usuário está online
   */
  public static void offline() {
    EventTask.invokeLater(new Runnable() {
      @Override
      public void run() {
        Image image = Resource.getInstance().getTrayIconOffline();
        trayIcon.setImage(image);
      }
    });
  }

}
