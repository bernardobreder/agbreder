package com.agbreder.compiler.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Scanner de uma linguagem simples
 * 
 * 
 * @author Bernardo Breder
 */
public class LNGScanner {

  /** Lista de tokens */
  private List<LNGToken> tokens = new ArrayList<LNGToken>();

  /**
   * Construtor
   * 
   * @param input
   * @throws IOException
   */
  public LNGScanner(InputStream input) throws IOException {
    StringBuilder sb = new StringBuilder();
    int offset = 0;
    int line = 1;
    int column = 1;
    for (;;) {
      int n = input.read();
      if (n == -1) {
        if (sb.length() > 0) {
          this.tokens.add(new LNGToken(sb.toString(), line, column - sb.length(),
            offset - sb.length()));
        }
        break;
      }
      char c = (char) n;
      if (isSpace(c)) {
        if (sb.length() > 0) {
          this.tokens.add(new LNGToken(sb.toString(), line, column - sb.length(),
            offset - sb.length()));
          sb.delete(0, sb.length());
        }
      }
      else if (isSymbol(c)) {
        if (sb.length() > 0) {
          this.tokens.add(new LNGToken(sb.toString(), line, column - sb.length(),
            offset - sb.length()));
          sb.delete(0, sb.length());
        }
        this.tokens.add(new LNGToken("" + c, line, column, offset));
      }
      else {
        sb.append(c);
      }
      if (c == '\n') {
        line++;
        column = 1;
      }
      else {
        column++;
      }
      offset++;
    }
  }

  /**
   * Retorna
   * 
   * @return tokens
   */
  public List<LNGToken> getTokens() {
    return tokens;
  }

  /**
   * Indica um identificador
   * 
   * @param text
   * @return resposta
   */
  public boolean isIdentify(String text) {
    if (!Character.isJavaIdentifierStart(text.charAt(0))) {
      return false;
    }
    for (int n = 1; n < text.length(); n++) {
      if (!Character.isJavaIdentifierPart(text.charAt(n))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Indica um simbolo
   * 
   * @param symbol
   * @return resposta
   */
  public boolean isSymbol(char symbol) {
    switch (symbol) {
      case '{':
      case '}':
        return true;
      default:
        return false;
    }
  }

  /**
   * Indica se é ignoravel
   * 
   * @param symbol
   * @return resposta
   */
  public boolean isSpace(char symbol) {
    return symbol <= ' ';
  }

}
