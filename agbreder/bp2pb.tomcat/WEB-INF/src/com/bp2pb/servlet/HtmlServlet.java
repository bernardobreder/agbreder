package com.bp2pb.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Página pagina de html
 * 
 * @author bernardobreder
 */
public abstract class HtmlServlet extends HttpServlet {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=UTF-8");
		try {
			this.action(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.getOutputStream().close();
	}

	/**
	 * Responde a requisição
	 * 
	 * @param req
	 * @param resp
	 */
	protected abstract void action(HttpServletRequest req, HttpServletResponse resp) throws Exception;

}
