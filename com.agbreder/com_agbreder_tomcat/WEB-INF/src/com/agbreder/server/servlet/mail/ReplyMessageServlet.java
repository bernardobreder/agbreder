package com.agbreder.server.servlet.mail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agbreder.server.model.user.User;
import com.agbreder.server.service.ServiceLocator;
import com.agbreder.server.servlet.core.OnlineServlet;

/**
 * Enviar email
 * 
 * @author Bernardo Breder
 */
public class ReplyMessageServlet extends OnlineServlet {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void action(HttpServletRequest req, HttpServletResponse resp, User user)
		throws Exception {
		int userId = user.getId();
		int mailId = new Integer(req.getParameter("id"));
		String text = req.getParameter("text");
		ServiceLocator.mail.reply(userId, mailId, text);
	}
	
}
