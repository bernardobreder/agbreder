package com.agbreder.server.servlet.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agbreder.server.servlet.core.OfflineServlet;

/**
 * Servlet que recebe requisição de Browser
 * 
 * 
 * @author Bernardo Breder
 */
public class ServerPageServlet extends OfflineServlet {

  /**
   * {@inheritDoc}
   */
  @Override
  public void action(HttpServletRequest req, HttpServletResponse resp)
    throws Exception {
    req.getRequestDispatcher("/WEB-INF/pag/server.html").forward(req, resp);
  }

}
