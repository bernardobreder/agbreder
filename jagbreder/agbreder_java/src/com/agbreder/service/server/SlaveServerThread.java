package com.agbreder.service.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.agbreder.client.BrowserFrame;

/**
 * Carrega o servidor no modo console
 * 
 * @author bernardobreder
 */
public class SlaveServerThread extends Thread {
	
	public static final int PORT = 1333;
	
	private ServerSocket server;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		try {
			server = new ServerSocket(PORT);
			while (BrowserFrame.getInstance().isVisible()) {
				try {
					Socket socket = server.accept();
					ClientThread client = new ClientThread(socket);
					client.start();
				} catch (Throwable t) {
					if (!server.isClosed()) {
						t.printStackTrace();
					}
				}
			}
			server.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Fecha o servidor
	 * 
	 * @throws IOException
	 */
	public void close() {
		try {
			server.close();
		} catch (IOException e) {
		}
	}
	
}
