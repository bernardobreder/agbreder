package com.agbreder.server.servlet.mail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agbreder.server.model.mail.Mail;
import com.agbreder.server.model.user.User;
import com.agbreder.server.service.ServiceLocator;
import com.agbreder.server.servlet.core.ObjectOnlineServlet;

/**
 * Enviar email
 * 
 * @author Bernardo Breder
 */
public class PopMessageServlet extends ObjectOnlineServlet {
	
	/** Dorme até achar algo */
	private static final int TIMEOUT = 5000;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String build(HttpServletRequest req, HttpServletResponse resp,
		User user) throws Exception {
		int userId = user.getId();
		Mail mail = ServiceLocator.mail.popAndWait(userId, TIMEOUT);
		if (mail != null) {
			return mail.getId() + ";" + mail.getInput();
		} else {
			return "";
		}
	}
	
}
