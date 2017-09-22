package com.agbreder.ide.gui.builder.agb.folder;

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

import com.agbreder.ide.gui.destkop.IDesktop;
import com.agbreder.ide.gui.util.UI;
import com.agbreder.ide.model.io.IFolder;

/**
 * Janela de criação de uma Classe Agent Breder
 * 
 * @author bernardobreder
 */
public class AGBFolderBuilderFrame extends BFrame {

  /** Field */
  private JTextField packageField;
  /** Field */
  private JTextField sourceField;
  /** Field */
  private JTextField projectField;
  /** Field */
  private JButton buildButton;
  /** Field */
  private IFolder projectSelected;
  /** Field */
  private IFolder sourceSelected;

  /**
   * Construtor
   * 
   * @param parent
   */
  public AGBFolderBuilderFrame(BFrame parent) {
    super(parent);
    this.setTitle("Create AGB Package");
    this.add(this.build(), BorderLayout.CENTER);
    this.add(this.buildButtons(), BorderLayout.SOUTH);
    this.pack();
    this.setLocationRelativeTo(null);
    this.packageField.requestFocus();
    this.fireValidator();
  }

  /**
   * Indica se a janela pode ser carregada
   * 
   * @return se a janela pode ser carregada
   */
  public static boolean accept() {
    return IDesktop.DEFAULT.getSelectedProject() != null
      && IDesktop.DEFAULT.getSelectedSource() != null;
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
      JLabel label = new JLabel("Project :");
      JButton button = new JButton("Browse...");
      projectSelected = IDesktop.DEFAULT.getSelectedProject();
      projectField = new JTextField();
      projectField.setText(projectSelected.getPath());
      projectField.setEditable(false);
      projectField.setFocusable(false);
      projectField.setPreferredSize(new Dimension(300, 20));
      panel.add(label, new GBC(0, 0).right());
      panel.add(projectField, new GBC(1, 0).horizontal());
      UI.equalizeHeight(projectField, button);
    }
    {
      JLabel label = new JLabel("Source :");
      JButton button = new JButton("Browse...");
      sourceSelected = IDesktop.DEFAULT.getSelectedSource();
      sourceField = new JTextField();
      sourceField.setText(sourceSelected.getPath());
      sourceField.setEditable(false);
      sourceField.setFocusable(false);
      sourceField.setPreferredSize(new Dimension(300, 20));
      panel.add(label, new GBC(0, 1).right());
      panel.add(sourceField, new GBC(1, 1).horizontal());
      UI.equalizeHeight(sourceField, button);
    }
    {
      JLabel label = new JLabel("Name :");
      packageField = new JTextField();
      packageField.addKeyListener(new KeyAdapter() {
        /**
         * {@inheritDoc}
         */
        @Override
        public void keyReleased(KeyEvent e) {
          fireValidator();
        }
      });
      panel.add(label, new GBC(0, 2).right());
      panel.add(packageField, new GBC(1, 2).gridwh(2, 1).horizontal());
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
    new AGBFolderBuilderAction(this, this.sourceSelected, this.getPackageText())
      .start();
  }

  /**
   * Valida o componente
   */
  protected void fireValidator() {
    this.buildButton.setEnabled(getProjectText().length() > 0
      && getSourceText().length() > 0 && getPackageText().length() > 0);
  }

  /**
   * Conteúdo da caixa de texto
   * 
   * @return Conteúdo da caixa de texto
   */
  private String getPackageText() {
    return this.packageField.getText().trim();
  }

  /**
   * Conteúdo da caixa de texto
   * 
   * @return Conteúdo da caixa de texto
   */
  private String getSourceText() {
    return this.sourceField.getText().trim();
  }

  /**
   * Conteúdo da caixa de texto
   * 
   * @return Conteúdo da caixa de texto
   */
  private String getProjectText() {
    return this.projectField.getText().trim();
  }

}
