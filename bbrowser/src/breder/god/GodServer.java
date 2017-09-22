package breder.god;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GodServer extends Thread {

	private final int portIndex;

	public GodServer(int portIndex) {
		super(GodServer.class.getSimpleName());
		this.portIndex = portIndex;
	}

	@Override
	public void run() {
		try {
			int port = 9001 + this.portIndex;
			ServerSocket server = new ServerSocket(port);
			for (;;) {
				try {
					Socket socket = server.accept();
					socket.getOutputStream().write("TST".getBytes("utf-8"));
					socket.close();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
