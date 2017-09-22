package com.agbreder.server.servlet.mail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import breder.util.util.Base64;
import breder.util.util.input.InputStreamUtil;

import com.agbreder.server.model.user.User;
import com.agbreder.server.service.ServiceLocator;
import com.agbreder.server.servlet.core.ObjectOnlineServlet;

/**
 * Enviar email
 * 
 * 
 * @author Bernardo Breder
 */
public class SendMessageServlet extends ObjectOnlineServlet {

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer build(HttpServletRequest req, HttpServletResponse resp,
    User user) throws Exception {
    int fromUserId = user.getId();
    String to = req.getParameter("to");
    User toUser = ServiceLocator.user.get(to);
    if (toUser == null) {
      throw new IllegalArgumentException("to user not found");
    }
    int toUserId = toUser.getId();
    String subject = req.getParameter("subject");
    String text =
      Base64.decodeString(new String(InputStreamUtil.getBytes(req
        .getInputStream())));
    return ServiceLocator.mail.send(fromUserId, toUserId, subject, text)
      .getId();
  }

}
