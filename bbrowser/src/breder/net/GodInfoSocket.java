package breder.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GodInfoSocket extends Socket {

	private DataInputStream dataInputStream;
	
	private DataOutputStream dataOutputStream;

	public GodInfoSocket() throws IOException {
		super("localhost", 9000);
		this.dataInputStream = new DataInputStream(this.getInputStream());
		this.dataOutputStream = new DataOutputStream(this.getOutputStream());
	}

	/**
	 * @return the in
	 */
	public DataInputStream getDataInputStream() {
		return dataInputStream;
	}

	/**
	 * @return the out
	 */
	public DataOutputStream getDataOutputStream() {
		return dataOutputStream;
	}

}
