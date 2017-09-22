package com.agbreder.server.servlet.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agbreder.server.service.ServiceLocator;
import com.agbreder.server.servlet.core.ObjectOfflineServlet;

/**
 * Servlet inicial
 * 
 * 
 * @author Bernardo Breder
 */
public class CreateAccountServlet extends ObjectOfflineServlet {

  /**
   * {@inheritDoc}
   */
  @Override
  public Object build(HttpServletRequest req, HttpServletResponse resp)
    throws Exception {
    String username = req.getParameter("username");
    String email = req.getParameter("email");
    String password = req.getParameter("password");
    Integer userId = ServiceLocator.user.create(username, email, password);
    if (userId != null) {
      return userId;
    }
    else {
      return null;
    }
  }

}
