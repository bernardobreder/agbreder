package com.agbreder.servlet.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet de Web Service
 * 
 * @author bernardobreder
 */
public abstract class WebServiceServlet extends HttpServlet {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		this.action(req, resp);
	}
	
	/**
	 * Responde a requisição
	 * 
	 * @param req
	 * @param resp
	 */
	public abstract void action(HttpServletRequest req, HttpServletResponse resp);
	
}
