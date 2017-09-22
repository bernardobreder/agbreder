package com.agbreder.server.servlet.mail;

import java.util.Set;

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
public class ListMessageServlet extends ObjectOnlineServlet {
	
	/** Dorme até achar algo */
	private static final int TIMEOUT = 5000;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Mail> build(HttpServletRequest req, HttpServletResponse resp,
		User user) throws Exception {
		int userId = user.getId();
		String session = req.getSession(true).getId();
		ServiceLocator.user.ping(userId, session);
		return null;
	}
	
}
