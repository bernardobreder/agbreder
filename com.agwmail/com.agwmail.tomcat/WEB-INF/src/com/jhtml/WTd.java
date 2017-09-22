package com.jhtml;

import java.io.IOException;

import org.dom4j.Element;

/**
 * Tag html
 * 
 * 
 * @author Bernardo Breder
 */
public class WTd extends WTag {

  /**
   * Construtor
   */
  public WTd() {
    super("td");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WTag load(Element elem) throws IOException {
    super.load(elem);
    return this;
  }

}
