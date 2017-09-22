package com.agbreder.server.servlet.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import breder.util.util.Json;

import com.agbreder.server.model.user.User;

/**
 * Servlet Online que retorna objeto json
 * 
 * @author Bernardo Breder
 */
public abstract class Object64OnlineServlet extends OnlineServlet {
	
	/**
	 * Retorna o objeto json
	 * 
	 * @param req
	 * @param resp
	 * @param user
	 * @return json
	 * @throws Exception
	 */
	public abstract Object build(HttpServletRequest req,
		HttpServletResponse resp, User user) throws Exception;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void action(HttpServletRequest req, HttpServletResponse resp,
		User user) throws Exception {
		Object object = this.build(req, resp, user);
		resp.getOutputStream().write(Json.encode64(object).getBytes());
	}
	
}
