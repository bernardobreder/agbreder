package com.jhtml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Element;

import com.jhtml.el.node.ELNode;

/**
 * Tag html
 * 
 * 
 * @author Bernardo Breder
 */
public class WStyle extends WTag {

  /**
   * Construtor
   */
  public WStyle() {
    super("link");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WTag load(Element elem) throws IOException {
    this.add("type", "text/css");
    this.add("rel", "stylesheet");
    return super.load(elem);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(HttpServletRequest req, HttpServletResponse resp)
    throws Exception {
    ELNode srcAtt = this.getAttributes().get("inline");
    if (srcAtt != null) {
      @SuppressWarnings("deprecation")
      File file =
        new File(req.getRealPath(srcAtt.execute(req, resp).toString()));
      ServletOutputStream output = resp.getOutputStream();
      output.write("<style>".getBytes());
      FileInputStream input = new FileInputStream(file);
      for (int n; (n = input.read()) != -1;) {
        if (n != '\t' && n != '\r' && n != '\n') {
          output.write((char) n);
        }
      }
      input.close();
      output.write("</style>".getBytes());
    }
    else {
      super.execute(req, resp);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasBody() {
    return true;
  }

}
