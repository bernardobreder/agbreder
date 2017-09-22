package com.agbreder.ide.gui.ide;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import breder.util.swing.BAction;
import breder.util.task.LocalTask;

import com.agbreder.ide.gui.view.ViewDialog;
import com.agbreder.ide.resource.Resource;

/**
 * Tabulação da IDE
 * 
 * 
 * @author Bernardo Breder
 */
public class IdeTabbed extends JPanel {

  /** Componente de Tabulação */
  private JTabbedPane tabbed;

  /**
   * Construtor
   */
  public IdeTabbed() {
    this.setLayout(new BorderLayout());
    this.add(this.buildTabbed(), BorderLayout.CENTER);
    this.add(this.buildButton(), BorderLayout.SOUTH);
  }

  /**
   * Constroi a tabulação
   * 
   * @return tabulação
   */
  private Component buildTabbed() {
    this.tabbed = new JTabbedPane();
    this.tabbed.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    this.tabbed.setPreferredSize(new Dimension(300, 50));
    return this.tabbed;
  }

  /**
   * Constroi os botões
   * 
   * @return componentes
   */
  private Component buildButton() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    {
      JButton c =
        new JButton(new BAction(
          new ImageIcon(Resource.getInstance().getPlus()), new LocalTask() {

            @Override
            public void updateUI() {
              onAddAction();
            }

          }));
      c.setRolloverEnabled(false);
      c.setPreferredSize(new Dimension(16, 16));
      c.setFocusable(false);
      panel.add(c);
    }
    {
      JButton c =
        new JButton(new BAction(
          new ImageIcon(Resource.getInstance().getMinus()), new LocalTask() {

            @Override
            public void updateUI() {
              onRemoveAction();
            }

          }));
      c.setRolloverEnabled(false);
      c.setPreferredSize(new Dimension(16, 16));
      c.setFocusable(false);
      panel.add(c);
    }
    return panel;
  }

  /**
   * Ação de adicionar
   */
  protected void onAddAction() {
    IdeFrame frame = IdeFrame.getInstance();
    new ViewDialog(frame, tabbed).setVisible(true);
  }

  /**
   * Ação de Remover
   */
  protected void onRemoveAction() {
    int index = this.tabbed.getSelectedIndex();
    if (index >= 0) {
      IdeFrame frame = IdeFrame.getInstance();
      if (JOptionPane
        .showConfirmDialog(frame, "Do want to remove a Tab ?", "Remove a Tab",
          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
        this.tabbed.removeTabAt(index);
      }
    }
  }

}
