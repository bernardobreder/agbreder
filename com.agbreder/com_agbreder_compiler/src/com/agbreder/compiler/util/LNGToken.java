package com.agbreder.compiler.util;

/**
 * Token de um codigo fonte
 * 
 * 
 * @author Bernardo Breder
 */
public class LNGToken {

  /** Imagem */
  private final String image;
  /** Imagem */
  private final int type;
  /** Linha */
  private final int line;
  /** Coluna */
  private final int column;
  /** Offset */
  private final int offset;

  /**
   * @param image
   * @param line
   * @param column
   * @param offset
   */
  public LNGToken(String image, int line, int column, int offset) {
    this(image, -1, line, column, offset);
  }

  /**
   * @param image
   * @param type
   * @param line
   * @param column
   * @param offset
   */
  public LNGToken(String image, int type, int line, int column, int offset) {
    super();
    this.image = image;
    this.type = type;
    this.line = line;
    this.column = column;
    this.offset = offset;
  }

  /**
   * Retorna
   * 
   * @return image
   */
  public String getImage() {
    return image;
  }

  /**
   * Retorna
   * 
   * @return line
   */
  public int getLine() {
    return line;
  }

  /**
   * Retorna
   * 
   * @return column
   */
  public int getColumn() {
    return column;
  }

  /**
   * Retorna
   * 
   * @return offset
   */
  public int getOffset() {
    return offset;
  }

  /**
   * Retorna
   * 
   * @return type
   */
  public int getType() {
    return type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + column;
    result = prime * result + ((image == null) ? 0 : image.hashCode());
    result = prime * result + line;
    result = prime * result + offset;
    result = prime * result + type;
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    LNGToken other = (LNGToken) obj;
    if (column != other.column) {
      return false;
    }
    if (image == null) {
      if (other.image != null) {
        return false;
      }
    }
    else if (!image.equals(other.image)) {
      return false;
    }
    if (line != other.line) {
      return false;
    }
    if (offset != other.offset) {
      return false;
    }
    if (type != other.type) {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "Token [image=" + image + ", type=" + type + ", line=" + line
      + ", column=" + column + ", offset=" + offset + "]";
  }

}
