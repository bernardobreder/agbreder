package com.agbreder.ide.gui.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.agbreder.ide.plugin.IView;

import breder.util.swing.BAction;
import breder.util.swing.BDialog;
import breder.util.swing.BFrame;
import breder.util.swing.BFrameUtil;
import breder.util.swing.tree.BTree;
import breder.util.task.FinishFrameTask;
import breder.util.task.LocalTask;

/**
 * Dialogo de visão
 * 
 * 
 * @author Bernardo Breder
 */
public class ViewDialog extends BDialog {

  /** Tabulação que irá atuar */
  private JTabbedPane tabbed;
  /** Arvore que será exibido */
  private BTree tree;

  /**
   * Construtor
   * 
   * @param frame
   * @param tabbed
   */
  public ViewDialog(BFrame frame, JTabbedPane tabbed) {
    super(frame);
    this.tabbed = tabbed;
    this.build();
    this.pack();
    this.setMinimumSize(this.getSize());
    this.setSize(400, 500);
    this.setLocationRelativeTo(null);
    this.setModal(true);
  }

  /**
   * Constroi todos os componentes
   */
  private void build() {
    this.setLayout(new BorderLayout());
    JScrollPane scroll = new JScrollPane(this.buildTree());
    scroll.setBorder(BorderFactory.createEmptyBorder());
    this.add(scroll, BorderLayout.CENTER);
    this.add(this.buildButton(), BorderLayout.SOUTH);
  }

  /**
   * Constroi a arvore
   * 
   * @return arvore
   */
  private Component buildTree() {
    return this.tree = new BTree(new ViewModel());
  }

  /**
   * Constroi os botões
   * 
   * @return botões
   */
  private Component buildButton() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    {
      JButton c = new JButton(new BAction("Ok", new LocalTask() {

        @Override
        public void updateUI() {
          onOkAction();
        }
      }));
      this.getRootPane().setDefaultButton(c);
      panel.add(c);
    }
    {
      JButton c = new JButton(new BAction("Cancel", new FinishFrameTask(this)));
      BFrameUtil.confEsq(this);
      panel.add(c);
    }
    return panel;
  }

  /**
   * Ação de Ok
   */
  protected void onOkAction() {
    ViewNode node =
      (ViewNode) this.tree.getSelectionPath().getLastPathComponent();
    IView view = node.getView();
    int index = this.tabbed.indexOfTab(view.getName());
    if (index < 0) {
      tabbed.addTab(view.getName(), view.build());
    }
    this.close();
  }

}
