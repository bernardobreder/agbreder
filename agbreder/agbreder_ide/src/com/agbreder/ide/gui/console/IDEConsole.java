package com.agbreder.ide.gui.console;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import com.agbreder.ide.resource.Resource;

/**
 * Console da IDE
 * 
 * @author bernardobreder
 */
public class IDEConsole extends JPanel {

  /** Caixa de texto */
  private JTextArea area;

  /**
   * Construtor
   */
  public IDEConsole() {
    this.setLayout(new BorderLayout());
    this.add(this.buildNorth(), BorderLayout.NORTH);
    this.add(this.buildCenter(), BorderLayout.CENTER);
  }

  /**
   * Constroi o componente Norte
   * 
   * @return o componente Norte
   */
  private Component buildNorth() {
    JToolBar bar = new JToolBar();
    {
      JButton c = new JButton(new ImageIcon(Resource.getInstance().getClear()));
      c.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          clean();
        }
      });
      c.setFocusable(false);
      bar.add(c);
    }
    bar.setFloatable(false);
    return bar;
  }

  /**
   * Constroi o componente Center
   * 
   * @return o componente Center
   */
  private Component buildCenter() {
    area = new JTextArea();
    area.setEditable(false);
    JScrollPane scroll = new JScrollPane(area);
    scroll.setBorder(BorderFactory.createEmptyBorder());
    return scroll;
  }

  /**
   * Limpa o conteudo e altera o valor
   * 
   * @param text
   */
  public void setText(String text) {
    area.setText(text);
  }

  /**
   * Limpa o conteudo
   */
  public void clean() {
    area.setText("");
  }

  /**
   * Acrescenta conteudo
   * 
   * @param text
   */
  public void append(String text) {
    String string = area.getText() + text;
    if (string.length() > 1024) {
      string = string.substring(string.length() - 1024, string.length());
    }
    area.setText(string);
  }

  /**
   * Acrescenta conteudo
   * 
   * @param text
   */
  public void append(char text) {
    area.setText(area.getText() + text);
  }

}
