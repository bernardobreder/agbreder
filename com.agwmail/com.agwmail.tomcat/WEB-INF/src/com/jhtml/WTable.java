package com.jhtml;

import java.io.IOException;

import org.dom4j.Element;

/**
 * Tag html
 * 
 * 
 * @author Bernardo Breder
 */
public class WTable extends WTag {

  /**
   * Construtor
   */
  public WTable() {
    super("table");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WTag load(Element elem) throws IOException {
    this.add("cellpadding", "0px");
    this.add("cellspacing", "0px");
    this.add("width", "100%");
    super.load(elem);
    return this;
  }

}
