package com.agbreder.ide.gui.ide;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import breder.util.swing.BAction;
import breder.util.swing.BFrame;
import breder.util.task.FinishFrameTask;

import com.agbreder.ide.gui.editor.UIDefaultEditor;
import com.agbreder.ide.plugin.IMenu;
import com.agbreder.ide.plugin.IMenuItem;

/**
 * Janela da IDE
 * 
 * 
 * @author Bernardo Breder
 */
public class IdeFrame extends BFrame {

  /** Instancia Unica */
  private static final IdeFrame INSTANCE = new IdeFrame();
  /** Barra de Menu */
  private JMenuBar menuBar;
  /** Tabulação Central */
  private JTabbedPane centerTab;
  /** Tabulação Left */
  private IdeTabbed leftTab;
  /** Tabulação Right */
  private IdeTabbed rightTopTab;
  /** Tabulação Right */
  private IdeTabbed rightBottomTab;

  /**
   * Construtor
   */
  private IdeFrame() {
    super(null);
    this.buildMenus();
    this.buildComponents();
    this.pack();
    this.setMinimumSize(this.getSize());
    this.setSize(800, 600);
    this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    this.setLocationRelativeTo(null);
  }

  /**
   * Constroi os menus
   */
  private void buildMenus() {
    this.menuBar = new JMenuBar();
    this.menuBar.add(this.buildFileMenu());
    this.setJMenuBar(this.menuBar);
  }

  /**
   * Menu de Arquivo
   * 
   * @return arquivo
   */
  private JMenu buildFileMenu() {
    JMenu menu = new JMenu("File");
    menu.add(new BAction("Exit", new FinishFrameTask(this)));
    return menu;
  }

  /**
   * Constroi os componentes
   */
  private void buildComponents() {
    this.setLayout(new BorderLayout());
    this.add(this.buildCenter(), BorderLayout.CENTER);
    this.add(this.buildLeft(), BorderLayout.WEST);
    this.add(this.buildRight(), BorderLayout.EAST);
  }

  /**
   * Controi o componente do meio
   * 
   * @return component
   */
  private Component buildCenter() {
    this.centerTab = new JTabbedPane();
    this.centerTab.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
    return centerTab;
  }

  /**
   * Controi o componente da esquerda
   * 
   * @return component
   */
  private Component buildLeft() {
    this.leftTab = new IdeTabbed();
    return leftTab;
  }

  /**
   * Controi o componente da direita
   * 
   * @return component
   */
  private Component buildRight() {
    JPanel panel = new JPanel(new GridLayout(2, 1));
    panel.add(this.rightTopTab = new IdeTabbed());
    panel.add(this.rightBottomTab = new IdeTabbed());
    return panel;
  }

  /**
   * Retorna instancia unica
   * 
   * @return this
   */
  public static IdeFrame getInstance() {
    return IdeFrame.INSTANCE;
  }

  /**
   * Adiciona um menu
   * 
   * @param item
   */
  public void addMenuItem(IMenuItem item) {
    JMenu menu = this.getMenu(item.getMenu());
    for (int n = 0; n < menu.getItemCount(); n++) {
      JMenuItem mm = menu.getItem(n);
      if (mm instanceof JMenu && mm.getName().equals(menu.getName())) {
        return;
      }
    }
    JMenuItem mm = new JMenuItem(item.getName());
    menu.add(mm);
  }

  /**
   * Abre um editor
   * 
   * @param editor
   */
  public void addEditor(String title, UIDefaultEditor editor) {
    this.centerTab.addTab(title, editor);
  }

  /**
   * Retorna o menu
   * 
   * @param menu
   * @return menu
   */
  private JMenu getMenu(IMenu menu) {
    if (menu.getParent() != null) {
      JMenu m = this.getMenu(menu.getParent());
      for (int n = 0; n < m.getItemCount(); n++) {
        JMenuItem mm = m.getItem(n);
        if (mm instanceof JMenu && mm.getText().equals(menu.getName())) {
          return m;
        }
      }
      JMenu mm = new JMenu(menu.getName());
      m.add(mm);
      return mm;
    }
    else {
      for (int n = 0; n < this.menuBar.getMenuCount(); n++) {
        JMenu m = this.menuBar.getMenu(n);
        if (m.getText().equals(menu.getName())) {
          return m;
        }
      }
      JMenu m = new JMenu(menu.getName());
      this.menuBar.add(m);
      return m;
    }
  }

}
