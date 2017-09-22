import java.io.IOException;
import java.net.Socket;

public class Client extends Thread {
	
	/** Socket */
	private Socket socket;
	
	/**
	 * Construtor
	 * 
	 * @param socket
	 */
	public Client(Socket socket) {
		this.setName("Server Client");
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			this.socket.getOutputStream().write((this.socket + "\n\n").getBytes());
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		finally {
			try {
				this.socket.close();
			}
			catch (IOException e) {
			}
		}
	}
	
}
