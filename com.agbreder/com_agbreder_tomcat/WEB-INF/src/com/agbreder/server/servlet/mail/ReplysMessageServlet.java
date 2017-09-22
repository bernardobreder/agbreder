package com.agbreder.server.servlet.mail;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import breder.util.util.Json;

import com.agbreder.server.model.user.User;
import com.agbreder.server.service.ServiceLocator;
import com.agbreder.server.servlet.core.OnlineServlet;

/**
 * Enviar email
 * 
 * @author Bernardo Breder
 */
public class ReplysMessageServlet extends OnlineServlet {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void action(HttpServletRequest req, HttpServletResponse resp, User user)
		throws Exception {
		int userId = user.getId();
		List<?> list = List.class.cast(Json.decode(req.getInputStream()));
		for (Object item : list) {
			Map<?, ?> map = Map.class.cast(item);
			int mailId = Integer.parseInt(map.get("id").toString());
			String text = map.get("text").toString();
			ServiceLocator.mail.reply(userId, mailId, text);
		}
	}
	
}
