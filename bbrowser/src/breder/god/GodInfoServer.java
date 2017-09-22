package breder.god;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import breder.request.GodInfoProtocol;
import breder.request.MasterAddressProtocol;
import breder.request.ProtocolType;

public class GodInfoServer extends Thread {

	private final List<String> hosts = new ArrayList<String>();

	public GodInfoServer() {
		super(GodInfoServer.class.getSimpleName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		try {
			ServerSocket server = new ServerSocket(9000);
			for (;;) {
				try {
					Socket socket = server.accept();
					DataInputStream in = new DataInputStream(socket.getInputStream());
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					int code = in.readInt();
					if (code == ProtocolType.GOD_INFO.ordinal()) {
						respGodInfo(in, out);
					} else if (code == ProtocolType.MASTER_ADDRESS.ordinal()) {
						respMasterAddress(in, out);
					}
					socket.close();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void respGodInfo(DataInputStream in, DataOutputStream out) throws IOException {
		String host = null;
		if (hosts.size() > 0) {
			Random random = new Random(System.currentTimeMillis());
			host = hosts.remove(random.nextInt(hosts.size()));
		}
		GodInfoProtocol.response(out, host);
	}

	private void respMasterAddress(DataInputStream in, DataOutputStream out) throws IOException {
		hosts.add(MasterAddressProtocol.receive(in));
	}

}
