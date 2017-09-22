package com.agwmail.servlet.compose;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import breder.util.util.Base64;

import com.agwmail.model.user.User;
import com.agwmail.service.ServiceLocator;
import com.agwmail.servlet.core.OnlineServlet;

/**
 * Enviar email
 * 
 * 
 * @author Bernardo Breder
 */
public class ComposeSendServlet extends OnlineServlet {

  /**
   * {@inheritDoc}
   */
  @Override
  public void action(HttpServletRequest req, HttpServletResponse resp, User user)
    throws Exception {
    String to = req.getParameter("to");
    String subject = req.getParameter("subject");
    String text = new String(Base64.decode(req.getParameter("text")), "utf-8");
    String[] tos = to.split(",");
    int fromUserId = user.getId();
    for (int n = 0; n < tos.length; n++) {
      int userId = ServiceLocator.user.get(tos[n]).getId();
      ServiceLocator.mail.sendPreInbox(fromUserId, userId, subject, text);
    }
  }

}
