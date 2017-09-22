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
public class WScript extends WTag {

  /**
   * Construtor
   */
  public WScript() {
    super("script");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WTag load(Element elem) throws IOException {
    this.add("type", "text/javascript");
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
      output.write("<script type='text/javascript'>".getBytes());
      FileInputStream input = new FileInputStream(file);
      for (int n; (n = input.read()) != -1;) {
        output.write((char) n);
      }
      input.close();
      output.write("</script>".getBytes());
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
