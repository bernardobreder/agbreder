package com.agbreder.server.servlet.page;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agbreder.server.model.user.User;
import com.agbreder.server.service.ServiceLocator;
import com.agbreder.server.servlet.core.OfflineServlet;

/**
 * Servlet que recebe requisição de Browser
 * 
 * @author Bernardo Breder
 */
public class PageServlet extends OfflineServlet {
	
	/** Timeout do Send And Wait */
	private static final int TIMEOUT_SEND_AND_WAIT = 60 * 1000;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void action(HttpServletRequest req, HttpServletResponse resp)
		throws Exception {
		Thread.currentThread().setName("Init Request");
		// long timerReq = System.currentTimeMillis();
		String uri = req.getRequestURI();
		uri = uri.substring("/agbreder/".length());
		uri = uri.substring(0, uri.length() - ".web".length());
		User user = ServiceLocator.user.get(uri);
		resp.getOutputStream().write(this.action(req, resp, user).getBytes());
		// TomcatLog.info("Time to Responde : %d milisegs",
		// System.currentTimeMillis()
		// - timerReq);
		Thread.currentThread().setName("Finish Request");
	}
	
	/**
	 * Responde a uma requisição de um usuário
	 * 
	 * @param req
	 * @param resp
	 * @param user
	 * @return
	 */
	private String action(HttpServletRequest req, HttpServletResponse resp,
		User user) {
		if (user == null) {
			return "User not Found";
		}
		else {
			try {
				// List<String> sources =
				// ServiceLocator.source.getSources(user.getId());
				List<String> sources =
					Arrays
						.asList("str IndexServlet_service() {return 'Minha Pagina no Ar'}");
				StringBuilder sb = new StringBuilder();
				for (String source : sources) {
					sb.append(source);
				}
				String reply =
					ServiceLocator.mail.sendAndWait(0, user.getId(), "", sb.toString(),
						TIMEOUT_SEND_AND_WAIT);
				if (reply == null) {
					return "Timeout for User to Responde your Request";
				}
				return reply;
			}
			catch (Throwable e) {
				e.printStackTrace();
				return "Internal Exception in the Server";
			}
		}
	}
	
}
