package com.jhtml;

/**
 * Tag html
 * 
 * 
 * @author Bernardo Breder
 */
public class WTextArea extends WTag {

  /**
   * Construtor
   */
  public WTextArea() {
    super("textarea");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasBody() {
    return true;
  }

}
