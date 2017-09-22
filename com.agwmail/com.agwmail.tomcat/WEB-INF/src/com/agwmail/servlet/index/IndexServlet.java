package com.agwmail.servlet.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agwmail.model.user.User;
import com.agwmail.servlet.core.PageOnlineServlet;
import com.jhtml.el.Grammer;

/**
 * Servlet inicial
 * 
 * 
 * @author Bernardo Breder
 */
public class IndexServlet extends PageOnlineServlet {

  /** Path */
  private File file;

  /**
   * {@inheritDoc}
   */
  @Override
  public void init() throws ServletException {
    super.init();
    this.file =
      new File(this.getServletContext().getRealPath("/WEB-INF/page/index.xml"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void action(HttpServletRequest req, HttpServletResponse resp, User user)
    throws Exception {
    req.setAttribute("names", Arrays.asList("Bernardo Breder", "Renata Guedes",
      "Giovanni Gargano"));
    InputStream input = new FileInputStream(file);
    Grammer.build(input).execute(req, resp);
  }

}
