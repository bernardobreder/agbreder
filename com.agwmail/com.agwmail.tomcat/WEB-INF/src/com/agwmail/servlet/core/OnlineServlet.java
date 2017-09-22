package com.agwmail.servlet.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agwmail.model.user.User;
import com.agwmail.model.user.UserContext;
import com.agwmail.model.user.UserManager;

/**
 * Servlet inicial
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class OnlineServlet extends HtmlServlet {

  /**
   * Ação online
   * 
   * @param req
   * @param resp
   * @param user
   * @throws Exception
   */
  public abstract void action(HttpServletRequest req, HttpServletResponse resp,
    User user) throws Exception;

  /**
   * {@inheritDoc}
   */
  @Override
  public final void action(HttpServletRequest req, HttpServletResponse resp)
    throws Exception {
    UserContext userContext = UserManager.getInstance().getUserContext();
    if (userContext == null) {
      resp.sendRedirect("login.html");
    }
    else {
      this.action(req, resp, userContext.getUser());
    }
  }

}