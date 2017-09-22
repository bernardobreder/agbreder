package breder.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GodServiceSocket extends Socket {

	private DataInputStream dataInputStream;

	private DataOutputStream dataOutputStream;

	public GodServiceSocket(int index) throws IOException {
		super("localhost", 9001 + index);
		this.dataInputStream = new DataInputStream(this.getInputStream());
		this.dataOutputStream = new DataOutputStream(this.getOutputStream());
	}

	/**
	 * Retorna o número de servidores
	 * 
	 * @return
	 */
	public static int size() {
		return 4;
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
