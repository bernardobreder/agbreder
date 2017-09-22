import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	
	private ServerSocket server;
	
	/**
	 * Construtor
	 * 
	 * @throws IOException
	 */
	public Server(int port) throws IOException {
		this.setName("Server Accept " + port);
		this.server = new ServerSocket(port);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		for (;;) {
			try {
				Socket socket = this.server.accept();
				new Client(socket).start();
			}
			catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
}
