package com.agbreder.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.net.NetworkClient;
import sun.net.www.http.HttpClient;
import breder.util.util.input.InputStreamUtil;

public class P2PServlet extends HttpServlet {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		String host = req.getRemoteHost();
		int port = req.getRemotePort();
		System.out.println(String.format("%s:%d", host, port));
		Socket socket = new Socket(host, port);
		socket.getOutputStream().write("AE\n".getBytes());
		socket.close();
		resp.getOutputStream().write("Foi".getBytes());
		resp.getOutputStream().close();
	}
	
	public static class Address implements Serializable {
		
		private static final long serialVersionUID = -6849794470754667710L;
		
		public String id;
		
		public String host;
		
		public int port;
		
	}
	
	public static void main(String[] args) throws Exception {
		{
			URL url = new URL("http://www.breder.org/agb/p2p");
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			Field field = connection.getClass().getDeclaredField("http");
			field.setAccessible(true);
			HttpClient client = (HttpClient) field.get(connection);
			field = NetworkClient.class.getDeclaredField("serverSocket");
			field.setAccessible(true);
			Socket socket = (Socket) field.get(client);
			final ServerSocket server = new ServerSocket(socket.getLocalPort());
			new Thread() {
				public void run() {
					try {
						Socket accept = server.accept();
						System.out.println(accept);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}.start();
			connection.getInputStream();
		}
		{
			
			Socket socket = new Socket("www.breder.org", 80);
			String host = socket.getLocalAddress().getHostAddress();
			int port = socket.getLocalPort();
			ServerSocket server = new ServerSocket(port);
			OutputStream out = socket.getOutputStream();
			out.write(String.format("%s %s HTTP/1.1\r\n", "GET", "/agb/p2p")
				.getBytes());
			out.write(String.format("Host: %s:%d\r\n", host, port).getBytes());
			out
				.write("Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\r\n"
					.getBytes());
			out.write("Connection: keep-alive\r\n".getBytes());
			out.write("\r\n\r\n".getBytes());
			Socket accept = server.accept();
			System.out.println(new String(InputStreamUtil.getBytes(accept
				.getInputStream())));
			accept.close();
		}
	}
}
