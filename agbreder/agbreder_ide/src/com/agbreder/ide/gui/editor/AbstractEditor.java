package com.agbreder.ide.gui.editor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;

import com.agbreder.ide.gui.editor.agb.AGBEditor;

/**
 * Classe que armazena as estruturas de dados para carregar um componente de
 * editor
 * 
 * @author Bernardo Breder
 */
public class AbstractEditor extends JComponent {

  /** Margem do Cabeçalho com o Editor */
  private static final int MARGIN_HEADER_EDITOR = 5;
  /** Margem do Cabeçalho */
  private static final int MARGIN_HEADER = 5;
  /** Espaço que o Tab ocupa */
  private static final int TAB_SPACE = 4;
  /** Altura de um caracter */
  private int charHeight;
  /** Largura de um caracter */
  private int charWidth;
  /** Linha do cursor */
  private int cursorLine;
  /** Coluna do cursor */
  private int cursorColumn;
  /** Largura que o cabeçalho ocupa */
  private int headerWidth;
  /** Strings das linhas */
  private final List<StringBuilder> lines = new ArrayList<StringBuilder>();
  /** Texto */
  private String text;
  /** Texto */
  private boolean textUpdated;
  /** Palavras Coloridas */
  private final Map<String, Color> words = new HashMap<String, Color>();

  /**
   * Construtor
   */
  public AbstractEditor() {
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    this.setFont(font);
    this.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    this.setFocusable(true);
    this.setFocusTraversalKeysEnabled(false);
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        onMousePressed(e);
      }
    });
    this.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        onKeyPressed(e);
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void paint(Graphics g) {
    Font lastFont = g.getFont();
    try {
      this.charHeight = g.getFontMetrics().getHeight();
      this.charWidth = g.getFontMetrics().getWidths()[32];
      g.setFont(this.getFont());
      this.headerWidth =
        Integer.valueOf(this.lines.size()).toString().length() * this.charWidth
          + 2 * MARGIN_HEADER;
      // Pintando o fundo
      {
        {
          g.setColor(Color.LIGHT_GRAY);
          g.fillRect(0, 0, this.headerWidth, this.getHeight());
        }
        {
          g.setColor(Color.WHITE);
          g.fillRect(this.headerWidth, 0, this.getWidth() - this.headerWidth,
            this.getHeight());
        }
      }
      {
        g.translate(MARGIN_HEADER, 0);
        g.setColor(Color.BLACK);
        for (int n = 0; n < this.lines.size(); n++) {
          Integer line = Integer.valueOf(n + 1);
          g.drawString(line.toString(), 0, (n + 1) * this.charHeight);
        }
        g.translate(-MARGIN_HEADER, 0);
      }
      {
        g.translate(this.headerWidth + MARGIN_HEADER_EDITOR, 0);
        // Pintando as palavras
        {
          g.setColor(Color.BLACK);
          int y = 0;
          for (StringBuilder line : this.lines) {
            y += g.getFontMetrics().getHeight();
            String chars = line.toString();
            if (chars.length() > 0) {
              int x = 0;
              List<String> splits = split(chars, '\t');
              for (String split : splits) {
                List<String> words = buildTokens(split);
                for (int n = 0; n < words.size(); n++) {
                  String word = words.get(n);
                  Color color = this.words.get(word);
                  if (color != null) {
                    Color old = g.getColor();
                    g.setColor(color);
                    g.drawString(word, x * this.charWidth, y);
                    g.setColor(old);
                  }
                  else {
                    g.drawString(word, x * this.charWidth, y);
                  }
                  x += word.length();
                }
                x += TAB_SPACE;
              }
            }
          }
        }
        // Pintando o Cursor
        {
          int y1 =
            this.cursorLine * this.charHeight + g.getFontMetrics().getDescent();
          int x1 = 0, offset = this.cursorColumn;
          {
            String chars = this.lines.get(this.cursorLine).toString();
            if (chars.length() > 0) {
              List<String> splits = split(chars, '\t');
              for (int n = 0; n < splits.size(); n++) {
                String split = splits.get(n);
                int len = split.length();
                if (len >= offset) {
                  x1 += offset;
                  break;
                }
                else {
                  x1 += len;
                  offset -= len;
                  if (n != splits.size() - 1) {
                    x1 += TAB_SPACE;
                    offset--;
                  }
                }
              }
              x1 *= this.charWidth;
            }
          }
          g.drawLine(x1, y1, x1, y1 + this.charHeight);
        }
        g.translate(-this.headerWidth - MARGIN_HEADER_EDITOR, 0);
      }
    }
    finally {
      g.setFont(lastFont);
    }
  }

  /**
   * Adiciona uma palavra
   * 
   * @param word
   * @param color
   */
  public void addWord(String word, Color color) {
    this.words.put(word, color);
  }

  /**
   * @return the text
   */
  public String getText() {
    if (!this.textUpdated) {
      StringBuilder sb = new StringBuilder();
      int size = this.lines.size();
      for (int n = 0; n < size; n++) {
        sb.append(this.lines.get(n));
        if (n != size - 1) {
          sb.append("\n");
        }
      }
      this.text = sb.toString();
      this.textUpdated = true;
    }
    return text;
  }

  /**
   * @param text the text to set
   */
  public void setText(String text) {
    this.text = text;
    this.refreshStruct();
  }

  /**
   * Evento de teclado
   * 
   * @param e
   */
  protected void onKeyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();
    char keyChar = e.getKeyChar();
    boolean isControl = e.isMetaDown() || e.isControlDown();
    boolean isAlt = e.isAltDown();
    boolean isShift = e.isShiftDown();
    if (isControl && keyCode == KeyEvent.VK_LEFT) {
      onCtrlLeftKeyPressed(isShift);
    }
    else if (isAlt && keyCode == KeyEvent.VK_LEFT) {
      onAltLeftKeyPressed();
    }
    else if (keyCode == KeyEvent.VK_LEFT) {
      onLeftKeyPressed(isShift);
    }
    else if (isControl && keyCode == KeyEvent.VK_RIGHT) {
      onCtrlRightKeyPressed(isShift);
    }
    else if (isAlt && keyCode == KeyEvent.VK_RIGHT) {
      onAltRightKeyPressed();
    }
    else if (keyCode == KeyEvent.VK_RIGHT) {
      onRightKeyPressed(isShift);
    }
    else if (isControl && keyCode == KeyEvent.VK_UP) {
      onCtrlUpKeyPressed(isShift);
    }
    else if (isAlt && keyCode == KeyEvent.VK_UP) {
      onAltUpKeyPressed();
    }
    else if (keyCode == KeyEvent.VK_UP) {
      onUpKeyPressed(isShift);
    }
    else if (isControl && keyCode == KeyEvent.VK_DOWN) {
      onCtrlDownKeyPressed(isShift);
    }
    else if (isAlt && keyCode == KeyEvent.VK_DOWN) {
      onAltDownKeyPressed();
    }
    else if (keyCode == KeyEvent.VK_DOWN) {
      onDownKeyPressed(isShift);
    }
    else if (keyCode == KeyEvent.VK_ENTER) {
      onEnterKeyPressed();
    }
    else if (keyCode == KeyEvent.VK_DELETE) {
      onDeleteKeyPressed();
    }
    else if (keyCode == KeyEvent.VK_BACK_SPACE) {
      onBackspaceKeyPressed();
    }
    else if (isControl && keyCode == KeyEvent.VK_C) {
      onCopyKeyPressed();
    }
    else if (isControl && keyCode == KeyEvent.VK_V) {
      onPasteKeyPressed();
    }
    else if (isControl && keyCode == KeyEvent.VK_A) {
      onSelectAllKeyPressed();
    }
    else if ((keyChar >= 32 && keyChar < 127) || keyCode == KeyEvent.VK_TAB) {
      onGenericKeyPressed(keyChar);
    }
    this.doScroll();
    this.repaint();
    this.textUpdated = false;
  }

  /**
   * Alinha o scroll da caixa de texto
   */
  private void doScroll() {
    if (this.getParent() != null && this.getParent() instanceof JViewport) {
      JViewport port = (JViewport) this.getParent();
      port.setViewSize(this.getSize());
      Rectangle rect = port.getViewRect();
      int x =
        Math.max(0, (this.cursorColumn + 2) * this.charWidth - rect.width + 2
          * this.headerWidth);
      int y =
        Math.max(0, (this.cursorLine + 2) * this.charHeight - rect.height);
      Point point = new Point(x, y);
      port.setViewPosition(point);
      if (port.getParent() != null) {
        port.getParent().validate();
      }
    }
  }

  /**
   * Evento de click no texto
   * 
   * @param e
   */
  protected void onMousePressed(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1) {
      int y = e.getY();
      int x = e.getX();
      this.cursorLine = Math.min(this.lines.size() - 1, y / this.charHeight);
      this.cursorColumn =
        this.convertViewToModelColumnIndex(x - this.headerWidth
          - MARGIN_HEADER_EDITOR);
      this.repaint();
    }
    this.requestFocus();
  }

  /**
   * Retorna o indice da coluna no modelo a partir da visão
   * 
   * @param x
   * @return indice da coluna no modelo a partir da visão
   */
  private int convertViewToModelColumnIndex(int x) {
    StringBuilder line = this.lines.get(this.cursorLine);
    int maxColumn = line.length();
    int charX = maxColumn;
    for (int n = 0; n < line.length(); n++) {
      char charAt = line.charAt(n);
      if (charAt == '\t') {
        x -= (TAB_SPACE - 1) * this.charWidth;
      }
      if (x < (n + 0.5f) * this.charWidth) {
        charX = n;
        break;
      }
    }
    return charX;
  }

  /**
   * Retorna o indice da coluna considerando os espaços do caracter tab
   * 
   * @param line
   * @param columnIndex
   * @return indice da coluna considerando os espaços do caracter tab
   */
  private int convertModelToViewColumnIndex(StringBuilder line, int columnIndex) {
    int x = columnIndex * this.charWidth;
    for (int n = 0; n < columnIndex; n++) {
      if (line.charAt(n) == '\t') {
        x += (TAB_SPACE - 1) * this.charWidth;
      }
    }
    return x;
  }

  /**
   * @param isShift
   */
  private void onLeftKeyPressed(boolean isShift) {
    if (this.cursorColumn == 0 && this.cursorLine != 0) {
      this.cursorLine--;
      this.cursorColumn = this.lines.get(this.cursorLine).length();
    }
    else {
      this.cursorColumn = Math.max(0, this.cursorColumn - 1);
    }
  }

  /**
   * @param isShift
   */
  private void onRightKeyPressed(boolean isShift) {
    if (this.cursorColumn == this.lines.get(this.cursorLine).length()
      && this.cursorLine != this.lines.size() - 1) {
      this.cursorLine++;
      this.cursorColumn = 0;
    }
    else {
      this.cursorColumn =
        Math.min(this.lines.get(this.cursorLine).length(),
          this.cursorColumn + 1);
    }
  }

  /**
   * @param isShift
   */
  private void onUpKeyPressed(boolean isShift) {
    if (this.cursorLine != 0) {
      this.cursorLine = Math.max(0, this.cursorLine - 1);
      this.cursorColumn =
        this.convertViewToModelColumnIndex(this.convertModelToViewColumnIndex(
          this.lines.get(this.cursorLine + 1), this.cursorColumn));
    }
  }

  /**
   * @param isShift
   */
  private void onDownKeyPressed(boolean isShift) {
    if (this.cursorLine != this.lines.size() - 1) {
      this.cursorLine = Math.max(0, this.cursorLine + 1);
      this.cursorColumn =
        this.convertViewToModelColumnIndex(this.convertModelToViewColumnIndex(
          this.lines.get(this.cursorLine - 1), this.cursorColumn));
    }
  }

  /**
   * @param isShift
   */
  private void onCtrlLeftKeyPressed(boolean isShift) {
    this.cursorColumn = 0;
  }

  /**
   * @param isShift
   */
  private void onCtrlRightKeyPressed(boolean isShift) {
    this.cursorColumn = this.lines.get(this.cursorLine).length();
  }

  /**
   * @param isShift
   */
  private void onCtrlUpKeyPressed(boolean isShift) {
    this.cursorLine = 0;
    this.cursorColumn = 0;
  }

  /**
   * @param isShift
   */
  private void onCtrlDownKeyPressed(boolean isShift) {
    this.cursorLine = this.lines.size() - 1;
    this.cursorColumn = this.lines.get(this.cursorLine).length();
  }

  /**
	 * 
	 */
  private void onAltLeftKeyPressed() {
    if (this.cursorColumn != 0) {
      String line =
        this.lines.get(this.cursorLine).substring(0, this.cursorColumn - 1);
      for (int n = line.length() - 1; n >= 0; n--) {
        if (this.isTokenSeparator(line.charAt(n))) {
          this.cursorColumn = n + 1;
          return;
        }
      }
      this.cursorColumn = 0;
    }
  }

  /**
	 * 
	 */
  private void onAltRightKeyPressed() {
    StringBuilder line = this.lines.get(this.cursorLine);
    for (int n = this.cursorColumn; n < line.length(); n++) {
      if (this.isTokenSeparator(line.charAt(n))) {
        this.cursorColumn = n + 1;
        return;
      }
    }
    this.cursorColumn = line.length();
  }

  /**
	 * 
	 */
  private void onAltUpKeyPressed() {
    if (this.cursorLine > 0) {
      StringBuilder backLine = this.lines.get(this.cursorLine - 1);
      this.lines.set(this.cursorLine - 1, this.lines.get(this.cursorLine));
      this.lines.set(this.cursorLine, backLine);
      this.cursorLine--;
      this.cursorColumn = 0;
    }
  }

  /**
	 * 
	 */
  private void onAltDownKeyPressed() {
    if (this.cursorLine < this.lines.size() - 1) {
      StringBuilder next = this.lines.get(this.cursorLine + 1);
      this.lines.set(this.cursorLine + 1, this.lines.get(this.cursorLine));
      this.lines.set(this.cursorLine, next);
      this.cursorLine++;
      this.cursorColumn = 0;
    }
  }

  /**
	 * 
	 */
  private void onBackspaceKeyPressed() {
    if (this.cursorLine != 0 || this.cursorColumn != 0) {
      StringBuilder currentLine = this.lines.get(this.cursorLine);
      if (this.cursorColumn == 0) {
        StringBuilder backLine = this.lines.get(this.cursorLine - 1);
        this.lines.remove(this.cursorLine--);
        this.cursorColumn = backLine.length();
        backLine.append(currentLine);
      }
      else {
        currentLine.deleteCharAt(--this.cursorColumn);
      }
    }
  }

  /**
	 * 
	 */
  private void onEnterKeyPressed() {
    StringBuilder currentLine = this.lines.get(this.cursorLine);
    String line = currentLine.toString();
    currentLine.delete(this.cursorColumn, currentLine.length());
    StringBuilder nextLine = new StringBuilder();
    int columnIndex = this.cursorColumn;
    this.cursorColumn = 0;
    {
      nextLine.append(line.substring(columnIndex, line.length()).trim());
      for (int n = 0; n < columnIndex; n++) {
        if (line.charAt(n) == '\t') {
          nextLine.insert(0, '\t');
          this.cursorColumn++;
        }
        else {
          break;
        }
      }
    }
    this.lines.add(++this.cursorLine, nextLine);
  }

  /**
	 * 
	 */
  private void onDeleteKeyPressed() {
    StringBuilder currentLine = this.lines.get(this.cursorLine);
    if (this.cursorColumn == currentLine.length()) {
      if (this.cursorLine != this.lines.size() - 1) {
        StringBuilder nextLine = this.lines.remove(this.cursorLine + 1);
        currentLine.append(nextLine);
      }
    }
    else {
      currentLine.deleteCharAt(this.cursorColumn);
    }
  }

  /**
	 * 
	 */
  private void onCopyKeyPressed() {
  }

  /**
	 * 
	 */
  private void onPasteKeyPressed() {
  }

  /**
	 * 
	 */
  private void onSelectAllKeyPressed() {
  }

  /**
   * @param keyCode
   */
  private void onGenericKeyPressed(char keyCode) {
    StringBuilder line = this.lines.get(this.cursorLine);
    line.insert(this.cursorColumn, keyCode);
    this.cursorColumn++;
  }

  /**
   * Atualiza a estrutura
   */
  private void refreshStruct() {
    List<String> lines = split(text, '\n');
    for (String line : lines) {
      this.lines.add(new StringBuilder(line));
    }
  }

  /**
   * Divide a string pelo separador.
   * 
   * @param text
   * @param separator
   * @return divide a string
   */
  private static List<String> split(String text, char separator) {
    List<String> list = new ArrayList<String>();
    int offset = 0;
    int index = text.indexOf(separator);
    while (index >= 0) {
      list.add(text.substring(offset, index));
      offset = index + 1;
      index = text.indexOf(separator, index + 1);
    }
    if (offset <= text.length()) {
      list.add(text.substring(offset, text.length()));
    }
    return list;
  }

  /**
   * Divide a string pelo separador.
   * 
   * @param text
   * @return divide a string
   */
  protected List<String> buildTokens(String text) {
    List<String> list = new ArrayList<String>();
    int begin = 0;
    for (int n = 0; n < text.length(); n++) {
      char c = text.charAt(n);
      if (isTokenSeparator(c)) {
        if (n > 0 && begin != n) {
          list.add(text.substring(begin, n));
        }
        list.add(text.substring(n, n + 1));
        begin = n + 1;
      }
    }
    if (begin != text.length()) {
      list.add(text.substring(begin, text.length()));
    }
    return list;
  }

  /**
   * Indica se um caracter é do tipo separador
   * 
   * @param c
   * @return se um caracter é do tipo separador
   */
  protected boolean isTokenSeparator(char c) {
    return (c >= 32 && c <= 47) || (c >= 58 && c <= 64) || (c >= 91 && c <= 96)
      || (c >= 123) || (c == '\t') || (c == '\n');
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Dimension getPreferredSize() {
    return this.getSize();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Dimension getSize() {
    int x = 0;
    for (StringBuilder line : this.lines) {
      if (line.length() > x) {
        x = line.length();
      }
    }
    return new Dimension((x + 1) * this.charWidth + this.headerWidth,
      (this.lines.size() + 1) * this.charHeight);
  }

  /**
   * Testador
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JTabbedPane pane = new JTabbedPane();
    AbstractEditor editor = new AGBEditor();
    editor.setText("\tBern\tardo\nBreder (class");
    JScrollPane scroll = new JScrollPane(editor);
    scroll.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    pane.addTab("Test", scroll);
    frame.add(scroll);
    frame.setSize(400, 300);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

}
