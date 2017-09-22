package com.age;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.WeakHashMap;

import com.age.db.SqlLiteConnectionTest;

/**
 * Inicializador do servidor
 * 
 * @author bernardobreder
 */
public class Main {

	public static final Map<String, byte[]> cache = new WeakHashMap<String, byte[]>(1024);

	public static class ClientThread extends Thread {
		private final Socket socket;

		public ClientThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				{
					System.out.print("Request : [");
					InputStream input = this.socket.getInputStream();
					for (int n; ((n = input.read()) > 0);) {
						System.out.print((char) n);
					}
					System.out.println("]");
				}
				byte[] bytes = cache.get("");
				if (bytes == null) {
					InputStream input = new FileInputStream("../agbreder_bdk/binary.agbc");
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					for (int n; ((n = input.read()) != -1);) {
						output.write((char) n);
					}
					input.close();
					cache.put("", bytes = output.toByteArray());
				}
				OutputStream output = socket.getOutputStream();
				output.write(bytes);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
