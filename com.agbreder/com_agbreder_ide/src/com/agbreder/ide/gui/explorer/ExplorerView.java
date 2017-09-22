package com.agbreder.ide.gui.explorer;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

import breder.util.util.ImageUtil;

import com.agbreder.ide.plugin.IView;

/**
 * Painel de Explorer
 * 
 * 
 * @author Bernardo Breder
 */
public class ExplorerView implements IView {

  /**
   * {@inheritDoc}
   */
  @Override
  public Component build() {
    return new JScrollPane(new ExplorerTree());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return "Explorer";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Icon getIcon() {
    return new ImageIcon(ImageUtil
      .load("/com/agbreder/ide/resource/explorer.png"));
  }

}
