package com.agbreder.servlet.service;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agbreder.peer.PeerManager;

/**
 * Serviço de conexão
 * 
 * @author bernardobreder
 */
public class ConnectServlet extends WebServiceServlet {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void action(HttpServletRequest req, HttpServletResponse resp) {
		String host = req.getRemoteHost();
		int port = req.getRemotePort();
		String address = host + ":" + port;
		PeerManager.getInstance().connect(address);
	}
	
}
