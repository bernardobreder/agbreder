package breder.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MasterServiceSocket extends Socket {

	private DataInputStream dataInputStream;

	private DataOutputStream dataOutputStream;

	public MasterServiceSocket(String host) throws IOException {
		super(host.substring(0, host.indexOf(':')), Integer.parseInt(host.substring(host.indexOf(':') + 1)));
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
