package com.agbreder.server.servlet.login.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.breder.jhtml.el.Grammer;

import breder.util.util.input.InputStreamUtil;

import com.agbreder.server.servlet.core.PageOfflineServlet;

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
    InputStreamUtil.getBytes(new ByteArrayInputStream(new byte[0]));
    Grammer.build(input).execute(req, resp);
  }

}
