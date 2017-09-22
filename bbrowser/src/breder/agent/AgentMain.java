package breder.agent;

import java.io.IOException;

import breder.net.MasterServiceSocket;
import breder.request.GodInfoProtocol;

public class AgentMain {

	public static void main(String[] args) {
		for (;;) {
			try {
				String host = GodInfoProtocol.request();
				if (host != null) {
					MasterServiceSocket socket = new MasterServiceSocket(host);
					while (socket.isConnected()) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
						}
					}
				}
			} catch (IOException e) {
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

}
