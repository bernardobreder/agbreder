package breder.god;

public class GodMain {

	public static void main(String[] args) {
		new GodInfoServer().start();
		for (int n = 0; n < 4; n++) {
			new GodServer(n).start();
		}
	}

}
