package com.agbreder.ide.gui.ide;

import javax.swing.JDialog;
import javax.swing.JFrame;

import breder.util.swing.BImage;
import breder.util.util.ImageUtil;

/**
 * Dialogo de carga da IDE
 * 
 * 
 * @author Bernardo Breder
 */
public class IdeLoaderFrame extends JDialog {

  /** Instancia default */
  private static IdeLoaderFrame instance = new IdeLoaderFrame();

  /**
   * Construtor
   */
  private IdeLoaderFrame() {
    this
      .add(new BImage(ImageUtil.load("/com/agbreder/ide/resource/loader.jpg")));
    this.setUndecorated(true);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setSize(320, 240);
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }

  /**
   * Fecha a janela
   */
  public void close() {
    this.dispose();
    instance = null;
  }

  /**
   * Retorna
   * 
   * @return instance
   */
  public static IdeLoaderFrame getInstance() {
    return instance;
  }

}
