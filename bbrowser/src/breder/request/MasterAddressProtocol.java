package breder.request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;

import breder.net.GodInfoSocket;

public abstract class MasterAddressProtocol extends Protocol {

	public static void request(int port) throws IOException {
		String ip = Inet4Address.getLocalHost().getHostAddress();
		GodInfoSocket socket = new GodInfoSocket();
		DataOutputStream out = socket.getDataOutputStream();
		out.writeInt(ProtocolType.MASTER_ADDRESS.ordinal());
		out.writeUTF(ip + ":" + port);
		socket.close();
	}

	public static String receive(DataInputStream in) throws IOException {
		return in.readUTF();
	}

}
