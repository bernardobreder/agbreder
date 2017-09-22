package com.agbreder.ide.gui.builder.agb.project;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import breder.util.swing.BFrame;
import breder.util.swing.GBC;

/**
 * Janela de criação de uma Classe Agent Breder
 * 
 * @author bernardobreder
 */
public class AGBProjectBuilderFrame extends BFrame {

  /** Field */
  private JTextField projectField;
  /** Field */
  private JButton buildButton;

  /**
   * Construtor
   * 
   * @param parent
   */
  public AGBProjectBuilderFrame(BFrame parent) {
    super(parent);
    this.setTitle("Create AGB Project");
    this.add(this.build(), BorderLayout.CENTER);
    this.add(this.buildButtons(), BorderLayout.SOUTH);
    this.pack();
    this.setLocationRelativeTo(null);
    this.projectField.requestFocus();
    this.fireValidator();
  }

  /**
   * Constroi o componente
   * 
   * @return componente
   */
  private Component build() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    {
      JLabel label = new JLabel("Name :");
      projectField = new JTextField();
      projectField.setPreferredSize(new Dimension(300, 20));
      projectField.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
          fireValidator();
        }
      });
      panel.add(label, new GBC(0, 0).right());
      panel.add(projectField, new GBC(1, 0).horizontal());
    }
    return panel;
  }

  /**
   * Constroi o componente
   * 
   * @return componente
   */
  private Component buildButtons() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    {
      buildButton = new JButton("Criar");
      buildButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          onBuildAction();
        }
      });
      this.getRootPane().setDefaultButton(buildButton);
      panel.add(buildButton);
    }
    return panel;
  }

  /**
   * Ação de Construir
   */
  protected void onBuildAction() {
    new AGBProjectBuilderAction(this, this.getProjectText()).start();
  }

  /**
   * Valida o componente
   */
  protected void fireValidator() {
    this.buildButton.setEnabled(getProjectText().length() > 0);
  }

  /**
   * Retorna o conteúdo da caixa de texto
   * 
   * @return conteúdo da caixa de texto
   */
  private String getProjectText() {
    return this.projectField.getText().trim();
  }

}
