package com.agwmail.servlet.preinbox;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import breder.util.util.StringUtil;

import com.agwmail.model.mail.PreMail;
import com.agwmail.model.user.User;
import com.agwmail.service.ServiceLocator;
import com.agwmail.servlet.core.ObjectOnlineServlet;

/**
 * Servlet inicial
 * 
 * 
 * @author Bernardo Breder
 */
public class PreInboxCloseServlet extends ObjectOnlineServlet {

  /**
   * {@inheritDoc}
   */
  @Override
  public Object build(HttpServletRequest req, HttpServletResponse resp,
    User user) throws Exception {
    int preMailId = Integer.parseInt(req.getParameter("id"));
    String text = req.getParameter("text");
    PreMail mail = ServiceLocator.mail.getPreInboxMail(user.getId(), preMailId);
    ServiceLocator.mail.removePreMail(user.getId(), preMailId);
    int fromUserId = mail.getFromUserId();
    User fromUser = ServiceLocator.user.get(fromUserId);
    int toUserId = mail.getToUserId();
    User toUser = ServiceLocator.user.get(toUserId);
    String toUserName = toUser.getUsername();
    String fromUserName = fromUser.getUsername();
    String subject = mail.getSubject();
    text = StringUtil.text2div(text);
    ServiceLocator.mail.sendMail(fromUserId, fromUserName, toUserId,
      toUserName, subject, text);
    ServiceLocator.mail.sendInbox(fromUserId, fromUserName, fromUserId,
      fromUserName, subject, text);
    return true;
  }

}
