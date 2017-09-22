package breder.master;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import breder.request.MasterAddressProtocol;

public class MasterServer extends Thread {

	private final long seed;

	public MasterServer(long seed) {
		super(MasterServer.class.getSimpleName());
		this.seed = seed;
	}

	@Override
	public void run() {
		Random random = new Random(seed);
		for (;;) {
			try {
				int port = random.nextInt(65536);
				ServerSocket server = new ServerSocket(port);
				try {
					MasterAddressProtocol.request(port);
					Socket socket = server.accept();
					try {
						DataInputStream in = new DataInputStream(socket.getInputStream());
						for (;;) {
							int code = in.readInt();
							System.out.println(code);
						}
					} finally {
						socket.close();
					}
				} finally {
					server.close();
				}
			} catch (IOException e) {
			}
		}
	}

}
