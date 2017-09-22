package com.jhtml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jhtml.el.Grammer;
import com.jhtml.el.node.ELNode;

/**
 * Tag html
 * 
 * 
 * @author Bernardo Breder
 */
public class WImport extends WTag {

  /**
   * Construtor
   */
  public WImport() {
    super("import");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(HttpServletRequest req, HttpServletResponse resp)
    throws Exception {
    String src = this.getAttributes().get("src").execute(req, resp).toString();
    @SuppressWarnings("deprecation")
    File file = new File(req.getRealPath(src));
    InputStream input = new FileInputStream(file);
    WTag page = Grammer.build(input);
    input.close();
    Map<String, Object> oldValues = new HashMap<String, Object>();
    for (String att : this.getAttributes().keySet()) {
      if (!att.equals("src")) {
        Object attribute = req.getAttribute(att);
        if (attribute != null) {
          oldValues.put(att, attribute);
        }
        String value =
          this.getAttributes().get(att).execute(req, resp).toString();
        ELNode read = Grammer.read(value);
        req.setAttribute(att, read.execute(req, resp));
      }
    }
    page.execute(req, resp);
    for (String att : oldValues.keySet()) {
      if (!att.equals("src")) {
        req.setAttribute(att, oldValues.get(att));
      }
    }
  }

}
