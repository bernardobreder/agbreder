package com.agbreder.server.servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class P2PServlet extends HttpServlet {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		try {
			ObjectInputStream input = new ObjectInputStream(req.getInputStream());
			Address address = (Address) input.readObject();
			Socket socket = new Socket(address.getHost(), address.getPort());
			socket.getOutputStream().write("AE\n".getBytes());
			socket.close();
			resp.getOutputStream().write("Foi".getBytes());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
	}
	
}
