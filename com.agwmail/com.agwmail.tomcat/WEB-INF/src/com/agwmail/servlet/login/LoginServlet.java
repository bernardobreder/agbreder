package com.agwmail.servlet.login;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agwmail.servlet.core.PageOfflineServlet;
import com.jhtml.el.Grammer;

/**
 * Servlet inicial
 * 
 * 
 * @author Bernardo Breder
 */
public class LoginServlet extends PageOfflineServlet {

  /** Path */
  private File file;

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    this.file =
      new File(config.getServletContext().getRealPath(
        "/WEB-INF/page/login/login.xml"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void action(HttpServletRequest req, HttpServletResponse resp)
    throws Exception {
    InputStream input = new FileInputStream(file);
    Grammer.build(input).execute(req, resp);
  }

}
