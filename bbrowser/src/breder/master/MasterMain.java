package breder.master;

import java.util.Random;

import breder.net.GodServiceSocket;

public class MasterMain {

	public static void main(String[] args) {
		for (;;) {
			for (int index = 0; index < GodServiceSocket.size(); index++) {
				try {
					Random random = new Random(System.currentTimeMillis());
					for (int n = 0; n < 10; n++) {
						new MasterServer(random.nextLong()).start();
					}
					GodServiceSocket socket = new GodServiceSocket(index);
					while (socket.isConnected()) {
						Thread.sleep(100);
					}
					socket.close();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			System.out.println("Wainting for 5 second...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}
		}
	}

}
