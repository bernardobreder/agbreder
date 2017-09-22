package breder.request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import breder.net.GodInfoSocket;

public abstract class GodInfoProtocol extends Protocol {

	public static void response(DataOutputStream out, String host) throws IOException {
		if (host == null) {
			out.writeBoolean(false);
		} else {
			out.writeBoolean(true);
			out.writeUTF(host);
		}
		out.flush();
	}

	public static String request() throws IOException {
		GodInfoSocket socket = new GodInfoSocket();
		try {
			DataOutputStream out = socket.getDataOutputStream();
			out.writeInt(ProtocolType.GOD_INFO.ordinal());
			out.flush();
			DataInputStream in = socket.getDataInputStream();
			boolean exist = in.readBoolean();
			if (exist) {
				return in.readUTF();
			} else {
				return null;
			}
		} finally {
			socket.close();
		}
	}

}
